package com.online.voting.election.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.online.voting.election.clients.CandidateClient;
import com.online.voting.election.dtos.AssignCandidateResponse;
import com.online.voting.election.dtos.PositionRequest;
import com.online.voting.election.dtos.PositionResponse;
import com.online.voting.election.handler.AlreadyExistsException;
import com.online.voting.election.handler.BadRequestException;
import com.online.voting.election.models.Election;
import com.online.voting.election.models.Position;
import com.online.voting.election.models.PositionCandidate;
import com.online.voting.election.models.PositionStatus;
import com.online.voting.election.repository.ElectionRepository;
import com.online.voting.election.repository.PositionCandidateRepository;
import com.online.voting.election.repository.PositionRepository;

import jakarta.transaction.Transactional;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final ElectionRepository electionRepository;
    private final CandidateClient candidateClient;
    private final PositionCandidateRepository positionCandidateRepository;

    public PositionService(PositionRepository positionRepository, ElectionRepository electionRepository,
            CandidateClient candidateClient, PositionCandidateRepository positionCandidateRepository) {
        this.positionRepository = positionRepository;
        this.electionRepository = electionRepository;
        this.candidateClient = candidateClient;
        this.positionCandidateRepository = positionCandidateRepository;
    }

    // ================= CREATE =================
    public PositionResponse createPosition(PositionRequest request) {
        Election election = electionRepository.findById(request.getElectionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Election not found with ID: " + request.getElectionId()));

        if (positionRepository.existsByElectionAndName(election, request.getName())) {
            throw new AlreadyExistsException(
                    "Position with the same name already exists in this election");
        }

        Position position = new Position();
        position.setElection(election);
        position.setName(request.getName());
        position.setDescription(request.getDescription());
        position.setMaxVotes(request.getMaxVotes() != null ? request.getMaxVotes() : 1);
        position.setStatus(request.getStatus() != null ? request.getStatus()
                : com.online.voting.election.models.PositionStatus.DRAFT);
        position.setDisplayOrder(
                request.getDisplayOrder() != null ? request.getDisplayOrder() : getNextDisplayOrder(election));

        positionRepository.save(position);

        return mapToResponse(position);
    }

    // ================= UPDATE =================
    public PositionResponse updatePosition(UUID positionId, PositionRequest request) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found with ID: " + positionId));

        Election election = electionRepository.findById(request.getElectionId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Election not found with ID: " + request.getElectionId()));

        if (positionRepository.existsByElectionAndName(election, request.getName())
                && !position.getName().equals(request.getName())) {
            throw new ResourceNotFoundException("Another position with the same name exists in this election");
        }

        position.setElection(election);
        position.setName(request.getName());
        position.setDescription(request.getDescription());
        position.setMaxVotes(request.getMaxVotes());
        position.setStatus(request.getStatus());
        position.setDisplayOrder(request.getDisplayOrder());

        positionRepository.save(position);

        return mapToResponse(position);
    }

    // ================= DELETE =================
    public void deletePosition(UUID positionId) {

        Position position = positionRepository.findByPositionId(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found"));

        positionRepository.delete(position);
    }

    // ================= GET ALL =================
    public Page<PositionResponse> getAllPositions(
            UUID electionId,
            PositionStatus status,
            Pageable pageable) {

        Specification<Position> spec = Specification.where(null);

        if (electionId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("election").get("electionId"), electionId));
        }

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        Page<Position> positionsPage = positionRepository.findAll(spec, pageable);

        return positionsPage.map(this::mapToResponse);
    }

    // ================= GET BY POSITION ELECTION =================
    public Page<PositionResponse> getPositionsByElection(UUID electionId, Pageable pageable) {

        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with ID: " + electionId));

        return positionRepository
                .findByElection(election, pageable)
                .map(this::mapToResponse);
    }

    // ================= ASSIGN POSITION TO CANDIDATE =================

    @Transactional
    public AssignCandidateResponse assignPositionToCandidate(UUID electionId,
            UUID positionId,
            UUID candidateId) {

        // Validate election and position existence and relationship
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with ID: " + electionId));

        // Validate position existence and that it belongs to the election
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found with ID: " + positionId));

        // Ensure the position belongs to the specified election
        if (!position.getElection().getElectionId().equals(election.getElectionId())) {
            throw new BadRequestException("Position does not belong to the specified election");
        }

        // Feign call will throw FeignException.NotFound if candidate doesn't exist
        candidateClient.verifyCandidateExists(candidateId);

        // Idempotency check
        boolean alreadyAssigned = positionCandidateRepository.existsByPositionIdAndCandidateId(positionId, candidateId);

        if (alreadyAssigned) {
            return new AssignCandidateResponse(
                    electionId,
                    positionId,
                    candidateId,
                    null,
                    "Candidate already assigned to this position");
        }

        PositionCandidate assignment = new PositionCandidate();
        assignment.setPosition(position);
        assignment.setCandidateId(candidateId);
        assignment.setAssignedAt(LocalDateTime.now());

        // duplicate insert throws DataIntegrityViolationException
        positionCandidateRepository.save(assignment);

        return new AssignCandidateResponse(
                electionId,
                positionId,
                candidateId,
                assignment.getAssignedAt(),
                "Candidate successfully assigned");
    }

    // ================= HELPERS =================
    private PositionResponse mapToResponse(Position p) {
        return new PositionResponse(
                p.getPositionId(),
                p.getElection().getElectionId(),
                p.getName(),
                p.getDescription(),
                p.getMaxVotes(),
                p.getStatus(),
                p.getDisplayOrder(),
                p.getCreatedAt(),
                p.getUpdatedAt());
    }

    private int getNextDisplayOrder(Election election) {
        Integer maxOrder = positionRepository.findMaxDisplayOrderByElection(election.getElectionId());
        return maxOrder != null ? maxOrder + 1 : 1;
    }
}

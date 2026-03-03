package com.online.voting.election.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.voting.election.clients.CandidateClient;
import com.online.voting.election.dtos.ApiResponse;
import com.online.voting.election.dtos.CandidateResponse;
import com.online.voting.election.dtos.PositionCandidateDetailsResponse;
import com.online.voting.election.models.Election;
import com.online.voting.election.models.Position;
import com.online.voting.election.models.PositionCandidate;
import com.online.voting.election.repository.PositionCandidateRepository;

@Service
public class PositionCandidateService {

    private final PositionCandidateRepository positionCandidateRepository;
    private final CandidateClient candidateClient;

    public PositionCandidateService(PositionCandidateRepository positionCandidateRepository,
            CandidateClient candidateClient) {
        this.positionCandidateRepository = positionCandidateRepository;
        this.candidateClient = candidateClient;
    }

    // ================= GET CANDIDATES BY POSITION =================
    @Transactional(readOnly = true)
    public List<PositionCandidateDetailsResponse> getCandidatesByPosition(UUID positionId) {

        List<PositionCandidate> assignments = positionCandidateRepository.findByPosition_PositionId(positionId);

        if (assignments.isEmpty()) {
            return List.of();
        }

        // 1️⃣ Extract candidate IDs
        List<UUID> candidateIds = assignments.stream()
                .map(PositionCandidate::getCandidateId)
                .toList();

        // 2️⃣ Call candidate service ONCE
        ApiResponse<List<CandidateResponse>> candidateApi = candidateClient.getCandidatesByIds(candidateIds);

        List<CandidateResponse> candidates = candidateApi.getData();

        // 3️⃣ Convert list to Map for fast lookup
        Map<UUID, CandidateResponse> candidateMap = candidates.stream()
                .collect(Collectors.toMap(
                        CandidateResponse::getCandidateId,
                        c -> c));

        // 4️⃣ Build response
        return assignments.stream().map(assignment -> {

            Position position = assignment.getPosition();
            Election election = position.getElection();

            CandidateResponse candidate = candidateMap.get(assignment.getCandidateId());

            boolean exists = candidate != null;

            return new PositionCandidateDetailsResponse(
                    election.getElectionId(),
                    election.getTitle(),
                    position.getPositionId(),
                    position.getName(),
                    assignment.getCandidateId(),
                    exists ? candidate.getFirstName() : null,
                    exists ? candidate.getLastName() : null,
                    exists,
                    assignment.getAssignedAt());

        }).toList();
    }

    @Transactional(readOnly = true)
    public List<PositionCandidateDetailsResponse> getCandidatesByElection(UUID electionId) {

        List<PositionCandidate> assignments = positionCandidateRepository
                .findByPosition_Election_ElectionId(electionId);

        if (assignments.isEmpty()) {
            return List.of();
        }

        // 1️⃣ Extract candidate IDs
        List<UUID> candidateIds = assignments.stream()
                .map(PositionCandidate::getCandidateId)
                .toList();

        // 2️⃣ Call candidate service ONCE
        ApiResponse<List<CandidateResponse>> candidateApi = candidateClient.getCandidatesByIds(candidateIds);

        List<CandidateResponse> candidates = candidateApi.getData();

        // 3️⃣ Convert list to Map for fast lookup
        Map<UUID, CandidateResponse> candidateMap = candidates.stream()
                .collect(Collectors.toMap(
                        CandidateResponse::getCandidateId,
                        c -> c));

        // 4️⃣ Build response
        return assignments.stream().map(assignment -> {

            Position position = assignment.getPosition();
            Election election = position.getElection();

            CandidateResponse candidate = candidateMap.get(assignment.getCandidateId());

            boolean exists = candidate != null;

            return new PositionCandidateDetailsResponse(
                    election.getElectionId(),
                    election.getTitle(),
                    position.getPositionId(),
                    position.getName(),
                    assignment.getCandidateId(),
                    exists ? candidate.getFirstName() : null,
                    exists ? candidate.getLastName() : null,
                    exists,
                    assignment.getAssignedAt());

        }).toList();
    }

}

package com.online.voting.election.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.online.voting.election.dtos.ApiResponse;
import com.online.voting.election.dtos.CreateElectionRequest;
import com.online.voting.election.dtos.ElectionResponse;
import com.online.voting.election.dtos.MessageResponse;
import com.online.voting.election.dtos.UpdateElectionRequest;
import com.online.voting.election.dtos.UpdateElectionStatusRequest;
import com.online.voting.election.handler.ElectionNotFoundException;
import com.online.voting.election.models.Election;
import com.online.voting.election.models.ElectionStatus;
import com.online.voting.election.repository.ElectionRepository;

@Service
public class ElectionService {

    private final ElectionRepository electionRepository;

    public ElectionService(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    // createElection method
    public MessageResponse createElection(CreateElectionRequest request) {
        if (electionRepository.existsByTitle(request.getTitle())) {
            throw new IllegalArgumentException("Election with the same title already exists");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        try {
            Election election = new Election();
            election.setTitle(request.getTitle());
            election.setStartDate(request.getStartDate());
            election.setEndDate(request.getEndDate());
            election.setStatus(ElectionStatus.DRAFT);

            electionRepository.save(election);

            return new MessageResponse("Election created successfully");
        } catch (Exception e) {
            return new MessageResponse("Failed to create election: " + e.getMessage());

        }
    }

    // UpdateElection Method
    public MessageResponse updateElection(UUID electionId, UpdateElectionRequest request) {

        // find if election exists
        if (!electionRepository.findByElectionId(electionId).isPresent()) {
            throw new ElectionNotFoundException(String.format("Election with ID %s not found", electionId));
        }
        // find election by title and check if it exists and is not the same election
        // being updated
        if (electionRepository.existsByTitle(request.getTitle()) && !electionRepository
                .findByElectionId(electionId).get().getTitle().equals(request.getTitle())) {
            return new MessageResponse("Election with the same title already exists");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException(
                    "Election end date/time cannot be before start date/time");
        }

        try {
            Election existingElection = electionRepository.findByElectionId(electionId).get();
            existingElection.setTitle(request.getTitle());
            existingElection.setStartDate(request.getStartDate());
            existingElection.setEndDate(request.getEndDate());
            existingElection.setStatus(request.getStatus());
            existingElection.setUpdatedAt(LocalDateTime.now());

            electionRepository.save(existingElection);

            return new MessageResponse("Election updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update election: ");
        }
    }

    // DeleteElection Method
    public MessageResponse deleteElection(UUID electionId) {
        try {
            if (!electionRepository.findByElectionId(electionId).isPresent()) {
                throw new ElectionNotFoundException(String.format("Election with ID %s not found", electionId));
            }
            electionRepository.deleteById(electionId);
            return new MessageResponse("Election deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete election: ");
        }
    }

    // GetElectionByTitle Method
    public ElectionResponse getElectionByTitle(String title) {
        Election election = electionRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Election not found"));

        return mapToResponse(election);
    }

    // UpdateElectionStatus Method
    public MessageResponse updateElectionStatus(UpdateElectionStatusRequest statusRequest) {
        UUID electionId = statusRequest.getElectionId();
        ElectionStatus status = statusRequest.getStatus();

        try {

            Election election = electionRepository.findById(electionId)
                    .orElseThrow(() -> new ElectionNotFoundException(
                            String.format("Election with ID %s not found", electionId)));

            if (status == ElectionStatus.OPEN &&
                    LocalDateTime.now().isBefore(election.getStartDate())) {
                return new MessageResponse("Election cannot open before start time");
            }

            if (status == ElectionStatus.CLOSED &&
                    LocalDateTime.now().isBefore(election.getEndDate())) {
                return new MessageResponse("Election cannot close before end time");
            }
            election.setStatus(status);
            election.setUpdatedAt(LocalDateTime.now());

            electionRepository.save(election);

            return new MessageResponse("Election status updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update election status: ");
        }
    }

    // RetrieveAllElections Method
    public ApiResponse<List<ElectionResponse>> getAllElections() {

        List<ElectionResponse> elections = electionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new ApiResponse<>(true, "Elections retrieved successfully", elections);
    }

    // get election by id
    public ElectionResponse getElectionById(UUID electionId) {
        Election election = electionRepository.findByElectionId(electionId)
                .orElseThrow(() -> new ElectionNotFoundException(
                        String.format("Election with ID %s not found", electionId)));

        return mapToResponse(election);
    }

    // get multiple elections by their IDs (for Voting Service)
    public List<ElectionResponse> getElectionsByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty())
            return List.of();

        List<Election> elections = electionRepository.findAllById(ids);

        return elections.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private boolean isVotingAllowed(Election election) {

        LocalDateTime now = LocalDateTime.now();

        return (election.getStatus() == ElectionStatus.OPEN
                || election.getStatus() == ElectionStatus.INPROGRESS)
                && !now.isBefore(election.getStartDate())
                && !now.isAfter(election.getEndDate());
    }

    // Helper method to map Election to ElectionResponse
    private ElectionResponse mapToResponse(Election election) {

        ElectionResponse response = new ElectionResponse();

        response.setElectionId(election.getElectionId());
        response.setTitle(election.getTitle());
        response.setStartDate(election.getStartDate());
        response.setEndDate(election.getEndDate());
        response.setStatus(election.getStatus());
        response.setCanVote(isVotingAllowed(election));
        response.setCreatedAt(election.getCreatedAt());
        response.setUpdatedAt(election.getUpdatedAt());

        return response;
    }

}
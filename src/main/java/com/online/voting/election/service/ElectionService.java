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
    public MessageResponse updateElection(UpdateElectionRequest request) {

        // find if election exists
        if (!electionRepository.findByElectionId(request.getElectionId()).isPresent()) {
            return new MessageResponse("Election not found");
        }
        // find election by title and check if it exists and is not the same election
        // being updated
        if (electionRepository.existsByTitle(request.getTitle()) && !electionRepository
                .findByElectionId(request.getElectionId()).get().getTitle().equals(request.getTitle())) {
            return new MessageResponse("Election with the same title already exists");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException(
                    "Election end date/time cannot be before start date/time");
        }

        try {
            Election existingElection = electionRepository.findByElectionId(request.getElectionId()).get();
            existingElection.setTitle(request.getTitle());
            existingElection.setStartDate(request.getStartDate());
            existingElection.setEndDate(request.getEndDate());
            existingElection.setStatus(request.getStatus());
            existingElection.setUpdatedAt(LocalDateTime.now());

            electionRepository.save(existingElection);

            return new MessageResponse("Election updated successfully");
        } catch (Exception e) {
            return new MessageResponse("Failed to update election: " + e.getMessage());
        }
    }

    // DeleteElection Method
    public MessageResponse deleteElection(UUID electionId) {
        try {
            if (!electionRepository.findByElectionId(electionId).isPresent()) {
                return new MessageResponse("Election not found");
            }
            electionRepository.deleteById(electionId);
            return new MessageResponse("Election deleted successfully");
        } catch (Exception e) {
            return new MessageResponse("Failed to delete election: " + e.getMessage());
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
                    .orElseThrow(() -> new IllegalArgumentException("Election not found with ID: " + electionId));

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
            return new MessageResponse("Failed to update election status: " + e.getMessage());
        }
    }

    // RetrieveAllElections Method
    public ApiResponse<List<ElectionResponse>> getAllElections() {

        List<ElectionResponse> elections = electionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new ApiResponse<>("Elections retrieved successfully", elections);
    }

    // Helper method to map Election to ElectionResponse
    private ElectionResponse mapToResponse(Election election) {
        return new ElectionResponse(
                election.getElectionId(),
                election.getTitle(),
                election.getStartDate(),
                election.getEndDate(),
                election.getStatus(),
                election.getCreatedAt(),
                election.getUpdatedAt());
    }

}
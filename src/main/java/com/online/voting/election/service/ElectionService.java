package com.online.voting.election.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.online.voting.election.dtos.CreateElectionRequest;
import com.online.voting.election.dtos.ElectionResponse;
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
    public ElectionResponse createElection(CreateElectionRequest request) {
        if (electionRepository.existsByTitle(request.getTitle())) {
            throw new IllegalArgumentException("Election with the same title already exists");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException(
                    "Election end date/time cannot be before start date/time");
        }

        try {
            Election election = new Election();
            election.setTitle(request.getTitle());

            election.setStartDate(request.getStartDate());
            election.setEndDate(request.getEndDate());
            election.setStatus(ElectionStatus.DRAFT);
            election.setCreatedAt(LocalDateTime.now());

            electionRepository.save(election);

            return new ElectionResponse("Election created successfully");
        } catch (Exception e) {
            // Option 1: return a failure response
            return new ElectionResponse("Failed to create election: " + e.getMessage());

            // Option 2: rethrow as runtime exception (if you want to bubble up)
            // throw new RuntimeException("Failed to create election: " + e.getMessage(),
            // e);
        }
    }

    // UpdateElection Method
    public ElectionResponse updateElection(UpdateElectionRequest request) {

        // find if election exists
        if (!electionRepository.findByElectionId(request.getElectionId()).isPresent()) {
            return new ElectionResponse("Election not found");
        }
        // find election by title and check if it exists and is not the same election
        // being updated
        if (electionRepository.existsByTitle(request.getTitle()) && !electionRepository
                .findByElectionId(request.getElectionId()).get().getTitle().equals(request.getTitle())) {
            return new ElectionResponse("Election with the same title already exists");
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

            return new ElectionResponse("Election updated successfully");
        } catch (Exception e) {
            return new ElectionResponse("Failed to update election: " + e.getMessage());
        }
    }

    // DeleteElection Method
    public ElectionResponse deleteElection(UUID electionId) {
        try {
            if (!electionRepository.findByElectionId(electionId).isPresent()) {
                return new ElectionResponse("Election not found");
            }
            electionRepository.deleteById(electionId);
            return new ElectionResponse("Election deleted successfully");
        } catch (Exception e) {
            return new ElectionResponse("Failed to delete election: " + e.getMessage());
        }
    }

    // GetElectionByTitle Method
    public Election getElectionByTitle(String title) {
        return electionRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Election not found with title: " + title));
    }

    // UpdateElectionStatus Method
    public ElectionResponse updateElectionStatus(UpdateElectionStatusRequest statusRequest) {
        UUID electionId = statusRequest.getElectionId();
        ElectionStatus status = statusRequest.getStatus();

        try {

            Election election = electionRepository.findById(electionId)
                    .orElseThrow(() -> new IllegalArgumentException("Election not found with ID: " + electionId));

            if (status == ElectionStatus.OPEN &&
                    LocalDateTime.now().isBefore(election.getStartDate())) {
                return new ElectionResponse("Election cannot open before start time");
            }

            if (status == ElectionStatus.CLOSED &&
                    LocalDateTime.now().isBefore(election.getEndDate())) {
                return new ElectionResponse("Election cannot close before end time");
            }
            election.setStatus(status);
            election.setUpdatedAt(LocalDateTime.now());

            electionRepository.save(election);

            return new ElectionResponse("Election status updated successfully");
        } catch (Exception e) {
            return new ElectionResponse("Failed to update election status: " + e.getMessage());
        }
    }

}
package com.online.voting.election.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public class PositionCandidateDetailsResponse {

    private UUID electionId;
    private String electionTitle;

    private UUID positionId;
    private String positionName;

    private UUID candidateId;
    private String firstName;
    private String lastName;
    private boolean candidateExists;

    private LocalDateTime assignedAt;

    // constructor
    public PositionCandidateDetailsResponse(
            UUID electionId,
            String electionTitle,
            UUID positionId,
            String positionName,
            UUID candidateId,
            String firstName,
            String lastName,
            boolean candidateExists,
            LocalDateTime assignedAt) {

        this.electionId = electionId;
        this.electionTitle = electionTitle;
        this.positionId = positionId;
        this.positionName = positionName;
        this.candidateId = candidateId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.candidateExists = candidateExists;
        this.assignedAt = assignedAt;
    }

    public PositionCandidateDetailsResponse() {
    }

    public UUID getElectionId() {
        return this.electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public String getElectionTitle() {
        return this.electionTitle;
    }

    public void setElectionTitle(String electionTitle) {
        this.electionTitle = electionTitle;
    }

    public UUID getPositionId() {
        return this.positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return this.positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public UUID getCandidateId() {
        return this.candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isCandidateExists() {
        return this.candidateExists;
    }

    public void setCandidateExists(boolean candidateExists) {
        this.candidateExists = candidateExists;
    }

    public LocalDateTime getAssignedAt() {
        return this.assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}

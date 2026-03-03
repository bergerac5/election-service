package com.online.voting.election.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public class AssignCandidateResponse {
    private UUID electionId;
    private String electionName;

    private UUID positionId;
    private String positionName;

    private UUID candidateId;
    private String candidateFirstName;
    private String candidateLastName;

    private LocalDateTime assignedAt;
    private String message;

    public AssignCandidateResponse() {
    }

    public AssignCandidateResponse(UUID electionId, String electionName, UUID positionId, String positionName,
            UUID candidateId, String candidateFirstName, String candidateLastName, LocalDateTime assignedAt,
            String message) {
        this.electionId = electionId;
        this.electionName = electionName;
        this.positionId = positionId;
        this.positionName = positionName;
        this.candidateId = candidateId;
        this.candidateFirstName = candidateFirstName;
        this.candidateLastName = candidateLastName;
        this.assignedAt = assignedAt;
        this.message = message;
    }

    public UUID getElectionId() {
        return this.electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public String getElectionName() {
        return this.electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
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

    public String getCandidateFirstName() {
        return this.candidateFirstName;
    }

    public void setCandidateFirstName(String candidateFirstName) {
        this.candidateFirstName = candidateFirstName;
    }

    public String getCandidateLastName() {
        return this.candidateLastName;
    }

    public void setCandidateLastName(String candidateLastName) {
        this.candidateLastName = candidateLastName;
    }

    public LocalDateTime getAssignedAt() {
        return this.assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

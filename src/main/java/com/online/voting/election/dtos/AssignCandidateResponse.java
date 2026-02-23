package com.online.voting.election.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public class AssignCandidateResponse {
    private UUID electionId;
    private UUID positionId;
    private UUID candidateId;
    private LocalDateTime assignedAt;
    private String message;

    public AssignCandidateResponse(UUID electionId,
            UUID positionId,
            UUID candidateId,
            LocalDateTime assignedAt,
            String message) {
        this.electionId = electionId;
        this.positionId = positionId;
        this.candidateId = candidateId;
        this.assignedAt = assignedAt;
        this.message = message;
    }

    public AssignCandidateResponse() {
    }

    public UUID getElectionId() {
        return this.electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public UUID getPositionId() {
        return this.positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }

    public UUID getCandidateId() {
        return this.candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
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

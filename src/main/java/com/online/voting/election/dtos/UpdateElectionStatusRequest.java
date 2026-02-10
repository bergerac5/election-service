package com.online.voting.election.dtos;

import java.util.UUID;

import com.online.voting.election.models.ElectionStatus;

import jakarta.validation.constraints.NotBlank;

public class UpdateElectionStatusRequest {

    @NotBlank(message = "Election ID is required")
    private UUID electionId;

    @NotBlank(message = "Status is required")
    private ElectionStatus status;

    public UpdateElectionStatusRequest() {
    }

    public UpdateElectionStatusRequest(UUID electionId, ElectionStatus status) {
        this.electionId = electionId;
        this.status = status;
    }

    public UUID getElectionId() {
        return this.electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public ElectionStatus getStatus() {
        return this.status;
    }

    public void setStatus(ElectionStatus status) {
        this.status = status;
    }

}

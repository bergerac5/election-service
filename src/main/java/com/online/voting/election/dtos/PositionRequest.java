package com.online.voting.election.dtos;

import java.util.UUID;

import com.online.voting.election.models.PositionStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PositionRequest {

    @NotNull(message = "Election ID is required")
    private UUID electionId;

    @NotBlank(message = "Position name is required")
    private String name;

    @NotBlank(message = "Position description is required")
    @Max(value = 100, message = "Description cannot exceed 100 characters")
    private String description;

    @NotNull(message = "Position max votes is required")
    @Min(value = 1, message = "Max votes must be at least 1")
    private Integer maxVotes;

    @NotNull(message = "Position display order is required")
    @Min(value = 1, message = "Display order must be at least 1")
    private Integer displayOrder;

    @NotNull(message = "Position status is required")
    private PositionStatus status;

    public PositionRequest() {
    }

    public PositionRequest(UUID electionId, String name, String description, Integer maxVotes,
            Integer displayOrder, PositionStatus status) {
        this.electionId = electionId;
        this.name = name;
        this.description = description;
        this.maxVotes = maxVotes;
        this.displayOrder = displayOrder;
        this.status = status;
    }

    // Getters and setters
    public UUID getElectionId() {
        return electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxVotes() {
        return maxVotes;
    }

    public void setMaxVotes(Integer maxVotes) {
        this.maxVotes = maxVotes;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public PositionStatus getStatus() {
        return status;
    }

    public void setStatus(PositionStatus status) {
        this.status = status;
    }
}

package com.online.voting.election.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.online.voting.election.models.PositionStatus;

public class PositionResponse {

    private UUID positionId;
    private UUID electionId;
    private String name;
    private String description;
    private Integer maxVotes;
    private PositionStatus status;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public PositionResponse() {
    }

    // Full constructor
    public PositionResponse(UUID positionId, UUID electionId, String name, String description,
            Integer maxVotes, PositionStatus status, Integer displayOrder,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.positionId = positionId;
        this.electionId = electionId;
        this.name = name;
        this.description = description;
        this.maxVotes = maxVotes;
        this.status = status;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public UUID getPositionId() {
        return positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }

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

    public PositionStatus getStatus() {
        return status;
    }

    public void setStatus(PositionStatus status) {
        this.status = status;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}

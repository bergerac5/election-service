package com.online.voting.election.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.online.voting.election.models.ElectionStatus;

public class ElectionResponse {

    private UUID electionId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ElectionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ElectionResponse() {
    }

    public ElectionResponse(UUID electionId, String title,
            LocalDateTime startDate,
            LocalDateTime endDate,
            ElectionStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.electionId = electionId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public UUID getElectionId() {
        return electionId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public ElectionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

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
    private boolean canVote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ElectionResponse() {
    }

    public ElectionResponse(
            UUID electionId,
            String title,
            LocalDateTime startDate,
            LocalDateTime endDate,
            ElectionStatus status,
            boolean canVote,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.electionId = electionId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.canVote = canVote;
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

    public boolean canVote() {
        return canVote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setStatus(ElectionStatus status) {
        this.status = status;
    }

    public boolean isCanVote() {
        return canVote;
    }

    public void setCanVote(boolean canVote) {
        this.canVote = canVote;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

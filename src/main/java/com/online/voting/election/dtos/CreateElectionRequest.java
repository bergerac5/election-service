package com.online.voting.election.dtos;

import java.time.LocalDateTime;

import com.online.voting.election.models.ElectionStatus;

import jakarta.validation.constraints.NotBlank;

public class CreateElectionRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Start date is required")
    private LocalDateTime startDate;
    @NotBlank(message = "End date is required")
    private LocalDateTime endDate;
    @NotBlank(message = "Status is required")
    private ElectionStatus status;

    public CreateElectionRequest() {
    }

    public CreateElectionRequest(String title, LocalDateTime startDate, LocalDateTime endDate, ElectionStatus status) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public ElectionStatus getStatus() {
        return this.status;
    }

    public void setStatus(ElectionStatus status) {
        this.status = status;
    }

}

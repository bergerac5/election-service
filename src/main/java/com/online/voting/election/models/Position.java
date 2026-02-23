package com.online.voting.election.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "position", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "election_id", "name" })
})
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID positionId;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<PositionCandidate> candidates;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    @Column(nullable = false)
    private String name;

    @Column(length = 100)
    private String description;

    @Column(nullable = false)
    private Integer maxVotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PositionStatus status;

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Position() {
    }

    public Position(List<PositionCandidate> candidates, Election election, String name, String description,
            Integer maxVotes,
            PositionStatus status,
            Integer displayOrder) {
        this.candidates = candidates;
        this.election = election;
        this.name = name;
        this.description = description;
        this.maxVotes = maxVotes;
        this.status = status;
        this.displayOrder = displayOrder;
    }

    // Getters and Setters
    public List<PositionCandidate> getCandidates() {
        return this.candidates;
    }

    public void setCandidates(List<PositionCandidate> candidates) {
        this.candidates = candidates;
    }

    public UUID getPositionId() {
        return positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Integer getMaxVotes() {
        return this.maxVotes;
    }

    public void setMaxVotes(Integer maxVotes) {
        this.maxVotes = maxVotes;
    }

    public PositionStatus getStatus() {
        return this.status;
    }

    public void setStatus(PositionStatus status) {
        this.status = status;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Lifecycle callbacks for timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

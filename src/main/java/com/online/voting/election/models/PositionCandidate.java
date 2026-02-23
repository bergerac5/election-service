package com.online.voting.election.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "position_candidates", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "position_id", "candidate_id" })
})
public class PositionCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId; // from Candidate Service

    private LocalDateTime assignedAt;

    public PositionCandidate() {
    }

    public PositionCandidate(UUID id, Position position, UUID candidateId, LocalDateTime assignedAt) {
        this.id = id;
        this.position = position;
        this.candidateId = candidateId;
        this.assignedAt = assignedAt;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

}

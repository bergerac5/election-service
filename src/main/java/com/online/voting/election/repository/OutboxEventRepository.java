package com.online.voting.election.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online.voting.election.models.OutboxEvent;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByPublishedFalse();
}

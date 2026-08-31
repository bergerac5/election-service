package com.online.voting.election.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.voting.election.models.OutboxEvent;
import com.online.voting.election.repository.OutboxEventRepository;
import com.online.voting.events.election.ElectionClosedEvent;

@Service
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final StreamBridge streamBridge;

    public OutboxPublisher(
            OutboxEventRepository outboxRepository,
            ObjectMapper objectMapper,
            StreamBridge streamBridge) {

        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
        this.streamBridge = streamBridge;
    }

    @Scheduled(fixedRate = 5000)
    public void publishEvents() {

        List<OutboxEvent> events = outboxRepository
                .findByPublishedFalseOrderByCreatedAtAsc();

        for (OutboxEvent outbox : events) {
            publishSingleEvent(outbox);
        }
    }

    // Each event gets its own transaction so one failure can't
    // roll back or block any other event in the batch.
    @Transactional
    public void publishSingleEvent(OutboxEvent outbox) {

        try {

            if (outbox.getEventType().equals("ElectionClosedEvent")) {

                ElectionClosedEvent event = deserialize(outbox);

                streamBridge.send(
                        "electionClosed-out-0",
                        event);

                outbox.setPublished(true);
                outboxRepository.save(outbox);

            } else {

                log.warn("No handler for outbox event type '{}', id={}. Skipping.",
                        outbox.getEventType(), outbox.getId());
            }

        } catch (Exception ex) {

            log.error("Failed to publish outbox event id={}, type={}: {}",
                    outbox.getId(), outbox.getEventType(), ex.getMessage(), ex);
        }
    }

    private ElectionClosedEvent deserialize(
            OutboxEvent outbox) {

        try {

            return objectMapper.readValue(
                    outbox.getPayload(),
                    ElectionClosedEvent.class);

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }
}
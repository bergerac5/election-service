package com.online.voting.election.service;

import java.util.List;

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
    @Transactional
    public void publishEvents() {

        List<OutboxEvent> events = outboxRepository.findByPublishedFalse();

        for (OutboxEvent outbox : events) {

            if (outbox.getEventType()
                    .equals("ElectionClosedEvent")) {

                ElectionClosedEvent event = deserialize(outbox);

                streamBridge.send(
                        "electionClosed-out-0",
                        event);

                outbox.setPublished(true);

                outboxRepository.save(outbox);
            }
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
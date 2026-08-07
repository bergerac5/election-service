package com.online.voting.election.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.voting.election.models.Election;
import com.online.voting.election.models.ElectionStatus;
import com.online.voting.election.models.OutboxEvent;
import com.online.voting.election.repository.ElectionRepository;
import com.online.voting.election.repository.OutboxEventRepository;
import com.online.voting.events.election.ElectionClosedEvent;

import jakarta.transaction.Transactional;

@Configuration
public class SchedulingConfig {

    @Component
    public class ElectionStatusScheduler {

        private final ElectionRepository electionRepository;
        private final OutboxEventRepository outboxRepository;
        private final ObjectMapper objectMapper;

        public ElectionStatusScheduler(
                ElectionRepository electionRepository,
                OutboxEventRepository outboxRepository,
                ObjectMapper objectMapper) {

            this.electionRepository = electionRepository;
            this.outboxRepository = outboxRepository;
            this.objectMapper = objectMapper;
        }

        @Scheduled(fixedRate = 60000)
        @Transactional
        public void updateElectionStatus() {

            LocalDateTime now = LocalDateTime.now();

            List<Election> elections = electionRepository.findAll();

            for (Election e : elections) {

                ElectionStatus newStatus;

                if (now.isBefore(e.getStartDate())) {
                    newStatus = ElectionStatus.OPEN;
                } else if (now.isAfter(e.getEndDate())) {
                    newStatus = ElectionStatus.CLOSED;
                } else {
                    newStatus = ElectionStatus.INPROGRESS;
                }

                e.setStatus(newStatus);

                if (newStatus == ElectionStatus.CLOSED
                        && !e.isClosureEventSent()) {

                    saveElectionClosedOutbox(e);

                    e.setClosureEventSent(true);
                }
            }

            electionRepository.saveAll(elections);
        }

        private void saveElectionClosedOutbox(Election election) {

            try {

                ElectionClosedEvent event = new ElectionClosedEvent(election.getElectionId(),
                        LocalDateTime.now());

                // Convert event to JSON string
                String payload = objectMapper.writeValueAsString(event);

                OutboxEvent outbox = new OutboxEvent();

                outbox.setAggregateType("Election");
                outbox.setAggregateId(election.getElectionId());
                outbox.setEventType("ElectionClosedEvent");
                outbox.setPayload(payload);

                outboxRepository.save(outbox);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

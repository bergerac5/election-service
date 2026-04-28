package com.online.voting.election.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.online.voting.election.models.Election;
import com.online.voting.election.models.ElectionStatus;
import com.online.voting.election.repository.ElectionRepository;

@Configuration
public class SchedulingConfig {

    @Component
    public class ElectionStatusScheduler {

        private final ElectionRepository electionRepository;

        public ElectionStatusScheduler(ElectionRepository electionRepository) {
            this.electionRepository = electionRepository;
        }

        @Scheduled(fixedRate = 60000) // every 1 minute
        public void updateElectionStatus() {

            LocalDateTime now = LocalDateTime.now();

            List<Election> elections = electionRepository.findAll();

            for (Election e : elections) {

                if (now.isBefore(e.getStartDate())) {
                    e.setStatus(ElectionStatus.OPEN);
                }

                if (now.isAfter(e.getEndDate())) {
                    e.setStatus(ElectionStatus.CLOSED);
                }

                if (!now.isBefore(e.getStartDate()) && !now.isAfter(e.getEndDate())) {
                    e.setStatus(ElectionStatus.INPROGRESS);
                }
            }

            electionRepository.saveAll(elections);
        }
    }

}

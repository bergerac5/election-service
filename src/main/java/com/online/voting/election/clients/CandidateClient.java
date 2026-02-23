package com.online.voting.election.clients;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.online.voting.election.config.FeignConfig;

@FeignClient(name = "candidate-service", url = "http://localhost:8083", configuration = FeignConfig.class)
public interface CandidateClient {

    @GetMapping("/candidates/{candidateId}")
    void verifyCandidateExists(@PathVariable UUID candidateId);
}

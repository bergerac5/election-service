package com.online.voting.election.clients;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online.voting.election.dtos.ApiResponse;
import com.online.voting.election.dtos.CandidateResponse;

@Component
public class CandidateClientFallback implements CandidateClient {

    @Override
    public ApiResponse<CandidateResponse> verifyCandidateExists(UUID candidateId) {
        return new ApiResponse<>(false, "Service unavailable", null);
    }

    @Override
    public ApiResponse<List<CandidateResponse>> getCandidatesByIds(List<UUID> ids) {
        return new ApiResponse<>(false, "Service unavailable", Collections.emptyList());
    }
}

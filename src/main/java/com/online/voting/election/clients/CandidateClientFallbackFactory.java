package com.online.voting.election.clients;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.online.voting.election.dtos.ApiResponse;
import com.online.voting.election.dtos.CandidateResponse;

@Component
public class CandidateClientFallbackFactory implements FallbackFactory<CandidateClient> {

    @Override
    public CandidateClient create(Throwable cause) {
        return new CandidateClient() {
            @Override
            public ApiResponse<CandidateResponse> verifyCandidateExists(UUID candidateId) {
                return new ApiResponse<>(false, "Service unavailable", null);
            }

            @Override
            public ApiResponse<List<CandidateResponse>> getCandidatesByIds(List<UUID> ids) {
                return new ApiResponse<>(false, "Service unavailable", Collections.emptyList());
            }
        };
    }

    // private RuntimeException translate(Throwable cause) {

    // if (cause instanceof CandidateNotFoundException
    // || cause instanceof UnauthorizedException
    // || cause instanceof ForbiddenException) {
    // log.debug("Passing through decoded exception: {}",
    // cause.getClass().getSimpleName());
    // {
    // if (cause instanceof RuntimeException re)
    // return re;
    // }
    // }
    // // Everything else (connection refused, timeout, 500, 503, decode failure) =
    // // real unavailability
    // log.warn("Candidate service unavailable, triggering fallback", cause);
    // return new ServiceUnavailableException(ServiceNames.CANDIDATE, "Candidate
    // service is currently unavailable");
    // }

}

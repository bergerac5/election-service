package com.online.voting.election.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.voting.election.dtos.PositionCandidateDetailsResponse;
import com.online.voting.election.service.PositionCandidateService;

@RestController
@RequestMapping("position-candidates")

public class PositionCandidateController {

    private final PositionCandidateService positionCandidateService;

    public PositionCandidateController(PositionCandidateService positionCandidateService) {
        this.positionCandidateService = positionCandidateService;
    }

    // ================= GET CANDIDATES BY POSITION =================
    @GetMapping("/position/{positionId}")
    public ResponseEntity<List<PositionCandidateDetailsResponse>> getCandidatesByPosition(
            @PathVariable UUID positionId) {

        List<PositionCandidateDetailsResponse> response = positionCandidateService.getCandidatesByPosition(positionId);

        return ResponseEntity.ok(response);
    }

    // ================= GET CANDIDATES BY ELECTION =================
    @GetMapping("/election/{electionId}")
    public ResponseEntity<List<PositionCandidateDetailsResponse>> getCandidatesByElection(
            @PathVariable UUID electionId) {

        List<PositionCandidateDetailsResponse> response = positionCandidateService.getCandidatesByElection(electionId);

        return ResponseEntity.ok(response);
    }
}

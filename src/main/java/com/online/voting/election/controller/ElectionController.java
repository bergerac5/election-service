package com.online.voting.election.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.voting.election.dtos.CreateElectionRequest;
import com.online.voting.election.dtos.ElectionResponse;
import com.online.voting.election.dtos.UpdateElectionRequest;
import com.online.voting.election.dtos.UpdateElectionStatusRequest;
import com.online.voting.election.models.Election;
import com.online.voting.election.service.ElectionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/elections")
public class ElectionController {

    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    /* ================= CREATE ================= */

    @PostMapping
    public ResponseEntity<ElectionResponse> createElection(
            @Valid @RequestBody CreateElectionRequest request) {

        try {
            ElectionResponse response = electionService.createElection(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ElectionResponse(ex.getMessage()));
        }
    }

    /* ================= UPDATE ================= */

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{electionId}")
    public ResponseEntity<ElectionResponse> updateElection(
            @PathVariable UUID electionId,
            @Valid @RequestBody UpdateElectionRequest request) {

        request.setElectionId(electionId);

        ElectionResponse response = electionService.updateElection(request);

        return resolveResponse(response);
    }

    /* ================= DELETE ================= */

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{electionId}")
    public ResponseEntity<ElectionResponse> deleteElection(
            @PathVariable UUID electionId) {

        ElectionResponse response = electionService.deleteElection(electionId);

        return resolveResponse(response);
    }

    /* ================= GET ================= */

    @GetMapping("/title/{title}")
    public ResponseEntity<?> getElectionByTitle(@PathVariable String title) {
        try {
            Election election = electionService.getElectionByTitle(title);
            return ResponseEntity.ok(election);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ElectionResponse(ex.getMessage()));
        }
    }

    /* ================= STATUS UPDATE ================= */

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{electionId}/status")
    public ResponseEntity<ElectionResponse> updateElectionStatus(
            @PathVariable UUID electionId,
            @Valid @RequestBody UpdateElectionStatusRequest request) {

        request.setElectionId(electionId);

        ElectionResponse response = electionService.updateElectionStatus(request);

        return resolveResponse(response);
    }

    /* ================= RESPONSE RESOLVER ================= */

    private ResponseEntity<ElectionResponse> resolveResponse(ElectionResponse response) {

        String message = response.getMessage().toLowerCase();

        if (message.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (message.contains("already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (message.contains("cannot") || message.contains("failed")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }
}

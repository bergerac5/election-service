package com.online.voting.election.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online.voting.election.dtos.ApiResponse;
import com.online.voting.election.dtos.CreateElectionRequest;
import com.online.voting.election.dtos.ElectionResponse;
import com.online.voting.election.dtos.MessageResponse;
import com.online.voting.election.dtos.UpdateElectionRequest;
import com.online.voting.election.dtos.UpdateElectionStatusRequest;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createPosition")
    public ResponseEntity<MessageResponse> createElection(
            @Valid @RequestBody CreateElectionRequest request) {

        try {
            MessageResponse response = electionService.createElection(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(ex.getMessage()));
        }
    }

    /* ================= UPDATE ================= */

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateElection/{electionId}")
    public ResponseEntity<MessageResponse> updateElection(
            @PathVariable UUID electionId,
            @Valid @RequestBody UpdateElectionRequest request) {

        MessageResponse response = electionService.updateElection(electionId, request);

        return resolveResponse(response);
    }

    /* ================= DELETE ================= */

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteElection/{electionId}")
    public ResponseEntity<MessageResponse> deleteElection(
            @PathVariable UUID electionId) {

        MessageResponse response = electionService.deleteElection(electionId);

        return resolveResponse(response);
    }

    /* ================= SEARCH BY NAME ================= */

    @GetMapping("/title/{title}")
    public ResponseEntity<ElectionResponse> getElectionByTitle(
            @PathVariable String title) {

        ElectionResponse response = electionService.getElectionByTitle(title);
        return ResponseEntity.ok(response);
    }

    /* ================= STATUS UPDATE ================= */

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{electionId}/status")
    public ResponseEntity<MessageResponse> updateElectionStatus(
            @PathVariable UUID electionId,
            @Valid @RequestBody UpdateElectionStatusRequest request) {

        request.setElectionId(electionId);

        MessageResponse response = electionService.updateElectionStatus(request);

        return resolveResponse(response);
    }

    // Get all elections
    @GetMapping("/allElections")
    public ResponseEntity<ApiResponse<List<ElectionResponse>>> getAllElections() {
        return ResponseEntity.ok(electionService.getAllElections());
    }

    // get election by id
    @GetMapping("/{electionId}")
    public ResponseEntity<ElectionResponse> getElectionById(@PathVariable UUID electionId) {
        ElectionResponse response = electionService.getElectionById(electionId);
        return ResponseEntity.ok(response);
    }

    // block endpoint to get multiple elections by their IDs
    @PostMapping("/bulk")
    public ApiResponse<List<ElectionResponse>> getElectionsByIds(@RequestBody List<UUID> ids) {
        List<ElectionResponse> elections = electionService.getElectionsByIds(ids);
        return new ApiResponse<>("Elections fetched successfully", elections);
    }

    /* ================= RESPONSE RESOLVER ================= */

    private ResponseEntity<MessageResponse> resolveResponse(MessageResponse response) {

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

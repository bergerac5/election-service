package com.online.voting.election.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.online.voting.election.dtos.ApiResponse;
import com.online.voting.election.dtos.AssignCandidateResponse;
import com.online.voting.election.dtos.PageResponse;
import com.online.voting.election.dtos.PositionRequest;
import com.online.voting.election.dtos.PositionResponse;
import com.online.voting.election.models.PositionStatus;
import com.online.voting.election.service.PositionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/positions")
public class PositionController {

        private final PositionService positionService;

        public PositionController(PositionService positionService) {
                this.positionService = positionService;
        }

        /* ================= CREATE ================= */
        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping
        public ResponseEntity<ApiResponse<PositionResponse>> createPosition(
                        @Valid @RequestBody PositionRequest request) {

                PositionResponse response = positionService.createPosition(request);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(new ApiResponse<>("Position created successfully", response));
        }

        /* ================= UPDATE ================= */
        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/{positionId}")
        public ResponseEntity<ApiResponse<PositionResponse>> updatePosition(
                        @PathVariable UUID positionId,
                        @Valid @RequestBody PositionRequest request) {

                PositionResponse response = positionService.updatePosition(positionId, request);
                return ResponseEntity.ok(new ApiResponse<>("Position updated successfully", response));
        }

        /* ================= DELETE ================= */
        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/{positionId}")
        public ResponseEntity<Void> deletePosition(@PathVariable UUID positionId) {

                positionService.deletePosition(positionId);
                return ResponseEntity.noContent().build();
        }

        /* ================= GET ALL POSITIONS ================= */

        @GetMapping
        public ResponseEntity<ApiResponse<PageResponse<PositionResponse>>> getAllPositions(
                        @RequestParam(required = false) UUID electionId,
                        @RequestParam(required = false) PositionStatus status,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "displayOrder") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDir) {

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                sortDir.equalsIgnoreCase("asc")
                                                ? Sort.by(sortBy).ascending()
                                                : Sort.by(sortBy).descending());

                Page<PositionResponse> positionsPage = positionService.getAllPositions(electionId, status, pageable);

                PageResponse<PositionResponse> response = PageResponse.from(positionsPage);

                return ResponseEntity.ok(
                                new ApiResponse<>("Positions retrieved successfully", response));
        }

        /* ================= GET POSITIONS BY ELECTION ================= */
        @GetMapping("/election/{electionId}")
        public ResponseEntity<ApiResponse<PageResponse<PositionResponse>>> getPositionsByElection(
                        @PathVariable UUID electionId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "displayOrder") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDir) {

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                sortDir.equalsIgnoreCase("asc")
                                                ? Sort.by(sortBy).ascending()
                                                : Sort.by(sortBy).descending());

                Page<PositionResponse> positionsPage = positionService.getPositionsByElection(electionId, pageable);

                PageResponse<PositionResponse> response = PageResponse.from(positionsPage);

                return ResponseEntity.ok(
                                new ApiResponse<>("Positions retrieved successfully", response));
        }

        /* ================= ASSIGN POSITION TO CANDIDATE ================= */
        @PreAuthorize("hasRole('ADMIN')")
        // post because is insert into conjuction table positioncandidate
        @PostMapping("/{electionId}/positions/{positionId}/assign-candidate/{candidateId}")
        public ResponseEntity<ApiResponse<AssignCandidateResponse>> assignCandidate(
                        @PathVariable UUID electionId,
                        @PathVariable UUID positionId,
                        @PathVariable UUID candidateId) {

                AssignCandidateResponse response = positionService.assignPositionToCandidate(
                                electionId, positionId, candidateId);

                return ResponseEntity.ok(
                                new ApiResponse<>(response.getMessage(), response));
        }

}

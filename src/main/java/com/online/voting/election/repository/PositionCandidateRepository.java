package com.online.voting.election.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.online.voting.election.models.PositionCandidate;

import feign.Param;

@Repository
public interface PositionCandidateRepository extends JpaRepository<PositionCandidate, UUID> {

    @Query("SELECT CASE WHEN COUNT(pc) > 0 THEN true ELSE false END FROM PositionCandidate pc WHERE pc.position.id = :positionId AND pc.candidateId = :candidateId")
    boolean existsByPositionIdAndCandidateId(@Param("positionId") UUID positionId,
            @Param("candidateId") UUID candidateId);

    @Query("SELECT pc FROM PositionCandidate pc WHERE pc.position.id = :positionId")
    List<PositionCandidate> findByPositionId(@Param("positionId") UUID positionId);
}

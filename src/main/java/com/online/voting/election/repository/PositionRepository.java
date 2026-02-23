package com.online.voting.election.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.online.voting.election.models.Election;
import com.online.voting.election.models.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> , JpaSpecificationExecutor<Position> {

    boolean existsByName(String name);

    Optional<Position> findByPositionId(UUID positionId);

    @Query("SELECT MAX(p.displayOrder) FROM Position p WHERE p.election.electionId = :electionId")
    Integer findMaxDisplayOrderByElection(@Param("electionId") UUID electionId);

    boolean existsByElectionAndName(Election election, String name);

    Optional<Position> findByElection(Election election);

    Page<Position> findByElection(Election election, Pageable pageable);

}

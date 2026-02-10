package com.online.voting.election.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.online.voting.election.models.Election;

@Repository
public interface ElectionRepository extends JpaRepository<Election, UUID> {

    Optional<Election> findByElectionId(UUID electionId);

    Boolean existsByTitle(String title);

    Optional<Election> findByTitle(String title);

}

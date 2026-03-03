package com.online.voting.election.dtos;

import java.util.UUID;

public class CandidateResponse {
    private UUID candidateId;
    private UUID userId;
    private String username;
    private String firstName;
    private String lastName;
    private String party;
    private String manifesto;
    private String nationalId;

    public CandidateResponse() {
    }

    public CandidateResponse(UUID candidateId, UUID userId, String username, String firstName, String lastName,
            String party, String manifesto, String nationalId) {
        this.candidateId = candidateId;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.party = party;
        this.manifesto = manifesto;
        this.nationalId = nationalId;
    }

    public UUID getCandidateId() {
        return this.candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getParty() {
        return this.party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getManifesto() {
        return this.manifesto;
    }

    public void setManifesto(String manifesto) {
        this.manifesto = manifesto;
    }

    public String getNationalId() {
        return this.nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

}

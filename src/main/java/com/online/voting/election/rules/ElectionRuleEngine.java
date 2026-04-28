package com.online.voting.election.rules;

import java.time.LocalDateTime;

import com.online.voting.election.dtos.ElectionResponse;
import com.online.voting.election.handler.VotingNotAllowedException;
import com.online.voting.election.models.ElectionStatus;

public class ElectionRuleEngine {

    public static void validateCanVote(ElectionResponse election) {

        if (election == null) {
            throw new VotingNotAllowedException("Election not found");
        }

        LocalDateTime now = LocalDateTime.now();

        boolean statusValid = election.getStatus() == ElectionStatus.OPEN ||
                election.getStatus() == ElectionStatus.INPROGRESS;

        boolean timeValid = !now.isBefore(election.getStartDate()) &&
                !now.isAfter(election.getEndDate());

        if (!statusValid) {
            throw new VotingNotAllowedException(
                    "Election is not open for voting. Current status: " + election.getStatus());
        }

        if (!timeValid) {
            throw new VotingNotAllowedException(
                    "Election is outside voting period");
        }

    }

    public static boolean isVotingAllowed(ElectionResponse election) {
        try {
            validateCanVote(election);
            return true;
        } catch (VotingNotAllowedException ex) {
            return false;
        }
    }
}

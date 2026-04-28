package com.online.voting.election.handler;

public class VotingNotAllowedException extends RuntimeException {

    public VotingNotAllowedException(String message) {
        super(message);
    }

}

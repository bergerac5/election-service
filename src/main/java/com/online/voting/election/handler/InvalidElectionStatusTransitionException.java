package com.online.voting.election.handler;

public class InvalidElectionStatusTransitionException extends RuntimeException {
    public InvalidElectionStatusTransitionException(String message) {
        super(message);
    }
}

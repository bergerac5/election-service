package com.online.voting.election.dtos;

public class ElectionResponse {
    private String message;

    public ElectionResponse() {
    }

    public ElectionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

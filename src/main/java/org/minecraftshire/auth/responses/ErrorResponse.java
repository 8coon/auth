package org.minecraftshire.auth.responses;


public class ErrorResponse {

    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return this.error;
    }
}

package org.minecraftshire.auth.responses;


public class ErrorWithCauseResponse extends ErrorResponse {

    private String cause;

    public ErrorWithCauseResponse(String error, String cause) {
        super(error);
        this.cause = cause;
    }

    public String getCause() {
        return this.cause;
    }
}

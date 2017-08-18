package org.minecraftshire.auth.responses;


public class StatusResponse {

    private String status;
    private int retryAfter;


    public StatusResponse(
            String status,
            int retryAfter
    ) {
        this.status = status;
        this.retryAfter = retryAfter;
    }


    public String getStatus() {
        return status;
    }

    public int getRetryAfter() {
        return retryAfter;
    }

}

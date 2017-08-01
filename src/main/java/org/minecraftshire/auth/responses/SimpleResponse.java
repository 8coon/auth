package org.minecraftshire.auth.responses;


public class SimpleResponse {

    private String message;
    private int status;


    public SimpleResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }


    public SimpleResponse(int status) {
        this.message = null;
        this.status = status;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
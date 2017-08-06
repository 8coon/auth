package org.minecraftshire.auth.exceptions;


public class ExceptionWithCause extends Exception {

    private String cause;

    public ExceptionWithCause(String cause) {
        this.cause = cause;
    }

    public String getCausedBy() {
        return this.cause;
    }

}

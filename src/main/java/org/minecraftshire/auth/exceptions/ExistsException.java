package org.minecraftshire.auth.exceptions;


public class ExistsException extends Throwable {

    private String cause;


    public ExistsException(String cause) {
        this.cause = cause;
    }

    public String getExistsCause() {
        return this.cause;
    }

}

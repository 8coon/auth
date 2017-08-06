package org.minecraftshire.auth.exceptions;


public class WrongCredentialsException extends ExceptionWithCause {

    public WrongCredentialsException(String cause) {
        super(cause);
    }

}

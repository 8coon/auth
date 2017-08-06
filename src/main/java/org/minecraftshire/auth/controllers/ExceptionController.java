package org.minecraftshire.auth.controllers;


import org.minecraftshire.auth.aspects.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorized(UnauthorizedException e) {
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

}

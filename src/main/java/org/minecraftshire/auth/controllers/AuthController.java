package org.minecraftshire.auth.controllers;


import org.minecraftshire.auth.aspects.AuthRequired;
import org.minecraftshire.auth.aspects.UserAgent;
import org.minecraftshire.auth.data.*;
import org.minecraftshire.auth.exceptions.WrongCredentialsException;
import org.minecraftshire.auth.repositories.TokenRepository;
import org.minecraftshire.auth.repositories.UserRepository;
import org.minecraftshire.auth.responses.ErrorWithCauseResponse;
import org.minecraftshire.auth.utils.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "*"})
@RequestMapping("/auth")
public class AuthController {

    private UserRepository users;
    private TokenRepository tokens;


    @Autowired
    public AuthController(UserRepository users, TokenRepository tokens) {
        this.users = users;
        this.tokens = tokens;
    }


    @PostMapping("/login")
    public ResponseEntity login(
            @RequestBody CredentialsData credentials,
            UserAgent userAgent,
            @RequestHeader("X-Real-IP") String ip
    ) {
        try {
            credentials.setAppToken(userAgent.toString());

            return new ResponseEntity<>(
                    new AuthTokenData(this.users.login(credentials, ip)),
                    HttpStatus.OK
            );
        } catch (WrongCredentialsException e) {
            return new ResponseEntity<>(
                    new ErrorWithCauseResponse(Errors.WRONG_CREDENTIALS, e.getCausedBy()),
                    HttpStatus.FORBIDDEN
            );
        }
    }


    @AuthRequired
    @PostMapping("/logout")
    public ResponseEntity logout(
            @RequestBody AuthTokenData authTokenData,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        this.tokens.dropToken(authTokenData.getAuthToken());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @AuthRequired
    @PostMapping("/logout_everywhere")
    public ResponseEntity logoutEverywhere(
            @RequestBody AuthTokenData authTokenData,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        this.tokens.closeAllSessions(sessionData.getUsername());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @AuthRequired
    @PostMapping("/sessions")
    public ResponseEntity sessions(
            @RequestBody AuthTokenData authTokenData,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        return new ResponseEntity<>(
                this.tokens.listAllSessions(sessionData.getUsername()),
                HttpStatus.OK
        );
    }


    @AuthRequired
    @PostMapping("/history")
    public ResponseEntity history(
            @RequestBody AuthTokenData authTokenData,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        return new ResponseEntity<>(
                this.tokens.getHistory(sessionData.getUsername()),
                HttpStatus.OK
        );
    }


    @PostMapping("/restore_access")
    public ResponseEntity restoreAccess(
            @RequestBody EmailData emailData
    ) {
        try {
            this.users.resetPassword(emailData.getEmail());
        } catch (WrongCredentialsException e) {
            return new ResponseEntity<>(
                    new ErrorWithCauseResponse(Errors.FAILED, e.getCausedBy()),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/restore_password")
    public ResponseEntity restorePassword(
            @RequestBody RestorePasswordData data
    ) {
        try {
            this.users.setPassword(data.getCode(), data.getPassword());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (WrongCredentialsException e) {
            return new ResponseEntity<>(
                    new ErrorWithCauseResponse(Errors.FAILED, e.getCausedBy()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}

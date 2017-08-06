package org.minecraftshire.auth.controllers;


import org.minecraftshire.auth.aspects.AuthRequired;
import org.minecraftshire.auth.data.AuthTokenData;
import org.minecraftshire.auth.data.CredentialsData;
import org.minecraftshire.auth.data.SessionData;
import org.minecraftshire.auth.exceptions.WrongCredentialsException;
import org.minecraftshire.auth.repositories.UserRepository;
import org.minecraftshire.auth.responses.ErrorWithCauseResponse;
import org.minecraftshire.auth.utils.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository users;

    @Autowired
    public AuthController(UserRepository users) {
        this.users = users;
    }


    @PostMapping("/login")
    public ResponseEntity login(
            @RequestBody CredentialsData credentials,
            @RequestHeader("User-Agent") String userAgent
    ) {
        try {
            credentials.setAppToken(userAgent);

            return new ResponseEntity<>(
                    new AuthTokenData(this.users.login(credentials)),
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
            @RequestHeader("User-Agent") String userAgent,
            SessionData sessionData
    ) {
        this.users.dropToken(authTokenData.getAuthToken());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @AuthRequired
    @PostMapping("/test")
    public String test(
            @RequestBody AuthTokenData authTokenData,
            @RequestHeader("User-Agent") String userAgent,
            SessionData sessionData
    ) {
        return sessionData.getUsername() + " " + sessionData.getGroup();
    }

}

package org.minecraftshire.auth.controllers;

import org.minecraftshire.auth.aspects.AuthRequired;
import org.minecraftshire.auth.aspects.UserAgent;
import org.minecraftshire.auth.data.*;
import org.minecraftshire.auth.exceptions.ExistsException;
import org.minecraftshire.auth.exceptions.WrongCredentialsException;
import org.minecraftshire.auth.repositories.ConfirmationRepository;
import org.minecraftshire.auth.repositories.UserRepository;
import org.minecraftshire.auth.responses.ErrorWithCauseResponse;
import org.minecraftshire.auth.utils.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "*"})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository users;
    @Autowired
    private ConfirmationRepository confirmations;


    @PostMapping("/create")
    public ResponseEntity create(
            @RequestBody UserData user
    ) {
        try {
            this.users.create(user.getUsername(), user.getEmail(), user.getPassword());

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (ExistsException e) {
            return new ResponseEntity<>(
                    new ErrorWithCauseResponse(Errors.EXISTS, e.getCausedBy()),
                    HttpStatus.CONFLICT
            );
        }
    }


    @PostMapping("/email_verify")
    public ResponseEntity emailVerify(
            @RequestBody ConfirmationData confirmation
    ) {
        if (this.confirmations.confirm(confirmation.getCode())) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    @AuthRequired
    @PostMapping("/change_password")
    public ResponseEntity changePassword(
            @RequestBody ChangePasswordData passwordData,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        try {
            this.users.changePassword(
                    sessionData.getUsername(),
                    passwordData.getOldPassword(),
                    passwordData.getNewPassword()
            );

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (WrongCredentialsException e) {
            return new ResponseEntity<>(
                    new ErrorWithCauseResponse(Errors.WRONG_CREDENTIALS, e.getCausedBy()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    @AuthRequired
    @PostMapping("/status")
    public ResponseEntity status(
            @RequestBody AuthTokenWithLastModifiedData data,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        UserStatusData status;

        try {
            status = users.getStatus(sessionData.getUsername(), data.getLastModified());
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (status == null) {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }

        return new ResponseEntity<>(status, HttpStatus.OK);
    }


}

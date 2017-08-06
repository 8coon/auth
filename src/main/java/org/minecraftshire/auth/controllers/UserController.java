package org.minecraftshire.auth.controllers;

import org.minecraftshire.auth.data.UserData;
import org.minecraftshire.auth.exceptions.ExistsException;
import org.minecraftshire.auth.repositories.ConfirmationRepository;
import org.minecraftshire.auth.repositories.UserRepository;
import org.minecraftshire.auth.responses.ErrorWithCauseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository users;
    private ConfirmationRepository confirmations;


    @Autowired
    public UserController(UserRepository users, ConfirmationRepository confirmations) {
        this.users = users;
        this.confirmations = confirmations;
    }


    @PostMapping("/create")
    public ResponseEntity create(
            @RequestBody UserData user
    ) {
        try {
            this.users.create(user.getUsername(), user.getEmail(), user.getPassword());

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (ExistsException e) {
            return new ResponseEntity<ErrorWithCauseResponse>(
                    new ErrorWithCauseResponse("exists", e.getExistsCause()),
                    HttpStatus.CONFLICT
            );
        }
    }


    @PostMapping("/email_verify")
    public ResponseEntity emailVerify(
            @RequestParam("code") long code
    ) {
        if (this.confirmations.confirmSignUp(code)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


}

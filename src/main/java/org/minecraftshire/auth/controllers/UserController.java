package org.minecraftshire.auth.controllers;

import org.minecraftshire.auth.data.UserData;
import org.minecraftshire.auth.repositories.ConfirmationRepository;
import org.minecraftshire.auth.repositories.UserRepository;
import org.minecraftshire.auth.utils.SimpleResponse;
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
    public ResponseEntity<SimpleResponse> create(
            @RequestBody UserData user
    ) {
        return new ResponseEntity<>(
                new SimpleResponse(
                        this.users.create(user.getUsername(), user.getEmail(), user.getPassword())
                ),
                HttpStatus.OK
        );
    }


    @GetMapping("/email_verify")
    public ResponseEntity<String> emailVerify(
            @RequestParam("code") long code
    ) {
        if (this.confirmations.confirmSignUp(code)) {
            ResponseEntity<String> result = new ResponseEntity<>(HttpStatus.TEMPORARY_REDIRECT);
            result.getHeaders().set("Location", "http://minecraftshire.ru/");

            return result;
        }

        return new ResponseEntity<>(
                "Ошибка: неверный код.",
                HttpStatus.OK
        );
    }


}

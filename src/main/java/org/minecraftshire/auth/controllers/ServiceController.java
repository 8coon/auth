package org.minecraftshire.auth.controllers;


import org.minecraftshire.auth.data.SecretTokenData;
import org.minecraftshire.auth.utils.ErrorCodes;
import org.minecraftshire.auth.utils.SimpleResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/service")
public class ServiceController {

    private JdbcTemplate jdbc;


    @Autowired
    public ServiceController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @PostMapping("/drop")
    public ResponseEntity<SimpleResponse> create(
            @RequestBody SecretTokenData token
    ) {
        if (token.isNot()) {
            return new ResponseEntity<>(new SimpleResponse(ErrorCodes.ACCESS_DENIED), HttpStatus.OK);
        }

        this.jdbc.update("DELETE FROM Users");
        this.jdbc.update("DELETE FROM Confirmations");

        return new ResponseEntity<>(new SimpleResponse("success", ErrorCodes.OK), HttpStatus.OK);
    }


    @PostMapping("/stop")
    public String stop(
            @RequestBody SecretTokenData token
    ) {
        if (token.isNot()) {
            return "{}";
        }

        System.exit(0);
        return "{}";
    }

}

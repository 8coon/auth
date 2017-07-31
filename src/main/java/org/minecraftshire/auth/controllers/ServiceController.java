package org.minecraftshire.auth.controllers;


import org.minecraftshire.auth.data.UserData;
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

import java.sql.SQLException;


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
            @RequestBody UserData user
    ) {
        try {
            this.jdbc.update("DELETE FROM Users");
            this.jdbc.update("DELETE FROM Confirmations");
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new SimpleResponse("fail", ErrorCodes.INTERNAL_ERROR), HttpStatus.OK);
        }

        return new ResponseEntity<>(new SimpleResponse("success", ErrorCodes.OK), HttpStatus.OK);
    }

}

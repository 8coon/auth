package org.minecraftshire.auth.controllers;


import org.minecraftshire.auth.MinecraftshireAuthApplication;
import org.minecraftshire.auth.data.SecretTokenData;
import org.minecraftshire.auth.responses.VersionResponse;
import org.minecraftshire.auth.utils.ErrorCodes;
import org.minecraftshire.auth.responses.SimpleResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

        this.jdbc.update("DELETE FROM Confirmations");
        this.jdbc.update("DELETE FROM Users");

        return new ResponseEntity<>(new SimpleResponse("success", ErrorCodes.OK), HttpStatus.OK);
    }


    @PostMapping("/stop")
    public String stop(
            @RequestBody SecretTokenData token
    ) {
        if (token.isNot()) {
            System.err.println("App was given wrong security token");

            return "{}";
        }

        System.err.println("Stopping application...");
        MinecraftshireAuthApplication.stop();

        return "{}";
    }


    @PostMapping("/version")
    public ResponseEntity<VersionResponse> version() {
        return new ResponseEntity<>(
                new VersionResponse(
                        MinecraftshireAuthApplication.getName(),
                        MinecraftshireAuthApplication.getVersion(),
                        MinecraftshireAuthApplication.getDescription()
                ),
                HttpStatus.OK
        );
    }

}

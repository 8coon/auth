package org.minecraftshire.auth.controllers;


import org.minecraftshire.auth.MinecraftshireAuthApplication;
import org.minecraftshire.auth.data.SecretTokenData;
import org.minecraftshire.auth.responses.VersionResponse;
import org.minecraftshire.auth.utils.ErrorCodes;
import org.minecraftshire.auth.responses.SimpleResponse;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/service")
public class ServiceController {

    private Logger log = Logger.getLogger();
    private JdbcTemplate jdbc;
    private Environment env;


    @Autowired
    public ServiceController(JdbcTemplate jdbc, Environment env) {
        this.jdbc = jdbc;
        this.env = env;
    }


    @PostMapping("/drop")
    public ResponseEntity<SimpleResponse> create(
            @RequestBody SecretTokenData token
    ) {
        if (token.isNot()) {
            log.info("App was given wrong security token");
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
            log.info("App was given wrong security token");
            return "{}";
        }

        MinecraftshireAuthApplication.stop();
        return "{}";
    }


    @PostMapping("/version")
    public ResponseEntity<VersionResponse> version() {
        return new ResponseEntity<>(
                new VersionResponse(
                        env.getProperty("minecraftshire.name"),
                        env.getProperty("minecraftshire.version") + "." +
                                MinecraftshireAuthApplication.getBuildNumber(),
                        env.getProperty("minecraftshire.description")
                ),
                HttpStatus.OK
        );
    }

}

package org.minecraftshire.auth.controllers;


import org.minecraftshire.auth.Server;
import org.minecraftshire.auth.data.SecretTokenData;
import org.minecraftshire.auth.responses.StatsResponse;
import org.minecraftshire.auth.responses.VersionResponse;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "*"})
@RequestMapping("/service")
public class ServiceController {

    private JdbcTemplate jdbc;
    private Environment env;


    @Autowired
    public ServiceController(JdbcTemplate jdbc, Environment env) {
        this.jdbc = jdbc;
        this.env = env;
    }


    @PostMapping("/drop")
    public ResponseEntity create(
            @RequestBody SecretTokenData token
    ) {
        if (token.isNot()) {
            Logger.getLogger().info("App was given wrong security token");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        this.jdbc.update("DELETE FROM Tokens");
        this.jdbc.update("DELETE FROM TokenHistory");
        this.jdbc.update("DELETE FROM Notifications");
        this.jdbc.update("DELETE FROM Confirmations");
        this.jdbc.update("DELETE FROM Users");

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/stop")
    public String stop(
            @RequestBody SecretTokenData token
    ) {
        if (token.isNot()) {
            Logger.getLogger().info("App was given wrong security token");
            return "{}";
        }

        Server.stop();
        return "{}";
    }


    @PostMapping("/version")
    public ResponseEntity<VersionResponse> version() {
        return new ResponseEntity<>(
                new VersionResponse(
                        env.getProperty("minecraftshire.name"),
                        env.getProperty("minecraftshire.version") + "." +
                                Server.getBuildNumber(),
                        env.getProperty("minecraftshire.description"),
                        Server.getBuildDate(),
                        Server.getGeoDbVersion()
                ),
                HttpStatus.OK
        );
    }


    @PostMapping("/stats")
    public ResponseEntity stats() {
        return new ResponseEntity<>(
                new StatsResponse(
                    this.jdbc.queryForObject("SELECT count(*) FROM Users", Integer.class),
                    this.jdbc.queryForObject("SELECT count(*) FROM Confirmations", Integer.class),
                    this.jdbc.queryForObject("SELECT count(*) FROM Tokens", Integer.class),
                    this.jdbc.queryForObject("SELECT count(*) FROM TokenHistory", Integer.class),
                    this.jdbc.queryForObject("SELECT count(*) FROM Notifications", Integer.class)
                ),
                HttpStatus.OK
        );
    }

}

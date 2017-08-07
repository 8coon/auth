package org.minecraftshire.auth.repositories;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.minecraftshire.auth.Server;
import org.minecraftshire.auth.aspects.UnauthorizedException;
import org.minecraftshire.auth.data.CredentialsData;
import org.minecraftshire.auth.data.SessionData;
import org.minecraftshire.auth.data.SessionGeoData;
import org.minecraftshire.auth.data.UserData;
import org.minecraftshire.auth.services.GeoLocator;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;


@org.springframework.stereotype.Repository
public class TokenRepository extends Repository {

    private GeoLocator geoLocator;


    @Autowired
    public TokenRepository(GeoLocator geoLocator) {
        this.geoLocator = geoLocator;
    }


    public static String getSignature(String appToken) {
        return Server.getSecretToken() + "." + appToken;
    }


    public String getAuthToken(CredentialsData credentials, UserData user, String ip) {
        Algorithm algorithm;

        try {
            algorithm = Algorithm.HMAC512(TokenRepository.getSignature(credentials.getAppToken()));
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger().severe(e);
            return "";
        }

        String token = JWT.create()
                .withIssuer(Server.getIssuer())
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(ZonedDateTime.now().plusMonths(3).toInstant()))
                .withClaim("username", credentials.getUsername())
                .withClaim("group", user.getGroup())
                .sign(algorithm);

        saveToken(token, credentials.getUsername(), ip);
        return token;
    }


    public SessionData verifyAuthToken(String authToken, String appToken) {
        if (!hasToken(authToken)) {
            throw new UnauthorizedException();
        }

        try {
            Algorithm algorithm = Algorithm.HMAC512(TokenRepository.getSignature(appToken));

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(Server.getIssuer())
                    .build();
            DecodedJWT decoded = verifier.verify(authToken);

            return new SessionData(
                    decoded.getClaim("username").asString(),
                    decoded.getClaim("group").asInt()
            );
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger().severe(e);
        } catch (JWTVerificationException e1) {
            dropToken(authToken);
        }

        throw new UnauthorizedException();
    }


    public void closeAllSessions(String username) {
        jdbc.update(
                "DELETE FROM Tokens WHERE username = ?",
                username
        );
    }


    public List<SessionGeoData> listAllSessions(String username) {
        return jdbc.query(
                "SELECT ip, issued_at, \"location\" FROM Tokens WHERE username = ?",
                new SessionGeoData(),
                username
        );
    }


    public void saveToken(String authToken, String username, String ip) {
        String location = geoLocator.lookupAddress(ip);

        jdbc.update(
                "INSERT INTO Tokens (token, username, ip, \"location\") VALUES (?, ?, ?, ?)" +
                        "ON CONFLICT DO UPDATE SET username = ?, ip = ?, \"location\" = ? WHERE token = ?",
                authToken, username, ip, location, username, ip, location, authToken
        );
    }

    public void dropToken(String authToken) {
        jdbc.update(
                "DELETE FROM Tokens WHERE token = ?",
                authToken
        );
    }

    public boolean hasToken(String authToken) {
        try {
            jdbc.queryForObject(
                    "SELECT token FROM Tokens WHERE token = ? LIMIT 1",
                    String.class,
                    authToken
            );

            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

}

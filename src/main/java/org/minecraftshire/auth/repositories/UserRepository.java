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
import org.minecraftshire.auth.data.UserData;
import org.minecraftshire.auth.exceptions.ExistsException;
import org.minecraftshire.auth.exceptions.ExistsExceptionCause;
import org.minecraftshire.auth.exceptions.WrongCredentialsException;
import org.minecraftshire.auth.exceptions.WrongCredentialsExceptionCause;
import org.minecraftshire.auth.utils.UserGroups;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Random;


@org.springframework.stereotype.Repository
public class UserRepository extends Repository {

    private SecureRandom random = new SecureRandom();
    private ConfirmationRepository confirmationRepository;


    @Autowired
    public UserRepository(ConfirmationRepository confirmationRepository) {
        this.confirmationRepository = confirmationRepository;
    }


    public boolean hasUsername(String username) {
        return this.hasObject("Users", "username", username);
    }

    public boolean hasEmail(String email) {
        return this.hasObject("Users", "email", email);
    }


    @Transactional
    public void create(String username, String email, String password) throws ExistsException {
        if (hasUsername(username)) {
            throw new ExistsException(ExistsExceptionCause.USERNAME);
        }

        if (hasEmail(email)) {
            throw new ExistsException(ExistsExceptionCause.EMAIL);
        }

        int salt = this.generateSalt();
        String salty = UserRepository.makeSalty(password, salt);

        this.jdbc.update(
                "INSERT INTO Users (username, password, email, salt, \"group\", is_confirmed, is_banned) VALUES" +
                        "(?, ?, ?, ?, ?, ?, ?)",
                username, salty, email, salt, UserGroups.STANDARD, false, false
        );

        this.confirmationRepository.requestSignUpConfirmation(username, email);
    }


    public String login(CredentialsData credentials) throws WrongCredentialsException {
        UserData user;

        try {
            user = this.jdbc.queryForObject(
                    "SELECT username, email, password, salt, \"group\", is_confirmed, is_banned FROM Users WHERE username = ? LIMIT 1",
                    new UserData(),
                    credentials.getUsername()
            );
        } catch (DataAccessException e) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.USERNAME_OR_PASSWORD);
        }

        if (!user.isConfirmed()) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.NOT_CONFIRMED);
        }

        if (user.isBanned()) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.BANNED);
        }

        String hash = UserRepository.makeSalty(credentials.getPassword(), user.getSalt());

        if (!user.getPassword().equals(hash)) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.USERNAME_OR_PASSWORD);
        }

        return getAuthToken(credentials, user);
    }


    public void changePassword(String username, String oldPassword, String newPassword) throws WrongCredentialsException {
        UserData user = this.jdbc.queryForObject(
                "SELECT username, email, password, salt, \"group\", is_confirmed, is_banned FROM Users WHERE username = ? LIMIT 1",
                new UserData(),
                username
        );

        if (oldPassword.equals(newPassword)) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.SAME_PASSWORD);
        }

        oldPassword = UserRepository.makeSalty(oldPassword, user.getSalt());
        newPassword = UserRepository.makeSalty(newPassword, user.getSalt());

        if (!oldPassword.equals(user.getPassword())) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.PASSWORD);
        }

        this.jdbc.update(
                "UPDATE Users SET password = ? WHERE username = ?",
                newPassword, username
        );
    }


    public int generateSalt() {
        return this.random.nextInt();
    }


    public static String getSignature(String appToken) {
        return Server.getSecretToken() + "." + appToken;
    }


    public String getAuthToken(CredentialsData credentials, UserData user) {
        Algorithm algorithm;

        try {
            algorithm = Algorithm.HMAC512(UserRepository.getSignature(credentials.getAppToken()));
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

        saveToken(token, credentials.getUsername(), "");
        return token;
    }


    public SessionData verifyAuthToken(String authToken, String appToken) {
        if (!hasToken(authToken)) {
            throw new UnauthorizedException();
        }

        try {
            Algorithm algorithm = Algorithm.HMAC512(UserRepository.getSignature(appToken));

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


    public void saveToken(String authToken, String username, String ip) {
        jdbc.update(
                "INSERT INTO Tokens (token, username, ip, \"location\") VALUES (?, ?, ?, ?)",
                authToken, username, ip
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


    public static String makeSalty(String password, int salt) {
        Random random = new Random(salt);
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger().error(e);
            System.exit(-1);
        }

        byte[] intermediate = null;

        try {
            intermediate = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger().error( e);
            System.exit(-1);
        }

        int upper = 100 + random.nextInt(500);

        for (int i = 0; i < upper; i++) {
            intermediate = md.digest(intermediate);
        }

        return Base64.getEncoder().encodeToString(intermediate);
    }

}

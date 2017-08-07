package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.data.CredentialsData;
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
import java.util.Base64;
import java.util.Random;


@org.springframework.stereotype.Repository
public class UserRepository extends Repository {

    private SecureRandom random = new SecureRandom();
    private ConfirmationRepository confirmations;
    private TokenRepository tokens;


    @Autowired
    public UserRepository(ConfirmationRepository confirmations, TokenRepository tokens) {
        this.confirmations = confirmations;
        this.tokens = tokens;
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

        this.confirmations.requestSignUpConfirmation(username, email);
    }


    public String login(CredentialsData credentials, String ip) throws WrongCredentialsException {
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

        return tokens.getAuthToken(credentials, user, ip);
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

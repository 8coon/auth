package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.utils.ErrorCodes;
import org.minecraftshire.auth.utils.UserGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Random;


@Repository
public class UserRepository {

    private JdbcTemplate jdbc;
    private SecureRandom random = new SecureRandom();
    private ConfirmationRepository confirmationRepository;


    @Autowired
    public UserRepository(JdbcTemplate jdbc, ConfirmationRepository confirmationRepository) {
        this.jdbc = jdbc;
        this.confirmationRepository = confirmationRepository;
    }


    @Transactional
    public int create(String username, String email, String password) {
        if (username == null || email == null || password == null) {
            return ErrorCodes.BAD_REQUEST;
        }

        try {
            this.jdbc.queryForObject(
                    "SELECT username FROM Users WHERE username = ?",
                    String.class,
                    username
            );
            return ErrorCodes.USERNAME_EXISTS;
        } catch (EmptyResultDataAccessException e) {
        }


        int salt = this.generateSalt();
        String salty = this.makeSalty(password, salt);

        try {
            this.jdbc.update(
                    "INSERT INTO Users (username, password, email, salt, \"group\", is_confirmed) VALUES" +
                            "(?, ?, ?, ?, ?, ?)",
                    username, salty, email, salt, UserGroups.STANDARD, false
            );
        } catch (DataIntegrityViolationException e) {
            return ErrorCodes.EMAIL_EXISTS;
        }

        this.confirmationRepository.requestSignUpConfirmation(username, email);
        return ErrorCodes.OK;
    }


    public int generateSalt() {
        return this.random.nextInt();
    }


    public String makeSalty(String password, int salt) {
        Random random = new Random(salt);
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        byte[] intermediate = null;

        try {
            intermediate = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        int upper = 100 + random.nextInt(500);

        for (int i = 0; i < upper; i++) {
            intermediate = md.digest(intermediate);
        }

        return Base64.getEncoder().encodeToString(intermediate);
    }

}

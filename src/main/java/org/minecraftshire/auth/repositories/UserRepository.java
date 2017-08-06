package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.exceptions.ExistsException;
import org.minecraftshire.auth.exceptions.ExistsExceptionCause;
import org.minecraftshire.auth.utils.ErrorCodes;
import org.minecraftshire.auth.utils.UserGroups;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
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
                "INSERT INTO Users (username, password, email, salt, \"group\", is_confirmed) VALUES" +
                        "(?, ?, ?, ?, ?, ?)",
                username, salty, email, salt, UserGroups.STANDARD, false
        );

        this.confirmationRepository.requestSignUpConfirmation(username, email);
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

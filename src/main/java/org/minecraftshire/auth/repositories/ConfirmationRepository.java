package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.services.EmailSender;
import org.minecraftshire.auth.utils.EmailOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;


@Repository
public class ConfirmationRepository {

    private JdbcTemplate jdbc;
    private EmailSender mail;
    private SecureRandom random = new SecureRandom();


    @Autowired
    public ConfirmationRepository(JdbcTemplate jdbc, EmailSender mail) {
        this.jdbc = jdbc;
        this.mail = mail;
    }


    public void requestSignUpConfirmation(String username, String email) {
        long code = Math.abs(this.random.nextLong());

        this.jdbc.update(
                "INSERT INTO Confirmations (username, operation, code, email) VALUES (?, ?, ?, ?)",
                username, EmailOperations.EMAIL_CONFIRM, code, email
        );

        this.mail.sendEmailConfirmation(email, code, "");
    }


    @Transactional
    public boolean confirmSignUp(long code) {
        String username;

        try {
            username = this.jdbc.queryForObject(
                    "SELECT username FROM Confirmations WHERE operation = ? AND code = ? LIMIT 1",
                    String.class,
                    EmailOperations.EMAIL_CONFIRM, code
            );
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

        this.jdbc.update("DELETE FROM Confirmations WHERE code = ?", code);
        this.jdbc.update("UPDATE Users SET is_confirmed = TRUE");

        return true;
    }

}

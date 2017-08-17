package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.exceptions.WrongCredentialsException;
import org.minecraftshire.auth.exceptions.WrongCredentialsExceptionCause;
import org.minecraftshire.auth.services.EmailSender;
import org.minecraftshire.auth.utils.EmailOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;


@Repository
public class ConfirmationRepository extends org.minecraftshire.auth.repositories.Repository {

    private final EmailSender mail;
    private SecureRandom random = new SecureRandom();

    @Autowired
    public ConfirmationRepository(EmailSender mail) {
        this.mail = mail;
    }


    public void requestSignUpConfirmation(String username, String email) {
        long code = Math.abs(this.random.nextLong());

        this.jdbc.update(
                "INSERT INTO Confirmations (username, operation, code, email) VALUES (?, ?, ?, ?)",
                username, EmailOperations.EMAIL_CONFIRM, code, email
        );

        this.mail.sendEmailConfirmation(email, code);
    }


    public void requestChangePasswordConfirmation(String username, String email) {
        long code = Math.abs(this.random.nextLong());

        this.jdbc.update(
                "INSERT INTO Confirmations (username, operation, code, email) VALUES (?, ?, ?, ?)",
                username, EmailOperations.PASSWORD_RESET, code, email
        );

        this.mail.sendEmailPasswordReset(email, code);
    }


    @Transactional
    public boolean confirm(long code) {
        String username;

        try {
            username = this.jdbc.queryForObject(
                    "SELECT username FROM Confirmations WHERE code = ? LIMIT 1",
                    String.class,
                    code
            );
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

        this.jdbc.update("DELETE FROM Confirmations WHERE code = ?", code);
        this.jdbc.update("UPDATE Users SET is_confirmed = TRUE WHERE username = ?", username);

        return true;
    }


    public String getUsername(long code) throws WrongCredentialsException {
        try {
            return this.jdbc.queryForObject(
                    "SELECT username FROM Confirmations WHERE code = ? LIMIT 1",
                    String.class,
                    code
            );
        } catch (EmptyResultDataAccessException e) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.NOT_FOUND);
        }
    }

}

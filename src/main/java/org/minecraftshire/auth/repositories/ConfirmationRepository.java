package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.data.ConfirmationData;
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


    public void requestChangeEmailConfirmation(String username, String email) {
        long code = Math.abs(this.random.nextLong());

        this.jdbc.update(
                "INSERT INTO Confirmations (username, operation, code, email) VALUES (?, ?, ?, ?)",
                username, EmailOperations.EMAIL_CHANGE, code, email
        );

        this.mail.sendEmailConfirmation(email, code);
    }


    @Transactional
    public boolean confirm(long code) {
        ConfirmationData confirmation;

        try {
            confirmation = jdbc.queryForObject(
                    "SELECT username, operation, code, email FROM Confirmations WHERE code = ? LIMIT 1",
                    new ConfirmationData(),
                    code
            );
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

        // Удаляем Confirmation
        this.jdbc.update("DELETE FROM Confirmations WHERE code = ?", code);

        // Выполняем действие
        switch (confirmation.getOperation()) {
            case EmailOperations.EMAIL_CHANGE: {
                this.jdbc.update(
                        "UPDATE Users SET email = ? WHERE username = ?",
                        confirmation.getEmail(),
                        confirmation.getUsername()
                );

                break;
            }

            case EmailOperations.EMAIL_CONFIRM: {
                this.jdbc.update(
                        "UPDATE Users SET is_confirmed = TRUE WHERE username = ?",
                        confirmation.getUsername()
                );

                break;
            }

            case EmailOperations.PASSWORD_RESET: {
                // Обработано в UserRepository
                break;
            }
        }

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

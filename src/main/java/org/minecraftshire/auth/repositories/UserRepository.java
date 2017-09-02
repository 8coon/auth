package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.data.*;
import org.minecraftshire.auth.exceptions.*;
import org.minecraftshire.auth.services.PasswordEncoder;
import org.minecraftshire.auth.utils.UserGroups;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;


@org.springframework.stereotype.Repository
public class UserRepository extends Repository {

    private MessageDigest md;
    private ConfirmationRepository confirmations;
    private TokenRepository tokens;
    private NotificationRepository notifications;
    private ModificationRepository modifications;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserRepository(
            ConfirmationRepository confirmations,
            TokenRepository tokens,
            NotificationRepository notifications,
            ModificationRepository modifications,
            PasswordEncoder passwordEncoder
    ) {
        this.confirmations = confirmations;
        this.tokens = tokens;
        this.notifications = notifications;
        this.modifications = modifications;
        this.passwordEncoder = passwordEncoder;

        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger().severe(e);
        }
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

        int salt = passwordEncoder.generateSalt();
        String salty = passwordEncoder.makeSalty(password, salt);

        this.jdbc.update(
                "INSERT INTO Users " +
                        "(username, password, email, salt, \"group\", is_confirmed, is_banned, password_length) " +
                        "VALUES" +
                        "(?, ?, ?, ?, ?, ?, ?, ?)",
                username, salty, email, salt, UserGroups.STANDARD, false, false, password.length()
        );

        this.confirmations.requestSignUpConfirmation(username, email);
    }


    @Transactional
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

        String hash = passwordEncoder.makeSalty(credentials.getPassword(), user.getSalt());

        if (!user.getPassword().equals(hash)) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.USERNAME_OR_PASSWORD);
        }

        return tokens.getAuthToken(credentials, user, ip);
    }

    @Transactional
    public void changeEmail(String username, String newEmail) throws ExceptionWithCause {
        String oldEmail;

        try {
            oldEmail = jdbc.queryForObject(
                    "SELECT email FROM Users WHERE username = ? LIMIT 1",
                    String.class,
                    username
            );
        } catch (DataAccessException e) {
            throw new ExceptionWithCause(GenericCause.USER_NOT_FOUND);
        }

        if (oldEmail.equalsIgnoreCase(newEmail)) {
            throw new ExceptionWithCause(GenericCause.SAME_EMAIL);
        }

        confirmations.requestChangeEmailConfirmation(username, newEmail);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) throws WrongCredentialsException {
        UserData user;

        try {
            user = this.jdbc.queryForObject(
                    "SELECT username, email, password, salt, \"group\", is_confirmed, is_banned FROM Users WHERE username = ? LIMIT 1",
                    new UserData(),
                    username
            );
        } catch (DataAccessException e) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.NOT_FOUND);
        }

        if (oldPassword.equals(newPassword)) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.SAME_PASSWORD);
        }

        oldPassword = passwordEncoder.makeSalty(oldPassword, user.getSalt());
        newPassword = passwordEncoder.makeSalty(newPassword, user.getSalt());

        if (!oldPassword.equals(user.getPassword())) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.PASSWORD);
        }

        this.jdbc.update(
                "UPDATE Users SET password = ? WHERE username = ?",
                newPassword, username
        );
    }


    @Transactional
    public void resetPassword(String email) throws WrongCredentialsException {
        String username;

        try {
            username = jdbc.queryForObject(
                    "SELECT username FROM Users WHERE email = ? LIMIT 1",
                    String.class,
                    email
            );
        } catch (DataAccessException e) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.NOT_FOUND);
        }

        this.confirmations.requestChangePasswordConfirmation(username, email);
    }


    @Transactional
    public void setPassword(long code, String password) throws WrongCredentialsException {
        String username = confirmations.getUsername(code);

        if (!confirmations.confirm(code)) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.NOT_CONFIRMED);
        }

        UserData user = this.jdbc.queryForObject(
                "SELECT username, email, password, salt, \"group\", is_confirmed, is_banned FROM Users WHERE username = ? LIMIT 1",
                new UserData(),
                username
        );

        String newPassword = passwordEncoder.makeSalty(password, user.getSalt());
        this.jdbc.update(
                "UPDATE Users SET password = ?, password_length = ? WHERE username = ?",
                newPassword, newPassword.length(), username
        );
    }


    @Transactional
    public UserStatusData getStatus(String username, String lastModified) throws EmptyResultDataAccessException {
        if (lastModified != null) {
            List<UserStatusData> users = jdbc.query(
                    "SELECT " +
                            "username, last_modified, free_balance, total_balance, avatar_hash, avatar_content_type " +
                            "FROM Users WHERE " +
                            "username = ? AND last_modified > ?::TIMESTAMPTZ LIMIT 1",
                    new UserStatusData(),
                    username, lastModified
            );

            if (users.size() == 0) {
                return null;
            }

            UserStatusData user = users.get(0);
            user.setNotifications(notifications.get(username, true));
        }

        UserStatusData user = jdbc.queryForObject(
                "SELECT " +
                        "username, last_modified, free_balance, total_balance, avatar_hash, avatar_content_type " +
                        "FROM Users WHERE " +
                        "username = ? LIMIT 1",
                new UserStatusData(),
                username
        );

        user.setNotifications(notifications.get(username, true));
        return user;
    }


    @Transactional
    public void setAvatar(String username, byte[] avatar, String contentType) {
        // Считаем хэш аватара, преобразовываем в base64, убираем лишние символы, чтобы в урле было покрасивее.
        String hash = Base64.getEncoder().encodeToString(md.digest(avatar))
                .replace("+", "").replace("/", "").replace("=", "").replace("%", "");

        jdbc.update(
                "UPDATE Users SET avatar = ?, avatar_content_type = ?, avatar_hash = ? WHERE username = ?",
                avatar,
                contentType,
                hash,
                username
        );
        modifications.updateUser(username);
    }


    @Transactional
    public AvatarData getAvatar(String username) {
        try {
            return jdbc.queryForObject(
                    "SELECT avatar, avatar_content_type FROM Users WHERE username = ? LIMIT 1",
                    new AvatarData(),
                    username
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    /**
     * @param username имя пользователя
     * @param ownProfile является ли данный пользователь владельцем этого профиля
     */
    @Transactional
    public ProfileData getFullProfile(String username, boolean ownProfile) {
        try {
            if (ownProfile) {
                return jdbc.queryForObject(
                        "SELECT username, email, password_length, avatar_hash, avatar_content_type, " +
                                "total_balance, free_balance, \"group\" " +
                                "FROM Users WHERE username = ? LIMIT 1",
                        new ProfileData(),
                        username
                );
            } else {
                return jdbc.queryForObject(
                        "SELECT username, NULL AS email, 0 AS password_length, avatar_hash, avatar_content_type, " +
                                "total_balance, free_balance, \"group\" " +
                                "FROM Users WHERE username = ? LIMIT 1",
                        new ProfileData(),
                        username
                );
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}

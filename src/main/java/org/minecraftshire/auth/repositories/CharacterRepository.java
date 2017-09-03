package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.data.character.CharacterCreationData;
import org.minecraftshire.auth.data.character.CharacterData;
import org.minecraftshire.auth.data.character.SkinData;
import org.minecraftshire.auth.exceptions.ExceptionWithCause;
import org.minecraftshire.auth.exceptions.GenericCause;
import org.minecraftshire.auth.exceptions.WrongCredentialsException;
import org.minecraftshire.auth.exceptions.WrongCredentialsExceptionCause;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;


@org.springframework.stereotype.Repository
public class CharacterRepository extends Repository {

    private MessageDigest md;


    public CharacterRepository() {
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger().severe(e);
        }
    }


    @Transactional
    public void create(CharacterCreationData data) throws ExceptionWithCause {
        boolean exists = true;

        // Проверяем, что такого персонажа нет
        try {
            jdbc.queryForObject(
                    "SELECT id FROM Characters WHERE lower(first_name) = ? AND lower(last_name) = ? LIMIT 1",
                    Integer.class,
                    data.getFirstName().toLowerCase(), data.getLastName().toLowerCase()
            );
        } catch (EmptyResultDataAccessException e) {
            exists = false;
        }

        // Если такой есть, выбрасываем исключение
        if (exists) {
            throw new ExceptionWithCause(GenericCause.CHARACTER_EXISTS);
        }

        jdbc.update(
                "INSERT INTO Characters (first_name, last_name, owner) VALUES (?, ?, ?)",
                data.getFirstName(), data.getLastName(), data.getOwner()
        );
    }


    @Transactional
    public List<CharacterData> list(String username) {
        return jdbc.query(
                "SELECT " +
                     "id, first_name, last_name, owner, is_online, created_at, skin_hash, skin_content_type," +
                     " is_favorite FROM Characters WHERE owner = ? AND deleted = FALSE LIMIT 1000",
                new CharacterData(),
                username
        );
    }


    @Transactional
    public CharacterData get(int id) throws ExceptionWithCause {
        try {
            return jdbc.queryForObject(
                    "SELECT " +
                         "id, first_name, last_name, owner, is_online, created_at, skin_hash, skin_content_type," +
                         " is_favorite FROM Characters WHERE id = ? AND deleted = FALSE LIMIT 1",
                    new CharacterData(),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionWithCause(GenericCause.CHARACTER_NOT_FOUND);
        }
    }


    @Transactional
    public CharacterData get(String firstName, String lastName) throws ExceptionWithCause {
        try {
            return jdbc.queryForObject(
                    "SELECT " +
                            "id, first_name, last_name, owner, is_online, created_at, skin_hash, skin_content_type," +
                            " is_favorite FROM Characters WHERE lower(first_name) = ? AND lower(last_name) = ? AND deleted = FALSE LIMIT 1",
                    new CharacterData(),
                    firstName.toLowerCase(), lastName.toLowerCase()
            );
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionWithCause(GenericCause.CHARACTER_NOT_FOUND);
        }
    }


    @Transactional
    public void setFavorite(String username, int id, boolean isFavorite) throws WrongCredentialsException {
        String owner = jdbc.queryForObject(
                "SELECT owner FROM Characters WHERE id = ? LIMIT 1",
                String.class,
                id
        );

        // Если владелец персонажа не совпадает с отправителем запроса
        if (!owner.equals(username)) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.PERMISSION_DENIED);
        }

        jdbc.update(
                "UPDATE Characters SET is_favorite = ? WHERE id = ?",
                isFavorite, id
        );
    }


    @Transactional
    public void setOnline(String username, int id, boolean isOnline) throws WrongCredentialsException {
        String owner = jdbc.queryForObject(
                "SELECT owner FROM Characters WHERE id = ? LIMIT 1",
                String.class,
                id
        );

        // Если владелец персонажа не совпадает с отправителем запроса
        if (!owner.equals(username)) {
            throw new WrongCredentialsException(WrongCredentialsExceptionCause.PERMISSION_DENIED);
        }

        jdbc.update(
                "UPDATE Characters SET is_online = ? WHERE id = ?",
                isOnline, id
        );
    }


    @Transactional
    public void setSkin(int charId, byte[] skin, String contentType) {
        // Считаем хэш скина, преобразовываем в base64, убираем лишние символы, чтобы в урле было покрасивее.
        String hash = Base64.getEncoder().encodeToString(md.digest(skin))
                .replace("+", "").replace("/", "").replace("=", "").replace("%", "");

        jdbc.update(
                "UPDATE Characters SET skin = ?, skin_content_type = ?, skin_hash = ? WHERE id = ?",
                skin, contentType, hash, charId
        );
    }


    @Transactional
    public SkinData getSkin(int charId) {
        try {
            return jdbc.queryForObject(
                    "SELECT skin, skin_content_type FROM Characters WHERE id = ? LIMIT 1",
                    new SkinData(),
                    charId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Transactional
    public void delete(int charId, String username) throws WrongCredentialsException {
        String owner;

        try {
            owner = jdbc.queryForObject(
                    "SELECT owner FROM Characters WHERE id = ? LIMIT 1",
                    String.class,
                    charId
            );
        } catch (EmptyResultDataAccessException e) {
            throw new WrongCredentialsException(GenericCause.CHARACTER_NOT_FOUND);
        }

        if (!owner.equals(username)) {
            throw new WrongCredentialsException(GenericCause.PERMISSION_DENIED);
        }

        jdbc.update("UPDATE Characters SET deleted = true WHERE id = ?", charId);
    }

}

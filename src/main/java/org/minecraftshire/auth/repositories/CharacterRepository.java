package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.data.character.CharacterCreationData;
import org.minecraftshire.auth.data.character.CharacterData;
import org.minecraftshire.auth.exceptions.ExceptionWithCause;
import org.minecraftshire.auth.exceptions.GenericCause;
import org.minecraftshire.auth.exceptions.WrongCredentialsException;
import org.minecraftshire.auth.exceptions.WrongCredentialsExceptionCause;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;


@org.springframework.stereotype.Repository
public class CharacterRepository extends Repository {


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


    public List<CharacterData> list(String username) {
        return jdbc.query(
                "SELECT " +
                     "id, first_name, last_name, owner, is_online, created_at, skin_hash, skin_content_type," +
                     " is_favorite FROM Characters WHERE owner = ? AND deleted = FALSE LIMIT 1000",
                new CharacterData(),
                username
        );
    }


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

}

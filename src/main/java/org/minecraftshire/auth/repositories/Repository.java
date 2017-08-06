package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;


@org.springframework.stereotype.Repository
public abstract class Repository {

    @Autowired
    public JdbcTemplate jdbc;
    private Logger log = Logger.getLogger();


    public JdbcTemplate getJdbc() {
        return this.jdbc;
    }

    public Logger getLog() {
        return this.log;
    }


    public boolean hasObject(String table, String key, Object value) {
        try {
            this.jdbc.queryForObject(
                    "SELECT " + key + " FROM " + table + " WHERE " + key + " = ? LIMIT 1",
                    value.getClass(),
                    value
            );

        } catch (EmptyResultDataAccessException e) {
            return false;
        }

        return true;
    }

}


package org.minecraftshire.auth.data.change;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ConfirmationData implements RowMapper<ConfirmationData> {

    private String username;
    private int operation;
    private long code;
    private String email;


    public ConfirmationData(
            String username,
            int operation,
            long code,
            String email
    ) {
        this.username = username;
        this.operation = operation;
        this.code = code;
        this.email = email;
    }


    @JsonCreator
    public ConfirmationData(
            @JsonProperty("code") long code
    ) {
        this.code = code;
    }

    public ConfirmationData() {}


    @Override
    public ConfirmationData mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ConfirmationData(
                resultSet.getString("username"),
                resultSet.getInt("operation"),
                resultSet.getLong("code"),
                resultSet.getString("email")
        );
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

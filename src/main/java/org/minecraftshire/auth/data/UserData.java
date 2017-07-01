package org.minecraftshire.auth.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.flywaydb.core.internal.util.jdbc.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserData implements RowMapper<UserData> {

    private String username;
    private String password;
    private String email;
    private int salt;
    private boolean salty = false;


    @JsonCreator
    public UserData(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email
    ) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    @Override
    public UserData mapRow(ResultSet resultSet) throws SQLException {
        UserData userData = new UserData(
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("email")
        );

        userData.setSalt(resultSet.getInt("salt"));

        return userData;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
        this.salty = true;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isSalty() {
        return salty;
    }

}

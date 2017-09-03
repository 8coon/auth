package org.minecraftshire.auth.data.user;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserData implements RowMapper<UserData> {

    private String username;
    private String password;
    private String email;
    private int salt;
    private boolean salty = false;
    private int group;
    private boolean confirmed;
    private boolean banned;


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


    public UserData() {
    }


    @Override
    public UserData mapRow(ResultSet resultSet, int i) throws SQLException {
        UserData userData = new UserData(
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("email")
        );

        userData.setSalt(resultSet.getInt("salt"));
        userData.setBanned(resultSet.getBoolean("is_banned"));
        userData.setConfirmed(resultSet.getBoolean("is_confirmed"));
        userData.setGroup(resultSet.getInt("group"));

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

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isSalty() {
        return salty;
    }

}

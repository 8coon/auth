package org.minecraftshire.auth.data;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TokenHistoryData implements RowMapper<TokenHistoryData> {

    private String username;
    private String time;
    private String ip;
    private String location;
    private String appToken;


    public TokenHistoryData(
            String username,
            String time,
            String ip,
            String location,
            String appToken
    ) {
        this.username = username;
        this.time = time;
        this.ip = ip;
        this.location = location;
        this.appToken = appToken;
    }

    public TokenHistoryData() {}


    @Override
    public TokenHistoryData mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TokenHistoryData(
                resultSet.getString("username"),
                resultSet.getString("time"),
                resultSet.getString("ip"),
                resultSet.getString("location"),
                resultSet.getString("appToken")
        );
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

}

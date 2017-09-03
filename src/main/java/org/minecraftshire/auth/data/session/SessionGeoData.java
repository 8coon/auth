package org.minecraftshire.auth.data.session;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SessionGeoData implements RowMapper<SessionGeoData> {

    private String ip;
    private String issuedAt;
    private String location;


    public SessionGeoData(
            String ip,
            String issuedAt,
            String location
    ) {
        this.ip = ip;
        this.issuedAt = issuedAt;
        this.location = location;
    }

    public SessionGeoData() {
    }


    @Override
    public SessionGeoData mapRow(ResultSet resultSet, int i) throws SQLException {
        return new SessionGeoData(
                resultSet.getString("ip"),
                resultSet.getString("issued_at"),
                resultSet.getString("location")
        );
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}

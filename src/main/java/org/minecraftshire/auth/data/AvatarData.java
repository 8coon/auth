package org.minecraftshire.auth.data;


import org.springframework.jdbc.core.RowMapper;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AvatarData implements RowMapper<AvatarData> {

    private String contentType;
    private byte[] data;


    public AvatarData(
            String contentType,
            byte[] data
    ) {
        this.contentType = contentType;
        this.data = data;
    }

    public AvatarData() {}


    @Override
    public AvatarData mapRow(ResultSet resultSet, int i) throws SQLException {
        Blob avatar = resultSet.getBlob("avatar");

        return new AvatarData(
                resultSet.getString("avatar_content_type"),
                avatar.getBytes(1, (int) avatar.length())
        );
    }


    public String getContentType() {
        return contentType;
    }

    public byte[] getData() {
        return data;
    }

}

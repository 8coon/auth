package org.minecraftshire.auth.data;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

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
        DefaultLobHandler lob = new DefaultLobHandler();
        lob.getBlobAsBytes(resultSet, "avatar");

        return new AvatarData(
                resultSet.getString("avatar_content_type"),
                lob.getBlobAsBytes(resultSet, "avatar")
        );
    }


    public String getContentType() {
        return contentType;
    }

    public byte[] getData() {
        return data;
    }

}

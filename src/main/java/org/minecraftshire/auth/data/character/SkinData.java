package org.minecraftshire.auth.data.character;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SkinData implements RowMapper<SkinData> {

    private String contentType;
    private byte[] data;


    public SkinData(
            String contentType,
            byte[] data
    ) {
        this.contentType = contentType;
        this.data = data;
    }

    public SkinData() {}


    @Override
    public SkinData mapRow(ResultSet resultSet, int i) throws SQLException {
        DefaultLobHandler lob = new DefaultLobHandler();
        lob.getBlobAsBytes(resultSet, "skin");

        return new SkinData(
                resultSet.getString("skin_content_type"),
                lob.getBlobAsBytes(resultSet, "skin")
        );
    }


    public String getContentType() {
        return contentType;
    }

    public byte[] getData() {
        return data;
    }

}

package org.minecraftshire.auth.data.character;


import org.minecraftshire.auth.storages.UploadStorage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CharacterData implements RowMapper<CharacterData> {
    private int id;
    private String firstName;
    private String lastName;
    private String owner;  // Username
    private boolean isOnline;
    private String createdAt;
    private String skinUrl;


    public CharacterData(
            int id,
            String firstName,
            String lastName,
            String owner,
            boolean isOnline,
            String createdAt,
            String skinHash,
            String skinContentType
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.owner = owner;
        this.isOnline = isOnline;
        this.createdAt = createdAt;

        if (skinHash != null) {
            this.skinUrl = CharacterData.getSkinUrl(id, skinHash, skinContentType);
        }
    }

    public CharacterData() {}


    @Override
    public CharacterData mapRow(ResultSet resultSet, int i) throws SQLException {
        return new CharacterData(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("owner"),
                resultSet.getBoolean("is_online"),
                resultSet.getString("created_at"),
                resultSet.getString("skin_hash"),
                resultSet.getString("skin_content_type")
        );
    }


    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getSkinUrl() {
        return skinUrl;
    }


    public static String getSkinUrl(int id, String hash, String contentType) {
        return "character/" + String.valueOf(id) + "/" + UploadStorage.getUrl(hash, contentType);
    }

}

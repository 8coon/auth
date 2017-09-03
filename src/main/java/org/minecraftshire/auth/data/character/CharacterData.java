package org.minecraftshire.auth.data.character;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    private boolean isFavorite;
    private boolean isDeleted;
    private String createdAt;
    private String skinUrl;


    public CharacterData(
            int id,
            String firstName,
            String lastName,
            String owner,
            boolean isOnline,
            boolean isFavorite,
            boolean isDeleted,
            String createdAt,
            String skinHash,
            String skinContentType
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.owner = owner;
        this.isOnline = isOnline;
        this.isFavorite = isFavorite;
        this.isDeleted = isDeleted;
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
                resultSet.getBoolean("is_favorite"),
                resultSet.getBoolean("deleted"),
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

    @JsonProperty("isOnline")
    public boolean isOnline() {
        return isOnline;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getSkinUrl() {
        return skinUrl;
    }

    @JsonProperty("isFavorite")
    public boolean isFavorite() {
        return isFavorite;
    }

    public static String getSkinUrl(int id, String hash, String contentType) {
        return "character/" + String.valueOf(id) + "/" + UploadStorage.getUrl(hash, contentType);
    }

    @JsonProperty("isDeleted")
    public boolean isDeleted() {
        return isDeleted;
    }
}

package org.minecraftshire.auth.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class NotificationData extends AuthTokenData implements RowMapper<NotificationData> {

    private int id;
    private String username;
    private String createdAt;
    private String title;
    private String text;
    private String pictureUrl;
    private String detailsUrl;
    private boolean unread;


    @JsonCreator
    public NotificationData(
            @JsonProperty("authToken") String authToken,
            @JsonProperty("id") int id,
            @JsonProperty("username") String username,
            @JsonProperty("createdAt") String createdAt,
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("pictureUrl") String pictureUrl,
            @JsonProperty("detailsUrl") String detailsUrl,
            @JsonProperty("unread") boolean unread
    ) {
        super(authToken);

        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
        this.title = title;
        this.text = text;
        this.pictureUrl = pictureUrl;
        this.detailsUrl = detailsUrl;
        this.unread = unread;
    }

    public NotificationData() {
        super("");
    }


    @Override
    public NotificationData mapRow(ResultSet resultSet, int i) throws SQLException {
        return new NotificationData(
                "",
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("created_at"),
                resultSet.getString("title"),
                resultSet.getString("text"),
                resultSet.getString("picture_url"),
                resultSet.getString("details_url"),
                resultSet.getBoolean("unread")
        );
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

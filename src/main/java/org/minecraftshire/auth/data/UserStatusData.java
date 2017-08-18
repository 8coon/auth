package org.minecraftshire.auth.data;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class UserStatusData implements RowMapper<UserStatusData> {

    private String username;
    private String lastModified;
    private String avatarUrl;
    private int totalBalance;
    private int freeBalance;
    private List<NotificationData> notifications;


    public UserStatusData(
            String username,
            String lastModified,
            int totalBalance,
            int freeBalance,
            List<NotificationData> notifications,
            String avatarHash,
            String avatarContentType
    ) {
        this.username = username;
        this.lastModified = lastModified;
        this.totalBalance = totalBalance;
        this.freeBalance = freeBalance;
        this.notifications = notifications;
        this.avatarUrl = "user/" + username + "/" + avatarHash;

        // Определяем расширение "файла" по его contentType.
        if ("image/jpeg".equalsIgnoreCase(avatarContentType)) {
            this.avatarUrl += ".jpg";
        } else if ("image/png".equalsIgnoreCase(avatarContentType)) {
            this.avatarUrl += ".png";
        } else {
            // У аватарки выставлен некорректный contentType -- не дадим пользователю её скачать!
            this.avatarUrl = null;
        }
    }

    public UserStatusData() {}



    @Override
    public UserStatusData mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserStatusData(
                resultSet.getString("username"),
                resultSet.getString("last_modified"),
                resultSet.getInt("total_balance"),
                resultSet.getInt("free_balance"),
                null,
                resultSet.getString("avatar_hash"),
                resultSet.getString("avatar_content_type")
        );
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
    }

    public int getFreeBalance() {
        return freeBalance;
    }

    public void setFreeBalance(int freeBalance) {
        this.freeBalance = freeBalance;
    }

    public List<NotificationData> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationData> notifications) {
        this.notifications = notifications;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }
}

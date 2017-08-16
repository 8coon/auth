package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.data.NotificationData;
import org.minecraftshire.auth.utils.logging.Logger;

import java.sql.SQLException;
import java.util.List;


@org.springframework.stereotype.Repository
public class NotificationRepository extends Repository {


    public void add(NotificationData notification) {
        this.jdbc.update(
                "INSERT INTO Notifications " +
                        "(username, title, \"text\", picture_url, details_url) " +
                        "VALUES (?, ?, ?, ?, ?)",
                notification.getUsername(),
                notification.getTitle(),
                notification.getText(),
                notification.getPictureUrl(),
                notification.getDetailsUrl()
        );
    }


    public void truncate() {
        this.jdbc.update(
                "DELETE FROM Notifications WHERE !unread AND now() - created_at > INTERVAL '10 days'"
        );
    }


    public void markRead(String username, Integer[] ids) {
        try {
            this.jdbc.update(
                    "UPDATE Notifications SET unread = FALSE WHERE username = ? AND id IN ?",
                    username,
                    jdbc.getDataSource().getConnection().createArrayOf("INT", ids)
            );
        } catch (SQLException e) {
            Logger.getLogger().severe(e);
        }
    }


    public List<NotificationData> get(String username) {
        return this.jdbc.query(
                "SELECT * FROM Notifications WHERE username = ? ORDER BY created_at DESC LIMIT 1000",
                new NotificationData(),
                username
        );
    }

}


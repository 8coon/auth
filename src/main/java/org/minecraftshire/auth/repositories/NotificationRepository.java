package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.data.NotificationData;
import org.minecraftshire.auth.utils.logging.Logger;

import java.sql.SQLException;
import java.util.Arrays;
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


    public void markRead(String username, int[] idx) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');

        int i = 0;
        for (int value: idx) {
            sb.append('\"');
            sb.append(String.valueOf(value));
            sb.append('\"');

            if (i < idx.length - 1) {
                sb.append(',');
            }

            i++;
        }

        sb.append('}');

        this.jdbc.update(
                "UPDATE Notifications SET unread = FALSE WHERE username = ? AND id IN ?",
                username,
                sb.toString()
        );
    }


    public List<NotificationData> get(String username, boolean unread) {
        String unreadCond = "";

        if (unread) {
            unreadCond = "AND unread";
        }

        return this.jdbc.query(
                "SELECT * FROM Notifications WHERE username = ? " + unreadCond +
                        " ORDER BY created_at DESC LIMIT 1000",
                new NotificationData(),
                username
        );
    }

    public List<NotificationData> get(String username) {
        return this.get(username, false);
    }

}


package org.minecraftshire.auth.repositories;


import org.minecraftshire.auth.data.notification.NotificationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@org.springframework.stereotype.Repository
public class NotificationRepository extends Repository {

    public final ModificationRepository modifications;

    @Autowired
    public NotificationRepository(ModificationRepository modifications) {
        this.modifications = modifications;
    }


    @Transactional
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
        this.modifications.updateUser(notification.getUsername());
    }


    @Transactional
    public void truncate() {
        this.jdbc.update(
                "DELETE FROM Notifications WHERE !unread AND now() - created_at > INTERVAL '10 days'"
        );
    }


    @Transactional
    public void markRead(String username, Integer[] idx) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');

        int i = 0;
        for (int value: idx) {
            sb.append(String.valueOf(value));

            if (i < idx.length - 1) {
                sb.append(',');
            }

            i++;
        }

        sb.append(')');

        this.jdbc.update(
                "UPDATE Notifications SET unread = FALSE WHERE username = ? AND id IN " + sb.toString(),
                username
        );
        this.modifications.updateUser(username);
    }


    @Transactional
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


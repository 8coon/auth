
CREATE TABLE Notifications (
    id SERIAL PRIMARY KEY,
    username TEXT REFERENCES Users,
    created_at TIMESTAMPTZ DEFAULT now(),
    title TEXT,
    "text" TEXT,
    picture_url TEXT,
    details_url TEXT,
    unread BOOLEAN DEFAULT TRUE
);


CREATE INDEX NotificationUsernameCreatedAtIndex ON Notifications(username, created_at);
CREATE INDEX NotificationUsernameCreatedAtUnreadIndex ON Notifications(username, created_at, unread);

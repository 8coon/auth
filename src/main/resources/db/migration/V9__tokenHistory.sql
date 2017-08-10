

CREATE TABLE TokenHistory(
    username TEXT REFERENCES Users,
    time TIMESTAMPTZ DEFAULT now(),
    ip TEXT,
    "location" TEXT,
    appToken TEXT
);


CREATE INDEX TokenHistoryIndex ON TokenHistory(username, time);

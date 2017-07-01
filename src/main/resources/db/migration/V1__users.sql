

CREATE TABLE Users (
    username TEXT UNIQUE PRIMARY KEY,
    password TEXT,
    salt INT,
    email TEXT UNIQUE CHECK (email LIKE '%@%')
);


CREATE UNIQUE INDEX UsersNamePasswordSaltIndex ON Users (username, password, salt);


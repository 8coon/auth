
CREATE TABLE Characters (
    id SERIAL PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    owner TEXT REFERENCES Users,
    is_online BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT now(),
    skin BYTEA DEFAULT NULL,
    skin_hash TEXT DEFAULT NULL,
    skin_content_type TEXT DEFAULT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX CharactersOwnersIdx on Characters(id, owner, deleted);
CREATE INDEX CharactersOnlineIdx on Characters(id, is_online, deleted);
CREATE INDEX CharactersNamesIdx on Characters(id, lower(first_name), lower(last_name));

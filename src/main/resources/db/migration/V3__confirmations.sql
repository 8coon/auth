

CREATE TABLE Confirmations (
    username TEXT,
    operation INT,
    code BIGINT,
    email TEXT
);


CREATE INDEX ConfirmationsCodeOperationIndex ON Confirmations(code, operation);


ALTER TABLE Users ADD COLUMN last_modified TIMESTAMPTZ DEFAULT now() - '10 years';
ALTER TABLE Users ADD COLUMN total_balance MONEY DEFAULT 0;
ALTER TABLE Users ADD COLUMN free_balance MONEY DEFAULT 0;

CREATE INDEX UsersNameDateBalanceIndex on Users(username, last_modified, total_balance, free_balance);


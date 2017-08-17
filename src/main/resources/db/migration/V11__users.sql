
ALTER TABLE Users ADD COLUMN last_modified TIMESTAMPTZ DEFAULT '2001-10-19 1:00:00+03';
ALTER TABLE Users ADD COLUMN total_balance MONEY DEFAULT 0;
ALTER TABLE Users ADD COLUMN free_balance MONEY DEFAULT 0;

CREATE INDEX UsersNameDateBalanceIndex on Users(username, last_modified, total_balance, free_balance);


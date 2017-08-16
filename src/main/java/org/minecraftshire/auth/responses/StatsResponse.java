package org.minecraftshire.auth.responses;


public class StatsResponse {

    private int users;
    private int confirmations;
    private int tokens;
    private int tokenHistory;
    private int notifications;

    public StatsResponse(
            int users,
            int confirmations,
            int tokens,
            int tokenHistory,
            int notifications
    ) {
        this.users = users;
        this.confirmations = confirmations;
        this.tokens = tokens;
        this.tokenHistory = tokenHistory;
        this.notifications = notifications;
    }

    public int getUsers() {
        return this.users;
    }

    public int getConfirmations() {
        return this.confirmations;
    }

    public int getTokens() {
        return this.tokens;
    }

    public int getTokenHistory() {
        return tokenHistory;
    }

    public int getNotifications() {
        return notifications;
    }
}

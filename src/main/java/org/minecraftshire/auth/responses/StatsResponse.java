package org.minecraftshire.auth.responses;


public class StatsResponse {

    private int users;
    private int confirmations;
    private int tokens;

    public StatsResponse(int users, int confirmations, int tokens) {
        this.users = users;
        this.confirmations = confirmations;
        this.tokens = tokens;
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

}

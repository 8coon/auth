package org.minecraftshire.auth.responses;


public class StatsResponse {

    private int users;
    private int confirmations;

    public StatsResponse(int users, int confirmations) {
        this.users = users;
        this.confirmations = confirmations;
    }

    public int getUsers() {
        return this.users;
    }

    public int getConfirmations() {
        return this.confirmations;
    }

}

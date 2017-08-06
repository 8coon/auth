package org.minecraftshire.auth.data;


public class SessionData {

    private String username;
    private int group;

    public SessionData(
            String username,
            int group
    ) {
        this.username = username;
        this.group = group;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

}

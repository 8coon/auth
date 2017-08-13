package org.minecraftshire.auth.aspects;


public class UserAgent {

    private String userAgent;

    public UserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String toString() {
        return this.userAgent;
    }

    public boolean equals(Object another) {
        return (another instanceof UserAgent || another instanceof String) &&
                this.hashCode() == another.hashCode();
    }

    public int hashCode() {
        return userAgent.hashCode();
    }

}

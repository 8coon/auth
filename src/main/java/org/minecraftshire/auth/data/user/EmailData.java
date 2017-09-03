package org.minecraftshire.auth.data.user;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class EmailData {

    private String email;


    @JsonCreator
    public EmailData(
            @JsonProperty("email") String email
    ) {
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

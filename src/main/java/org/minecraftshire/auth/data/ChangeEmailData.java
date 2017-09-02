package org.minecraftshire.auth.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ChangeEmailData extends AuthTokenData {

    private String newEmail;


    @JsonCreator
    public ChangeEmailData(
            @JsonProperty("authToken") String authToken,
            @JsonProperty("newEmail") String newEmail
    ) {
        super(authToken);
        this.newEmail = newEmail;
    }


    public String getNewEmail() {
        return newEmail;
    }
}

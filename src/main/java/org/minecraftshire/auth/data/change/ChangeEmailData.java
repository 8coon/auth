package org.minecraftshire.auth.data.change;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.minecraftshire.auth.data.auth.AuthTokenData;


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

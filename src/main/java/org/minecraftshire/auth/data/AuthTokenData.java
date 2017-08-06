package org.minecraftshire.auth.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class AuthTokenData {

    private String authToken;


    @JsonCreator
    public AuthTokenData(
            @JsonProperty  String authToken
    ) {
        this.authToken = authToken;
    }


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}

package org.minecraftshire.auth.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class AuthTokenWithLastModifiedData extends AuthTokenData {

    private String lastModified;


    @JsonCreator
    public AuthTokenWithLastModifiedData(
            @JsonProperty("authToken") String authToken,
            @JsonProperty("lastModified") String lastModified
    ) {
        super(authToken);
        this.lastModified = lastModified;
    }


    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

}

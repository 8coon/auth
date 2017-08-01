package org.minecraftshire.auth.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.minecraftshire.auth.MinecraftshireAuthApplication;


public class SecretTokenData {

    private String secret;


    @JsonCreator
    public SecretTokenData(
            @JsonProperty String secret
    ) {
        this.secret = secret;
    }


    public String getSecret() {
        return secret;
    }

    public boolean is() {
        return secret.equals(MinecraftshireAuthApplication.getSecretToken());
    }

    public boolean isNot() {
        return !this.is();
    }

}

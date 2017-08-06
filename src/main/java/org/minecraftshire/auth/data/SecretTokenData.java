package org.minecraftshire.auth.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.minecraftshire.auth.Server;


public class SecretTokenData {

    private String secret;


    @JsonCreator
    public SecretTokenData(
            @JsonProperty("secret") String secret
    ) {
        this.secret = secret;
    }


    public String getSecret() {
        return secret;
    }

    public boolean is() {
        return secret.equals(Server.getSecretToken());
    }

    public boolean isNot() {
        return !this.is();
    }

}

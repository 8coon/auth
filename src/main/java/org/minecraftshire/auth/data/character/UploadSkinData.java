package org.minecraftshire.auth.data.character;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.minecraftshire.auth.data.auth.AuthTokenData;


public class UploadSkinData extends AuthTokenData {

    private int id;

    @JsonCreator
    public UploadSkinData(
            @JsonProperty("authToken") String authToken,
            @JsonProperty("id") int id
    ) {
        super(authToken);
        this.id = id;
    }

    public int getId() {
        return id;
    }

}

package org.minecraftshire.auth.data.upload;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.minecraftshire.auth.data.auth.AuthTokenData;


public class UploadTokenData extends AuthTokenData {

    private String uploadToken;


    @JsonCreator
    public UploadTokenData(
            @JsonProperty("authToken") String authToken,
            @JsonProperty("uploadToken") String uploadToken
    ) {
        super(authToken);
        this.uploadToken = uploadToken;
    }


    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }

}

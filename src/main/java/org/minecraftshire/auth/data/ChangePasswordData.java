package org.minecraftshire.auth.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ChangePasswordData extends AuthTokenData {

    private String oldPassword;
    private String newPassword;


    @JsonCreator
    public ChangePasswordData(
            @JsonProperty("authToken") String authToken,
            @JsonProperty("oldPassword") String oldPassword,
            @JsonProperty("newPassword") String newPassword
    ) {
        super(authToken);

        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}

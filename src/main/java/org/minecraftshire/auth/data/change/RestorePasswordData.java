package org.minecraftshire.auth.data.change;


import com.fasterxml.jackson.annotation.JsonProperty;

public class RestorePasswordData {

    private String password;
    private long code;


    public RestorePasswordData(
            @JsonProperty("password") String password,
            @JsonProperty("code") long code
    ) {
        this.password = password;
        this.code = code;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

}

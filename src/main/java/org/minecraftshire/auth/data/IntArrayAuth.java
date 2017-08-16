package org.minecraftshire.auth.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class IntArrayAuth extends AuthTokenData {

    private Integer[] values;


    @JsonCreator
    public IntArrayAuth(
            @JsonProperty("authToken") String authToken,
            @JsonProperty("values") Integer[] values
    ) {
        super(authToken);
        this.values = values;
    }


    public Integer[] getValues() {
        return values;
    }

    public void setValues(Integer[] values) {
        this.values = values;
    }

}

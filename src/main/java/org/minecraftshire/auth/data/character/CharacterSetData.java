package org.minecraftshire.auth.data.character;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.minecraftshire.auth.data.auth.AuthTokenData;


public class CharacterSetData extends AuthTokenData {
    private int id;
    private Boolean favorite;
    private Boolean online;

    @JsonCreator
    public CharacterSetData(
            @JsonProperty("authToken") String authToken,
            @JsonProperty("id") int id,
            @JsonProperty("isFavorite") Boolean isFavorite,
            @JsonProperty("isOnline") Boolean isOnline
    ) {
        super(authToken);

        this.id = id;
        this.favorite = isFavorite;
        this.online = isOnline;
    }

    public Boolean isFavorite() {
        return favorite;
    }

    public Boolean isOnline() {
        return online;
    }

    public int getId() {
        return id;
    }

}

package org.minecraftshire.auth.data.character;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.minecraftshire.auth.data.auth.AuthTokenData;


public class CharacterCreationData extends AuthTokenData {
    private String firstName;
    private String lastName;
    private String owner;  // Username


    @JsonCreator
    public CharacterCreationData(
            @JsonProperty("authToken") String authToken,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName
    ) {
        super(authToken);

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}

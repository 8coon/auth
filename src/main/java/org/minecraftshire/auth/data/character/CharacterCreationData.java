package org.minecraftshire.auth.data.character;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class CharacterCreationData {
    private String firstName;
    private String lastName;
    private String owner;  // Username


    @JsonCreator
    public CharacterCreationData(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            String owner
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.owner = owner;
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

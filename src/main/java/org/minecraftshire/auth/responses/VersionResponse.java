package org.minecraftshire.auth.responses;


public class VersionResponse {

    private String name;
    private String version;
    private String description;


    public VersionResponse(
            String name, String version, String description
    ) {
        this.name = name;
        this.version = version;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

}

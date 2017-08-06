package org.minecraftshire.auth.responses;


public class VersionResponse {

    private String name;
    private String version;
    private String description;
    private String buildDate;


    public VersionResponse(
            String name, String version, String description, String buildDate
    ) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.buildDate = buildDate;
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

    public String getBuildDate() {
        return this.buildDate;
    }

}

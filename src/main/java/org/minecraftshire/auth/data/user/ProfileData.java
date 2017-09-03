package org.minecraftshire.auth.data.user;


import org.minecraftshire.auth.data.character.CharacterData;
import org.minecraftshire.auth.data.session.SessionGeoData;
import org.minecraftshire.auth.data.session.TokenHistoryData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class ProfileData implements RowMapper<ProfileData> {

    private String username;
    private String email;
    private int passwordLength;
    private String avatarUrl;
    private String profileUrl;
    private int totalBalance;
    private int freeBalance;
    private int group;
    private List<SessionGeoData> sessions;
    private List<TokenHistoryData> tokens;
    private List<CharacterData> characters;


    public ProfileData(
            String username,
            String email,
            int passwordLength,
            String avatarHash,
            String avatarContentType,
            String profileUrl,
            int totalBalance,
            int freeBalance,
            int group
    ) {
        this.username = username;
        this.email = email;
        this.passwordLength = passwordLength;
        this.avatarUrl = UserStatusData.getAvatarUrl(username, avatarHash, avatarContentType);
        this.profileUrl = profileUrl;
        this.totalBalance = totalBalance;
        this.freeBalance = freeBalance;
        this.group = group;
    }

    public ProfileData() {};


    @Override
    public ProfileData mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ProfileData(
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getInt("password_length"),
                resultSet.getString("avatar_hash"),
                resultSet.getString("avatar_content_type"),
                null, // Profile URL
                resultSet.getInt("total_balance"),
                resultSet.getInt("free_balance"),
                resultSet.getInt("group")
        );
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
    }

    public int getFreeBalance() {
        return freeBalance;
    }

    public void setFreeBalance(int freeBalance) {
        this.freeBalance = freeBalance;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public List<SessionGeoData> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionGeoData> sessions) {
        this.sessions = sessions;
    }

    public List<TokenHistoryData> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokenHistoryData> tokens) {
        this.tokens = tokens;
    }

    public List<CharacterData> getCharacters() {
        return characters;
    }

    public void setCharacters(List<CharacterData> characters) {
        this.characters = characters;
    }
}

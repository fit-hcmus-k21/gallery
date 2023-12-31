package com.example.gallery.data.models.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Created on 27/10/2023
 */

@Entity(tableName = "users")
public class User  {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private String id;

    @ColumnInfo(name = "fullName")
    private String fullName;

    @ColumnInfo(name = "avatarURL")
    private String avatarURL;

    @ColumnInfo(name = "avatarPath")
    private String avatarPath;

    @ColumnInfo(name = "username")
    private String username;

    // not used
    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "email")
    private String email;

    // not used
    @ColumnInfo(name = "accessToken")
    private String accessToken;

    @ColumnInfo(name = "googleToken")
    private String googleToken;

    @ColumnInfo(name = "facebookToken")
    private String facebookToken;

    @ColumnInfo(name = "fingerprintData")
    private String fingerprintData;

    @ColumnInfo(name = "creationDate")
    private long creationDate;


    // Constructor

    public User(String id, String fullName, String avatarURL, String username, String password, String email, String accessToken, String googleToken, String facebookToken, String fingerprintData) {
        this.id = id;
        this.fullName = fullName;
        this.avatarURL = avatarURL;
        this.username = username;
        this.password = password;
        this.email = email;
        this.accessToken = accessToken;
        this.googleToken = googleToken;
        this.facebookToken = facebookToken;
        this.fingerprintData = fingerprintData;
    }


    public User( ) {

    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    // setters and getters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {

        return fullName;
    }

    public void setFullName(String fullName) {

        this.fullName = fullName == null ? "" : fullName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getFingerprintData() {
        return fingerprintData;
    }

    public void setFingerprintData(String fingerprintData) {
        this.fingerprintData = fingerprintData;
    }
}

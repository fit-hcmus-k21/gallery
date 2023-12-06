package com.example.gallery.data.repositories.models;


/**
 * Created on 28/10/2023
 */

public class Account {

    // attributes
    private String username;
    private String password;
    private String email;
    private String accessToken;
    private String googleToken;
    private String facebookToken;
    private String fingerprintData;

    // constructors

    public Account() {
        // default constructor
    }
    public Account(String username, String password, String email, String accessToken, String googleToken, String facebookToken, String fingerprintData) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.accessToken = accessToken;
        this.googleToken = googleToken;
        this.facebookToken = facebookToken;
        this.fingerprintData = fingerprintData;
    }

    // setters


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public void setFingerprintData(String fingerprintData) {
        this.fingerprintData = fingerprintData;
    }

    // getters

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getGoogleToken() {
        return googleToken;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public String getFingerprintData() {
        return fingerprintData;
    }

    // other methods

    // login with username and password
    public boolean authenticate(String username, String password) {


        return true;
    }

    // login with google
    public boolean authenticateWithGoogle() {


        return true;
    }

    // login with facebook
    public boolean authenticateWithFacebook() {


        return true;
    }

    // login with fingerprint
    public boolean authenticateWithFingerprint() {

        return true;
    }

    // sign up with username and password
    public boolean signUp(String username, String password) {


        return true;
    }

    // sign up with Google
    public boolean signUpWithGoogle() {


        return true;
    }

    // sign up with facebook
    public boolean signUpWithFacebook () {

        return true;
    }

    // add Fingerprint
    public boolean addFingerprint() {


        return true;
    }

}

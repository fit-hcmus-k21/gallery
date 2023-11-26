package com.example.gallery.data.repositories.models;


import com.example.gallery.data.DataManager;

/**
 * Created on 28/10/2023
 */

public class User {

    // attributes
    private String fullName;
    private Account account;
    private Avatar avatar;
    private boolean hasFingerprint ;
    private MediaManager dataManager;

    private static User sInstance;

    // constructors

    public User() {
        // default constructor
    }

    public User(String fullName, Account account, Avatar avatar, boolean hasFingerprint, MediaManager dataManager) {
        this.fullName = fullName;
        this.account = account;
        this.avatar = avatar;
        this.hasFingerprint = hasFingerprint;
        this.dataManager = dataManager;
    }

    // get single instance user
    public static User getInstance() {
        return sInstance;
    }


    // setters

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void setHasFingerprint(boolean hasFingerprint) {
        this.hasFingerprint = hasFingerprint;
    }

    public void setDataManager(MediaManager dataManager) {
        this.dataManager = dataManager;
    }


    // getters

    public String getFullName() {
        return fullName;
    }

    public Account getAccount() {
        return account;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public boolean isHasFingerprint() {
        return hasFingerprint;
    }

    public MediaManager getMediaManager() {
        return dataManager;
    }


    // other methods

    // handle login methods
    public boolean login(String username, String password) {

        return account.authenticate(username, password);
    }

    public boolean loginWithGoogle() {

        return account.authenticateWithGoogle();
    }

    public boolean loginWithFacebook() {

        return account.authenticateWithFacebook();
    }

    public boolean loginWithFingerprint() {

        return account.authenticateWithFingerprint();
    }

    // handle signup methods
    public boolean signUp(String username, String password) {

        return account.signUp(username, password);
    }

    public boolean signUpWithGoogle() {

        return account.signUpWithGoogle();
    }

    public boolean signUpWithFacebook() {


        return account.signUpWithFacebook();
    }

}

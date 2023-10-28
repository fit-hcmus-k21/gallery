package com.example.gallery.data.repositories.models;

import java.util.HashMap;

/**
 * Created on 28/10/2023
 */

public class AlbumManager {
    // attributes
    private HashMap<String, Album> defaultAlbums; // favorite, from zalo, facebook, messenger, instagram, camera, download
    private SecureAlbum secureAlbum;
    private BinAlbum binAlbum;
    private HashMap<String, Album> customAlbum;

    // constructors

    public AlbumManager() {
        // default constructor
    }

    public AlbumManager(HashMap<String, Album> defaultAlbums, SecureAlbum secureAlbum, BinAlbum binAlbum, HashMap<String, Album> customAlbum) {
        this.defaultAlbums = defaultAlbums;
        this.secureAlbum = secureAlbum;
        this.binAlbum = binAlbum;
        this.customAlbum = customAlbum;
    }


    // setters

    public void setDefaultAlbums(HashMap<String, Album> defaultAlbums) {
        this.defaultAlbums = defaultAlbums;
    }

    public void setSecureAlbum(SecureAlbum secureAlbum) {
        this.secureAlbum = secureAlbum;
    }

    public void setBinAlbum(BinAlbum binAlbum) {
        this.binAlbum = binAlbum;
    }

    public void setCustomAlbum(HashMap<String, Album> customAlbum) {
        this.customAlbum = customAlbum;
    }


    // getters

    public HashMap<String, Album> getDefaultAlbums() {
        return defaultAlbums;
    }

    public SecureAlbum getSecureAlbum() {
        return secureAlbum;
    }

    public BinAlbum getBinAlbum() {
        return binAlbum;
    }

    public HashMap<String, Album> getCustomAlbum() {
        return customAlbum;
    }


    // other methods

    // add custom album to album manager
    public void addCustomAlbum(Album item) {

        // handle here

    }

    // remove album
    public void removeAlbum(String albumName) {

        // handle here
    }


}

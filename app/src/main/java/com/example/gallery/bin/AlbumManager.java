package com.example.gallery.data.repositories.models;

import java.util.HashMap;

/**
 * Created on 28/10/2023
 */

public class AlbumManager {
    // attributes
    private HashMap<String, com.example.gallery.data.repositories.models.Album> defaultAlbums; // favorite, from zalo, facebook, messenger, instagram, camera, download
    private com.example.gallery.data.repositories.models.SecureAlbum secureAlbum;
    private com.example.gallery.data.repositories.models.BinAlbum binAlbum;
    private HashMap<String, com.example.gallery.data.repositories.models.Album> customAlbum;

    // constructors

    public AlbumManager() {
        // default constructor
    }

    public AlbumManager(HashMap<String, com.example.gallery.data.repositories.models.Album> defaultAlbums, com.example.gallery.data.repositories.models.SecureAlbum secureAlbum, com.example.gallery.data.repositories.models.BinAlbum binAlbum, HashMap<String, com.example.gallery.data.repositories.models.Album> customAlbum) {
        this.defaultAlbums = defaultAlbums;
        this.secureAlbum = secureAlbum;
        this.binAlbum = binAlbum;
        this.customAlbum = customAlbum;
    }


    // setters

    public void setDefaultAlbums(HashMap<String, com.example.gallery.data.repositories.models.Album> defaultAlbums) {
        this.defaultAlbums = defaultAlbums;
    }

    public void setSecureAlbum(com.example.gallery.data.repositories.models.SecureAlbum secureAlbum) {
        this.secureAlbum = secureAlbum;
    }

    public void setBinAlbum(com.example.gallery.data.repositories.models.BinAlbum binAlbum) {
        this.binAlbum = binAlbum;
    }

    public void setCustomAlbum(HashMap<String, com.example.gallery.data.repositories.models.Album> customAlbum) {
        this.customAlbum = customAlbum;
    }


    // getters

    public HashMap<String, com.example.gallery.data.repositories.models.Album> getDefaultAlbums() {
        return defaultAlbums;
    }

    public com.example.gallery.data.repositories.models.SecureAlbum getSecureAlbum() {
        return secureAlbum;
    }

    public com.example.gallery.data.repositories.models.BinAlbum getBinAlbum() {
        return binAlbum;
    }

    public HashMap<String, com.example.gallery.data.repositories.models.Album> getCustomAlbum() {
        return customAlbum;
    }


    // other methods

    // add custom album to album manager
    public void addCustomAlbum(com.example.gallery.data.repositories.models.Album item) {

        // handle here

    }

    // remove album
    public void removeAlbum(String albumName) {

        // handle here
    }


}

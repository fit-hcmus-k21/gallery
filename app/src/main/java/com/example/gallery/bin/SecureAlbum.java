package com.example.gallery.data.repositories.models;

/**
 * Created on 28/10/2023
 */

public class SecureAlbum {
    // attributes
    private boolean hasFingerprint;
    private boolean hasPassword;

    // methods
    public boolean authenticateWithPassword() {

        // handle here

        return true;
    }

    public boolean authenticateWithFingerprint() {

        // handle here

        return true;
    }
}

package com.example.gallery.data.repositories.models;


/**
 * Created on 28/10/2023
 */

public class Avatar {
    private String origin;  // fb, gg or local
    private String url;
    private String path;

    // constructor

    public Avatar(String origin, String url, String path) {
        this.origin = origin;
        this.url = url;
        this.path = path;
    }

    // setters

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }


    // getters


    public String getOrigin() {
        return origin;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }
}

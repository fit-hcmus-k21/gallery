package com.example.gallery.data.repositories.models;

/**
 * Created on 28/10/2023
 */

public class MediaItem {

    // attributes
    private int id;
    private String name;
    private String path;
    private String tag;
    private String description;
    private Metadata metadata;

    // constructors

    // setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }


    // getters


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getTag() {
        return tag;
    }

    public String getDescription() {
        return description;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    // other methods

}

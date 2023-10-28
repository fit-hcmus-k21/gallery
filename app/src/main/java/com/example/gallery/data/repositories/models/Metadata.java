package com.example.gallery.data.repositories.models;

import com.example.gallery.utils.Date;

/**
 * Created on 28/10/2023
 */

public class Metadata {
    // attributes
    private int width;
    private int height;
    private long fileSize;  // in byte unit
    private String fileExtension;
    private Date creationDate;
    private String location;

    // constructors


    // getters and setters

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

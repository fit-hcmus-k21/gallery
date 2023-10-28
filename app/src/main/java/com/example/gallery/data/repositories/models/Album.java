package com.example.gallery.data.repositories.models;

import com.example.gallery.utils.Date;

import java.util.ArrayList;

/**
 * Created on 28/10/2023
 */

public class Album {

    // attributes
    private String name;
    private String description;
    private Date creationDate;
    private long totalSize;
    private ArrayList<Integer> listMediaItems;
    private MediaItem coverPhoto;
    private int quantity;


    // constructors

    public Album() {
        // default constructor
    }

    public Album(String name, String description, Date creationDate, long totalSize, ArrayList<Integer> listMediaItems, MediaItem coverPhoto, int quantity) {
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.totalSize = totalSize;
        this.listMediaItems = listMediaItems;
        this.coverPhoto = coverPhoto;
        this.quantity = quantity;
    }


    // setters

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public void setListMediaItems(ArrayList<Integer> listMediaItems) {
        this.listMediaItems = listMediaItems;
    }

    public void setCoverPhoto(MediaItem coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    // getters

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public ArrayList<Integer> getListMediaItems() {
        return listMediaItems;
    }

    public MediaItem getCoverPhoto() {
        return coverPhoto;
    }

    public int getQuantity() {
        return quantity;
    }


    // other methods

    // add media item to album
    public void addMediaItemToAlbum(int identifier) {

        // handle here

    }

    // remove media item from album
    public void removeMediaItemFromAlbum(int identifier) {

        // handle here

    }


}

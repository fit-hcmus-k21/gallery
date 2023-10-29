package com.example.gallery.data.local.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;


/**
 * Created on 27/10/2023
 */

@Entity(tableName = "media_items",
        foreignKeys = {
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "id",
                    childColumns = "userID",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                        entity = Album.class,
                        parentColumns = "name",
                        childColumns = "albumName"
                )
        },
        primaryKeys = {
            "id", "userID"
        }
)
public class MediaItem {

    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "userID")
    private int userID;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "tag")
    private String tag;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "width")
    private int width;

    @ColumnInfo(name = "height")
    private int height;

    @ColumnInfo(name = "fileSize")
    private long fileSize;

    @ColumnInfo(name = "fileExtension")
    private String fileExtension;

    @ColumnInfo(name = "creationDate")
    private String creationDate;

    @ColumnInfo(name = "location")
    private String location;


    @ColumnInfo(name = "albumName")
    private String albumName;

    // setters and getters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}

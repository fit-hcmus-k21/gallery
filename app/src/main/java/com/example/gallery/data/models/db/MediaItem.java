package com.example.gallery.data.models.db;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import java.io.Serializable;


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
                        childColumns = "albumName",
                        onDelete = ForeignKey.NO_ACTION // Thêm mới ở đây
                )
        },
        primaryKeys = {
            "id", "userID"
        }
)
public class MediaItem implements Serializable {
    // This is used to represent for MediaItem in entites with the atribute typeDisplay which uses for recognize the type to display in recyclerview
    public static final int TYPE_GRID = 1;
    public static final int TYPE_LIST = 2;
    public static final int TYPE_STAGGERED = 3;


    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "userID", index = true)
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

    @ColumnInfo(name = "creationDate") // covert from String to Long, that specify the number of milliseconds since January 1, 1970, 00:00:00 GMT
    private Long creationDate;

    @ColumnInfo(name = "location")
    private String location;


    @ColumnInfo(name = "albumName", index = true)
    private String albumName;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "favorite")
    private boolean favorite; // 0 is not favorite, 1 is favorite

    @ColumnInfo(name = "parentPath")
    private String parentPath;

    @ColumnInfo(name = "lastModified") // covert from String to Long, that specify the number of milliseconds since January 1, 1970, 00:00:00 GMT
    private Long lastModified;

    @ColumnInfo(name = "deletedTs") // Deleted timestamp
    private long deletedTs;

    // Add ignore attribute to display some work

    @Ignore
    private int typeDisplay;

    public int getTypeDisplay() {
        return typeDisplay;
    }

    public void setTypeDisplay(int typeDisplay) {
        this.typeDisplay = typeDisplay;
    }


    // Constructor


    public MediaItem(int id, int userID, String name, String tag, String description, String path,
                     int width, int height, long fileSize, String fileExtension, Long creationDate, String location,
                     String albumName, String url, boolean favorite, String parentPath, Long lastModified, long deletedTs) {
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.tag = tag;
        this.description = description;
        this.path = path;
        this.width = width;
        this.height = height;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
        this.creationDate = creationDate;
        this.location = location;
        this.albumName = albumName;
        this.url = url;
        this.favorite = favorite;
        this.parentPath = parentPath;
        this.lastModified = lastModified;
        this.deletedTs = deletedTs;
    }

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

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public long getDeletedTs() {
        return deletedTs;
    }

    public void setDeletedTs(long deletedTs) {
        this.deletedTs = deletedTs;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null){
            return false;
        }
        if(obj.getClass() != this.getClass()){
            return false;
        }
        MediaItem mediaItem = (MediaItem) obj;
        return mediaItem.getPath() == this.getPath();
    }
}


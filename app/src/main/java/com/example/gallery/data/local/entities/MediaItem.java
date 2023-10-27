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
                    parentColumns = "userID",
                    childColumns = "id",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                        entity = Album.class,
                        parentColumns = "albumName",
                        childColumns = "name"
                )
        }
)
public class MediaItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

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

    @ColumnInfo(name = "userID")
    private int userID;

    @ColumnInfo(name = "albumName")
    private String albumName;

}

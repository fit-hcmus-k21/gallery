package com.example.gallery.data.local.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

/**
 * Created on 27/10/2023
 */

@Entity(tableName = "albums",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userID",
                onDelete = ForeignKey.CASCADE
        )
)
public class Album {

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        private int id;

        @ColumnInfo(name = "name")
        private String name;

        @ColumnInfo(name = "description")
        private String description;

        @ColumnInfo(name = "creationDate")
        private String creationDate;

        @ColumnInfo(name = "coverPhotoPath")
        private String coverPhotoPath;

        @ColumnInfo(name = "userID")
        private int userID;
}

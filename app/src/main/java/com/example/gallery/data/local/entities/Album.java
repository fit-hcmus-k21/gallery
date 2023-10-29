package com.example.gallery.data.local.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
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
        ),
        indices = {@Index(value = "name", unique = true)}
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

        // constructor


        // setters and getters
        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getCreationDate() {
                return creationDate;
        }

        public void setCreationDate(String creationDate) {
                this.creationDate = creationDate;
        }

        public String getCoverPhotoPath() {
                return coverPhotoPath;
        }

        public void setCoverPhotoPath(String coverPhotoPath) {
                this.coverPhotoPath = coverPhotoPath;
        }

        public int getUserID() {
                return userID;
        }

        public void setUserID(int userID) {
                this.userID = userID;
        }
}

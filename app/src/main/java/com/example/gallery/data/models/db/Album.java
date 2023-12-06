package com.example.gallery.data.models.db;


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
        private Long creationDate; // Milisecond

        @ColumnInfo(name = "coverPhotoPath")
        private String coverPhotoPath;

        @ColumnInfo(name = "userID", index = true)
        private String userID;

        @ColumnInfo(name = "path")
        private String path;

        // Add more attributes: Bin attributes, Secure attributes

        @ColumnInfo(name = "deletedTs")
        private long deletedTs;

        // constructor

        public Album() {
        }

        public Album( String name, String description, Long creationDate, String coverPhotoPath, String userID, String path, long deletedTs) {
                this.name = name;
                this.description = description;
                this.creationDate = creationDate;
                this.coverPhotoPath = coverPhotoPath;
                this.userID = userID;
                this.path = path;
                this.deletedTs = deletedTs;
        }

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

        public Long getCreationDate() {
                return creationDate;
        }

        public void setCreationDate(Long creationDate) {
                this.creationDate = creationDate;
        }

        public String getCoverPhotoPath() {
                return coverPhotoPath;
        }

        public void setCoverPhotoPath(String coverPhotoPath) {
                this.coverPhotoPath = coverPhotoPath;
        }

        public String getUserID() {
                return userID;
        }

        public void setUserID(String userID) {
                this.userID = userID;
        }

        public void setAlbumName(String name) {
                this.name = name;
        }

        public String getPath() {
                return path;
        }

        public void setPath(String path) {
                this.path = path;
        }

        public long getDeletedTs() {
                return deletedTs;
        }

        public void setDeletedTs(long deletedTs) {
                this.deletedTs = deletedTs;
        }
}

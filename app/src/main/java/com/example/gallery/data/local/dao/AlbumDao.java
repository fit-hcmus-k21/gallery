package com.example.gallery.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gallery.data.local.entities.Album;

import java.util.List;

/**
 * Created on 27/10/2023
 */

@Dao
public interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Album album);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Album> albums);

    @Query("SELECT * FROM albums WHERE userID = :userID")
    LiveData<List<Album>> getAllAlbumsByUserID (int userID);

    @Query("SELECT id, name, description, creationDate, coverPhotoPath, userID, path, deletedTs FROM albums")
    LiveData<List<Album>> getAllAlbums();

    @Query("DELETE FROM albums WHERE path = :path COLLATE NOCASE")
    void deleteAlbumPath(String path);

    @Query("UPDATE OR REPLACE albums SET coverPhotoPath = :newCoverPhotoPath WHERE path = :path COLLATE NOCASE")
    void updateAlbumCoverPhotoPath(String path, String newCoverPhotoPath);

    @Query("UPDATE albums SET name = :newName, coverPhotoPath = :newCoverPhotoPath, path = :newPath WHERE path = :oldPath COLLATE NOCASE")
    void updateAlbumAfterRename(String oldPath, String newName, String newCoverPhotoPath, String newPath);

    @Query("SELECT coverPhotoPath FROM albums WHERE path = :path COLLATE NOCASE")
    String getAlbumCoverPhotoPath(String path);

    @Query("UPDATE albums SET deletedTs = :deletedTs WHERE path = :path COLLATE NOCASE")
    void updateAlbumDeletedTs(String path, long deletedTs);
}

package com.example.gallery.data.local.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gallery.data.models.db.Album;

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

    @Update
    void update(Album album);

    @Delete
    void delete(Album album);

    @Query("DELETE FROM albums WHERE userID = 22")  // change later
    void deleteAll();

    @Query("SELECT * FROM albums WHERE userID = 22")
    LiveData<List<Album>> loadAll ();
    @Query("SELECT * FROM albums WHERE userID = :userID")
    LiveData<List<Album>> getAllAlbumsByUserID (String userID);

    @Query("SELECT id, name, description, creationDate, coverPhotoPath, userID, path, deletedTs FROM albums WHERE userID = :userID")
    LiveData<List<Album>> getAllAlbums(String userID);

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


//    ---------------------------------------

    @Query("SELECT COUNT(*) FROM albums WHERE userID = :userID")
    LiveData<Integer> getNumberOfAlbums(String userID);


}

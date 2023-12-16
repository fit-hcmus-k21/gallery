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

    @Query("SELECT id, name, description, creationDate, coverPhotoPath, userID, path, deletedTs,isPrivateAlb,password FROM albums WHERE userID = :userID")
    LiveData<List<Album>> getAllAlbums(String userID);

    @Query("DELETE FROM albums WHERE path = :path COLLATE NOCASE")
    void deleteAlbumPath(String path);

    @Query("UPDATE OR REPLACE albums SET coverPhotoPath = :newCoverPhotoPath WHERE userID = :userID AND name = :albumName COLLATE NOCASE")
    void updateAlbumCoverPhotoPath(String userID, String albumName, String newCoverPhotoPath);

    @Query("UPDATE albums SET name = :newName, coverPhotoPath = :newCoverPhotoPath, path = :newPath WHERE path = :oldPath COLLATE NOCASE")
    void updateAlbumAfterRename(String oldPath, String newName, String newCoverPhotoPath, String newPath);

    @Query("SELECT coverPhotoPath FROM albums WHERE path = :path COLLATE NOCASE")
    String getAlbumCoverPhotoPath(String path);

    @Query("UPDATE albums SET deletedTs = :deletedTs WHERE path = :path COLLATE NOCASE")
    void updateAlbumDeletedTs(String path, long deletedTs);


//    ---------------------------------------

    @Query("SELECT COUNT(*) FROM albums WHERE userID = :userID")
    LiveData<Integer> getNumberOfAlbums(String userID);

    @Query("SELECT COUNT(*) FROM albums WHERE userID = :userID AND name = :name COLLATE NOCASE")
    LiveData<Integer> isExistAlbum(String userID, String name);

    @Query("UPDATE OR REPLACE albums SET name = :newAlbumName WHERE userID = :userID AND name = :oldAlbumName COLLATE NOCASE")
    void updateAlbumName(String userID, String oldAlbumName, String newAlbumName);

    @Query("SELECT * FROM albums WHERE userID = :userID AND name = :name COLLATE NOCASE")
    LiveData<Album> getAlbumByAlbumName(String userID, String name);

    @Query("DELETE FROM albums WHERE userID = :userID AND name = :name COLLATE NOCASE")
    void deleteAlbumByName(String userID, String name);

    @Query("SELECT * FROM albums WHERE id = :id")
    LiveData<Album> getAlbumById(int id);
    @Query("SELECT * FROM albums WHERE id = :id")
    Album getAlbumByIdNoLive(int id);


    @Query("UPDATE albums SET isPrivateAlb = :isPrivate, password = :password WHERE userID = :userId and name = :albName")
    void updateAlbumIsPrivate(String userId, String albName, int isPrivate, String password);
}

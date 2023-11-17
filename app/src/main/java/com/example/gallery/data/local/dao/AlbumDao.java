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
    LiveData<List<Album>> loadAllByUserID (int userID);

    @Query("SELECT id, name, description, creationDate, coverPhotoPath, userID, path FROM albums")
    List<Album> loadAllAlbums();

}

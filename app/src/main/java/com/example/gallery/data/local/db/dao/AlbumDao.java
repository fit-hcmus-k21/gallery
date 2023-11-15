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

}

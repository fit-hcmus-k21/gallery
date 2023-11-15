package com.example.gallery.data.local.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gallery.data.AppDataManager;
import com.example.gallery.data.models.db.MediaItem;

import java.util.List;


/**
 * Created on 27/10/2023
 */


@Dao
public interface MediaItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MediaItem mediaItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MediaItem> mediaItems);

    @Update
    void update(MediaItem item);

    @Delete
    void delete(MediaItem item);

    @Query("DELETE  FROM media_items WHERE userID = 22 ") // change later
    void deleteAll();

    @Query("SELECT * FROM media_items WHERE userID = 22 ")  // change later
    LiveData<List<MediaItem>> loadAll();

}

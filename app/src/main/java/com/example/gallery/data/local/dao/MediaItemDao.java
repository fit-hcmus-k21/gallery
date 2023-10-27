package com.example.gallery.data.local.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gallery.data.local.entities.MediaItem;

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

    @Query("SELECT * FROM media_items WHERE userID = :userID")
    LiveData<List<MediaItem>> loadAllByUserID(int userID);

}

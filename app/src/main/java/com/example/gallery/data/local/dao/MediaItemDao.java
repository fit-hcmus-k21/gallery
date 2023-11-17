package com.example.gallery.data.local.dao;


import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    @Query("SELECT COUNT(id) FROM media_items WHERE fileExtension LIKE 'image/png'")
    int getNumberOfMediaItem();

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items")
    List<MediaItem> getAllMediaItems();

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE path = :path")
    List<MediaItem> getMediaFromPath(String path);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE albumName = :albumName")
    List<MediaItem> getMediaFromAlbum(String albumName);

}

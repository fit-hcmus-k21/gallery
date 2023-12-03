package com.example.gallery.data.local.db.dao;


import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    @Delete
    void delete(List<MediaItem> mediaItem);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE userID = :userID")
    LiveData<List<MediaItem>> getAllMediaItemsByUserID(String userID);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE userID = :userID AND deletedTs = 0")
    LiveData<List<MediaItem>> getAllMediaItems(String userID);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE deletedTs = 0 AND path = :path  COLLATE NOCASE")
    LiveData<List<MediaItem>> getMediaFromPath(String path);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE deletedTs = 0 AND favorite = 1")
    LiveData<List<MediaItem>> getFavorites();

    @Query("SELECT COUNT(id) FROM media_items WHERE deletedTs = 0 AND favorite = 1")
    LiveData<Integer> getFavoritesCount();

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE deletedTs < :timestamp AND deletedTs != 0")
    LiveData<List<MediaItem>> getOldRecycleBinMediaItems(Long timestamp);

    @Query("DELETE FROM media_items WHERE path = :path COLLATE NOCASE")
    void deleteMediaItemPath(String path);

    @Query("UPDATE OR REPLACE media_items SET name = :newFileName, path = :newPath, parentPath = :newParentPath WHERE id = :id COLLATE NOCASE")
    void updateMediaItem(int id, String newFileName, String newPath, String newParentPath);

    @Query("UPDATE media_items SET favorite = :favorite WHERE id = :id")
    void updateFavorite(int id, boolean favorite);

    @Query("UPDATE media_items SET favorite = 0")
    void clearAllFavorite();

    @Query("DELETE FROM media_items WHERE deletedTs != 0")
    void clearAllRecycleBin();

    @Query("DELETE FROM media_items WHERE deletedTs != 0 AND id = :id")
    void clearRecycleBinItemWithId(int id);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE albumName = :albumName")
    LiveData<List<MediaItem>> getMediaFromAlbum(String albumName);

    @Query("UPDATE OR REPLACE media_items SET deletedTs = :deletedTs WHERE id = :id")
    void updatedDeletedTs(int id, long deletedTs);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE deletedTs = 0 AND fileExtension = :fileExtension")
    LiveData<List<MediaItem>> getMediaFromExtension(String fileExtension);

    @Query("UPDATE media_items SET tag = :tag WHERE id = :id")
    void updateMediaItemTag(int id, String tag);

    @Query("UPDATE media_items SET description = :description WHERE id = :id")
    void updateMediaItemDescription(int id, String description);
    @Query("UPDATE media_items SET deletedTs = :deletedTs WHERE parentPath = :parentPath")
    void updateMediaItemDeletedTsWithParentPath(String parentPath, long deletedTs);

    @Query("SELECT * FROM media_items WHERE deletedTs = 0")
    LiveData<List<MediaItem>> loadAll();


}


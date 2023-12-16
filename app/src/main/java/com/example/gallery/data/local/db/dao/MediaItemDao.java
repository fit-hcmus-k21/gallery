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

    @Query("SELECT COUNT(*) FROM media_items WHERE userID = :userID")
    LiveData<Integer> getNumberOfMediaItems(String userID);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE userID = :userID AND deletedTs = 0")
    LiveData<List<MediaItem>> getAllMediaItemsByUserID(String userID);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath,previousAlbum FROM media_items WHERE deletedTs = 0 AND userID = :userID ")
    LiveData<List<MediaItem>> getAllMediaItems(String userID);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath,previousAlbum FROM media_items WHERE deletedTs = 0 AND path = :path  COLLATE NOCASE")
    LiveData<List<MediaItem>> getMediaFromPath(String path);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath FROM media_items WHERE deletedTs = 0 AND favorite = 1")
    LiveData<List<MediaItem>> getFavorites();

    @Query("SELECT COUNT(id) FROM media_items WHERE deletedTs = 0 AND favorite = 1")
    LiveData<Integer> getFavoritesCount();

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath,previousAlbum FROM media_items WHERE deletedTs < :timestamp AND deletedTs != 0")
    LiveData<List<MediaItem>> getOldRecycleBinMediaItems(Long timestamp);


    @Query("DELETE FROM media_items WHERE path = :path COLLATE NOCASE")
    void deleteMediaItemPath(String path);

    //--------------
    @Query("DELETE FROM media_items WHERE id = :id COLLATE NONCASE")
    void deleteMediaItemId(int id);

    @Query("UPDATE OR REPLACE media_items SET albumName = :newAlbumName WHERE id = :id COLLATE NONCASE")
    void updateMediaItemAlbum(int id, String newAlbumName);

    @Query("UPDATE OR REPLACE media_items SET deletedTs = :newTime WHERE id = :id COLLATE NONCASE")
    void updateMediaItemDeleteTs(int id, long newTime);

    @Query("UPDATE OR REPLACE media_items SET previousAlbum = :previous WHERE id = :id COLLATE NONCASE")
    void updateMediaPreviousAlbum(int id, String previous);
    //--------
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

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath,previousAlbum FROM media_items WHERE albumName = :albumName")
    LiveData<List<MediaItem>> getMediaFromAlbum(String albumName);

    @Query("UPDATE OR REPLACE media_items SET deletedTs = :deletedTs WHERE id = :id")
    void updatedDeletedTs(int id, long deletedTs);

    @Query("SELECT id, userID, name, tag, description, path, width, height, fileSize, fileExtension, creationDate, location, albumName, url, favorite, deletedTs, parentPath,previousAlbum FROM media_items WHERE deletedTs = 0 AND fileExtension = :fileExtension")
    LiveData<List<MediaItem>> getMediaFromExtension(String fileExtension);

    @Query("UPDATE media_items SET tag = :tag WHERE id = :id")
    void updateMediaItemTag(int id, String tag);

    @Query("UPDATE media_items SET description = :description WHERE id = :id")
    void updateMediaItemDescription(int id, String description);
    @Query("UPDATE media_items SET deletedTs = :deletedTs WHERE parentPath = :parentPath")
    void updateMediaItemDeletedTsWithParentPath(String parentPath, long deletedTs);

    @Query("SELECT * FROM media_items WHERE deletedTs = 0")
    LiveData<List<MediaItem>> loadAll();

    @Query("UPDATE OR REPLACE media_items SET albumName = :newAlbumName WHERE userID = :userID AND albumName = :oldAlbumName COLLATE NOCASE")
    void updateAlbumName(String userID, String oldAlbumName, String newAlbumName);

    @Query("SELECT * FROM media_items WHERE deletedTs = 0 AND userID = :userID AND albumName = :albumName COLLATE NOCASE")
    LiveData<List<MediaItem>> getAllMediaItemsByAlbumName(String userID, String albumName);

    @Query("SELECT * FROM media_items WHERE userID = :userID AND deletedTs != 0 COLLATE NOCASE")
    LiveData<List<MediaItem>> getDeletedMediaItems(String userID);

    @Query("SELECT * FROM media_items WHERE userID = :userID AND deletedTs != 0 AND favorite = :favorite COLLATE NOCASE")
    LiveData<List<MediaItem>> getAllFavoriteMediaItem(String userID, boolean favorite);

    @Query("SELECT media_items.* FROM media_items LEFT JOIN albums ON media_items.albumName = albums.name WHERE media_items.userID = :userID AND albums.isPrivateAlb = 0 AND media_items.deletedTs = 0")
    LiveData<List<MediaItem>> getAllPublicMediaItem(String userID);
}


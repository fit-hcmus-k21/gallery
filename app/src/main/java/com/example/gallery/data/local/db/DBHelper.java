package com.example.gallery.data.local.db;


import androidx.lifecycle.LiveData;

import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;

import java.util.List;

/**
 * Created on 1/11/2023
 */
public interface DBHelper {
    LiveData<List<MediaItem>> getAllMediaItems();
    LiveData<List<Album>> getAllAlbums();

//    void insertUser(User user);
//    void insertMediaItem(MediaItem item);
//    void insertAlbum(Album alb);
//
//    void updateUser(User user);
//    void updateMediaItem(MediaItem item);
//    void updateAlbum(Album alb);



}

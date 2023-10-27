package com.example.gallery.data.local.db;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gallery.App;
import com.example.gallery.data.local.dao.AlbumDao;
import com.example.gallery.data.local.dao.MediaItemDao;
import com.example.gallery.data.local.dao.UserDao;
import com.example.gallery.data.local.entities.Album;
import com.example.gallery.data.local.entities.MediaItem;
import com.example.gallery.data.local.entities.User;

/**
 * Created on 27/10/2023
 */

@Database(entities = {User.class, Album.class, MediaItem.class }, version = 1)
public abstract class GalleryDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract AlbumDao albumDao();
    public abstract MediaItemDao mediaItemDao();

    private static GalleryDatabase sInstance;

    public static GalleryDatabase getInstance() {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(
                    App.getInstance(),
                    GalleryDatabase.class,
                    "Gallery.db"
            ).build();
        }
        return sInstance;
    }
}

package com.example.gallery.data.local.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gallery.App;
import com.example.gallery.data.local.db.dao.AlbumDao;
import com.example.gallery.data.local.db.dao.MediaItemDao;
import com.example.gallery.data.local.db.dao.UserDao;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;

/**
 * Created on 27/10/2023
 */

@Database(entities = {User.class, Album.class, MediaItem.class }, version = 2)
public abstract class GalleryDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract AlbumDao albumDao();
    public abstract MediaItemDao mediaItemDao();

    private static GalleryDatabase sInstance;

    public static synchronized GalleryDatabase getInstance() {
        System.out.println("GalleryDatabase.getInstance");
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(
                    App.getInstance(),
                    GalleryDatabase.class,
                    "Gallery_Database"
                    ).fallbackToDestructiveMigration()
                    .build();
        }
        return sInstance;
    }
    public static synchronized GalleryDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    GalleryDatabase.class,
                    "Gallery_Database"
                    ).fallbackToDestructiveMigration()
                    .build();
        }
        return sInstance;
    }
}

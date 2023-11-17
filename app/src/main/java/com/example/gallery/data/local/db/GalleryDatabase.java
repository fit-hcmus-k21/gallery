package com.example.gallery.data.local.db;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gallery.App;
import com.example.gallery.data.local.dao.AlbumDao;
import com.example.gallery.data.local.dao.MediaItemDao;
import com.example.gallery.data.local.dao.UserDao;
import com.example.gallery.data.local.entities.Album;
import com.example.gallery.data.local.entities.MediaItem;
import com.example.gallery.data.local.entities.User;
import com.example.gallery.data.repositories.models.AlbumFromExternalStorage;
import com.example.gallery.data.repositories.models.MediaItemFromExternalStorage;

import java.util.ArrayList;

/**
 * Created on 27/10/2023
 */

@Database(entities = {User.class, Album.class, MediaItem.class }, version = 11)
public abstract class GalleryDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract AlbumDao albumDao();
    public abstract MediaItemDao mediaItemDao();

    private static GalleryDatabase sInstance;

    public static synchronized GalleryDatabase getInstance() {
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

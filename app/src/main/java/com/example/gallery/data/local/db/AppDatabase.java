package com.example.gallery.data.local.db;


import android.os.Handler;
import android.os.Looper;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gallery.App;
import com.example.gallery.data.local.db.dao.AlbumDao;
import com.example.gallery.data.local.db.dao.MediaItemDao;
import com.example.gallery.data.local.db.dao.UserDao;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;

import java.util.concurrent.Executors;

import javax.annotation.Nonnull;

/**
 * Created on 27/10/2023
 */

@Database(entities = {User.class, Album.class, MediaItem.class }, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract AlbumDao albumDao();
    public abstract MediaItemDao mediaItemDao();

    private static AppDatabase sInstance;

    public static synchronized AppDatabase getInstance() {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(
                    App.getInstance(),
                    AppDatabase.class,
                    "Gallery_Database"
                    ).fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return sInstance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@Nonnull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // execute the background task on the executor
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    // seed db in the bg
                    UserDao userDao = sInstance.userDao();
                    MediaItemDao mediaItemDao = sInstance.mediaItemDao();
                    AlbumDao albumDao = sInstance.albumDao();

                    // do something here, such as fake data for user



                    // post a task to the main thread for UI updates
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // update the UI on the main thread
                        }
                    });
                }
            });
        }
    };

}

package com.example.gallery.data.local.db;

import androidx.lifecycle.LiveData;

import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;

import java.util.List;

public class AppDBHelper implements DBHelper {
    private final AppDatabase mAppDatabase;
    private static AppDBHelper sInstance;

    public AppDBHelper(AppDatabase appDatabase) {
        this.mAppDatabase = appDatabase;
    }

    public static synchronized AppDBHelper getInstance() {
        if (sInstance == null) {
            sInstance = new AppDBHelper(AppDatabase.getInstance());
        }
        return sInstance;
    }

    @Override
    public LiveData<List<MediaItem>> getAllMediaItems() {
        return mAppDatabase.mediaItemDao().loadAll(); // handle later ***
    }

    @Override
    public LiveData<List<Album>> getAllAlbums() {
        return mAppDatabase.albumDao().loadAll(); // handle later ****
    }

    @Override
    public void insertUser(User user) {
        System.out.println("AppDBHelper.insertUser");
        mAppDatabase.userDao().insert(user);
    }

    @Override
    public void insertMediaItem(MediaItem item) {
        mAppDatabase.mediaItemDao().insert(item);
    }

    @Override
    public void insertAlbum(Album alb) {
        mAppDatabase.albumDao().insert(alb);
    }

    @Override
    public void updateUser(User user) {
        mAppDatabase.userDao().update(user);
    }

    @Override
    public void updateMediaItem(MediaItem item) {
//        mAppDatabase.mediaItemDao().update(item);
    }

    @Override
    public void updateAlbum(Album alb) {
        mAppDatabase.albumDao().update(alb);
    }
}

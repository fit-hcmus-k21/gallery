package com.example.gallery.data.local.db.repositories;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.gallery.data.local.db.AppDatabase;
import com.example.gallery.data.local.db.dao.MediaItemDao;
import com.example.gallery.data.models.db.MediaItem;

import java.util.List;
import java.util.concurrent.Executors;

public class MediaItemRepository {
    private MediaItemDao mediaItemDao;
    private LiveData<List<MediaItem>> allMediaItems;

    public MediaItemRepository() {
        mediaItemDao = AppDatabase.getInstance().mediaItemDao();
        allMediaItems = mediaItemDao.loadAll();
    }

    public void insert(MediaItem item) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                mediaItemDao.insert(item);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }

    public void update(MediaItem item) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                mediaItemDao.update(item);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }

    public void delete(MediaItem item) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                mediaItemDao.delete(item);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }

    public void deleteAllMediaItems() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                mediaItemDao.deleteAll();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }

    public LiveData<List<MediaItem>> getAllMediaItems() {
        return allMediaItems;
    }

//    -----------------------------


}

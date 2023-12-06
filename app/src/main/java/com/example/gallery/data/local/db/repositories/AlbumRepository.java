package com.example.gallery.data.local.db.repositories;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.gallery.data.local.db.AppDatabase;
import com.example.gallery.data.local.db.dao.AlbumDao;
import com.example.gallery.data.models.db.Album;

import java.util.List;
import java.util.concurrent.Executors;

public class AlbumRepository {
    private AlbumDao albumDao;
    private LiveData<List<Album>> allAlbums;

    public AlbumRepository() {
        albumDao = AppDatabase.getInstance().albumDao();
        allAlbums = albumDao.loadAll();
    }



    public void insert(Album alb) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                albumDao.insert(alb);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }

    public void update(Album alb) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                albumDao.update(alb);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }

    public void delete(Album alb) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                albumDao.delete(alb);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }

    public void deleteAllAlbums() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                albumDao.deleteAll();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }

    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }

}

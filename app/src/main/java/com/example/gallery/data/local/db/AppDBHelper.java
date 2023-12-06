package com.example.gallery.data.local.db;

import androidx.lifecycle.LiveData;

import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        Executors.newSingleThreadExecutor().execute(() -> {
            System.out.println("AppDBHelper.insertUser");
            mAppDatabase.userDao().insert(user);
        });
    }


    @Override
    public void insertMediaItem(MediaItem item) {
        mAppDatabase.mediaItemDao().insert(item);
    }

    @Override
    public void insertAlbum(Album alb) {
        // Using Executors.newSingleThreadExecutor() to execute the insertion on a background thread
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

                System.out.println("AppDBHelper.insertAlbum: " + alb.getName());
                mAppDatabase.albumDao().insert(alb);
            }
        });
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

    @Override
    public boolean isUserExist(String userID) {
        System.out.println("UserRepository isUserExist");

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Use Callable to return the result
        Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return mAppDatabase.userDao().isUserExist(userID);
            }
        });

        // Shut down the executor to release resources
        executorService.shutdown();

        try {
            // Retrieve the result from the Future
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return false; // Return a default value or throw an exception based on your logic
        }
    }


}

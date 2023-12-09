package com.example.gallery.data.local.db.repositories;

import android.os.Handler;
import android.os.Looper;


import com.example.gallery.data.local.db.AppDatabase;
import com.example.gallery.data.local.db.dao.UserDao;
import com.example.gallery.data.models.db.User;

import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;

    public UserRepository() {
        userDao = AppDatabase.getInstance().userDao();
    }

    public void insert(User user) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                userDao.insert(user);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }

    public void update(User user) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                userDao.update(user);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }

    public void delete(User user) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here
                userDao.delete(user);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });
    }



}

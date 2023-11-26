package com.example.gallery.data.repositories.models.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.App;
import com.example.gallery.data.local.dao.UserDao;
import com.example.gallery.data.local.db.GalleryDatabase;
import com.example.gallery.data.local.entities.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> users;
    private Application application;

    public UserRepository(Application application){
        this.application = application;
        GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(application);
        userDao = galleryDatabase.userDao();
        users = userDao.getAllUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }
    public void insertUser(User user){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDao.insert(user);
            }
        });
    }
    public void deleteUser(User user){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDao.delete(user);
            }
        });
    }
}

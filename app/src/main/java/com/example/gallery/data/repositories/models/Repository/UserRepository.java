package com.example.gallery.data.repositories.models.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.App;
import com.example.gallery.data.local.db.AppDatabase;
import com.example.gallery.data.local.db.dao.UserDao;
import com.example.gallery.data.local.db.GalleryDatabase;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlinx.coroutines.CoroutineScope;

public class UserRepository {
    private UserDao userDao;
//    private LiveData<List<User>> users;
    private Application application;

    private static UserRepository currentUserInstance;

    public static UserRepository getInstance(){
        if(currentUserInstance == null){
            //  System.out.println("get Instance of UserRepository");
            currentUserInstance = new UserRepository(App.getInstance());
        }
        return currentUserInstance;
    }

    public UserRepository(Application application){
        this.application = application;
        //  System.out.println("UserRepository constructor");
        AppDatabase galleryDatabase = AppDatabase.getInstance();
        //  System.out.println("galleryDatabase : " + galleryDatabase);
        userDao = galleryDatabase.userDao();
        //  System.out.println("userDao from UserRepos: " + userDao);
//        users = userDao.getAllUsers();

    }


    public void insertUser(User user){
        //  System.out.println("UserRepository insertUser");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDao.insert(user);
            }
        });
    }

    private class InsertUserTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
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

    public LiveData<User> getAllUserData() {
        return userDao.getAllUserData(AppPreferencesHelper.getInstance().getCurrentUserId());
    }
}

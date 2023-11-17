package com.example.gallery.data.repositories.models;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.gallery.App;
import com.example.gallery.data.local.dao.UserDao;
import com.example.gallery.data.local.db.GalleryDatabase;
import com.example.gallery.data.local.entities.User;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private MutableLiveData<List<User>> users;
    private Application application;

    public UserRepository(Application application){
        this.application = application;
        GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(application);
        userDao = galleryDatabase.userDao();
        users = new MutableLiveData<>();
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }
    public void fetchUsersFromDatabase(){
        new fetchUsersFromDatabaseAsyncTask(userDao, users).execute();
    }
    public void insertUser(User user){
        new insertUserAsyncTask(userDao).execute(user);
    }
    private class fetchUsersFromDatabaseAsyncTask extends android.os.AsyncTask<Void, Void, List<User>>{
        private UserDao userDao;
        private MutableLiveData<List<User>> users;

        public fetchUsersFromDatabaseAsyncTask(UserDao userDao, MutableLiveData<List<User>> users) {
            this.userDao = userDao;
            this.users = users;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            return userDao.getAllUsers();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            if(users != null){
                this.users.setValue(users);
                Log.e("Tag", "onPostExecute: " + users.size());
            }
        }
    }
    private class insertUserAsyncTask extends android.os.AsyncTask<User, Void, Void>{
        private UserDao userDao;

        public insertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }
}

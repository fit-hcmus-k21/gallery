package com.example.gallery.data.repositories.models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.Repository.UserRepository;


import java.util.List;

public class UserViewModel extends AndroidViewModel {
//    private LiveData<List<User>> allUsers;
    private UserRepository userRepository;

    private static UserViewModel currentUserViewModel;

    //    ------------------------------------
    private String _id;
    private LiveData<List<MediaItem>> allMediaItems;
    private LiveData<List<Album>> allAlbums;

    //   ------------------------------------

    public static UserViewModel getInstance(Application application){
        if(currentUserViewModel == null){
            currentUserViewModel = new UserViewModel(application);
        }
        return currentUserViewModel;
    }

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
//        allUsers = userRepository.getUsers();
    }


     public LiveData<User> getAllUserData() {
        return userRepository.getAllUserData();
     }

    public void insertUser(User user){
        userRepository.insertUser(user);
    }

//    ------------------------------------
    public String getUserId(){
        return _id;
    }

    public void setUserId(String id){
        this._id = id;
    }

    public LiveData<List<MediaItem>> getAllMediaItems(){
        return allMediaItems;
    }

    public void setAllMediaItems(LiveData<List<MediaItem>> allMediaItems){
        this.allMediaItems = allMediaItems;
    }

    public LiveData<List<Album>> getAllAlbums(){
        return allAlbums;
    }




}

package com.example.gallery.data.repositories.models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.App;
import com.example.gallery.data.AppDataManager;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
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



    public static UserViewModel getInstance(){
        if(currentUserViewModel == null){
            currentUserViewModel = new UserViewModel(App.getInstance());
        }
        System.out.println("get Instance of UserViewModel:" + currentUserViewModel);

        return currentUserViewModel;
    }

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance();
        System.out.println("on user view model constr: 48");
        allMediaItems = MediaItemRepository.getInstance().getAllMediaItems();
        System.out.println("on user view model constr: 50");

        allAlbums = AlbumRepository.getInstance().getAlbums();
        System.out.println("on user view model constr: 53");
        setUserId(AppPreferencesHelper.getInstance().getCurrentUserId());
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

    public LiveData<Integer> getNumberOfItems(){
        return MediaItemRepository.getInstance().getNumberOfMediaItems();
    }




}

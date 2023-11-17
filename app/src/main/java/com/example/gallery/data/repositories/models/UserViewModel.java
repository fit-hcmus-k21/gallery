package com.example.gallery.data.repositories.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.data.local.entities.User;


import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private LiveData<List<User>> allUsers;
    private UserRepository userRepository;
    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allUsers = new MutableLiveData<>();
    }
    public void fetchUsersFromDatabase(){
        userRepository.fetchUsersFromDatabase();
        allUsers = userRepository.getUsers();
    }
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
    public void insertUser(User user){
        userRepository.insertUser(user);
    }

}

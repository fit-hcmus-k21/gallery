package com.example.gallery.data.repositories.models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.data.local.entities.User;
import com.example.gallery.data.repositories.models.Repository.UserRepository;


import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private LiveData<List<User>> allUsers;
    private UserRepository userRepository;
    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allUsers = userRepository.getUsers();
    }
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
    public void insertUser(User user){
        userRepository.insertUser(user);
    }

}

package com.example.gallery.data.local.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gallery.data.local.entities.User;

import java.util.List;

/**
 * Created on 27/10/2023
 */
import androidx.lifecycle.LiveData;
@Dao
public interface UserDao {

    @Delete
    void delete(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT id, fullName, avatarURL, username, password, email, accessToken, googleToken, facebookToken, fingerprintData FROM users")
    LiveData<List<User>> getAllUsers();


}

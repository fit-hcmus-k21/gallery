package com.example.gallery.data.local.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gallery.data.models.db.User;

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

    @Update
    void update(User user);


    @Query("SELECT id, fullName, avatarURL, email FROM users WHERE id = :id")
    LiveData<User> getAllUserData(String id);


}

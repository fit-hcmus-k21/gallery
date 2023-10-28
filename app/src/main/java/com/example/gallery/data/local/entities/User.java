package com.example.gallery.data.local.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Created on 27/10/2023
 */

@Entity(tableName = "users")
public class User  {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "fullName")
    private String fullName;

    @ColumnInfo(name = "avatarURL")
    private String avatarURL;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "accessToken")
    private String accessToken;

    @ColumnInfo(name = "googleToken")
    private String googleToken;

    @ColumnInfo(name = "facebookToken")
    private String facebookToken;

    @ColumnInfo(name = "fingerprintData")
    private String fingerprintData;

}

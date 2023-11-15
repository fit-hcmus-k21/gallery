package com.example.gallery.data;

import androidx.lifecycle.LiveData;

import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;

import java.util.List;

public class AppDataManager implements DataManager {

    public  AppDataManager() {
    }

    @Override
    public LiveData<List<MediaItem>> getAllMediaItems() {
        return null;
    }

    @Override
    public LiveData<List<Album>> getAllAlbums() {
        return null;
    }

    @Override
    public String getAccessToken() {
        return null;
    }

    @Override
    public void setAccessToken(String accessToken) {

    }

    @Override
    public void setCurrentUserEmail(String email) {

    }

    @Override
    public Long getCurrentUserId() {
        return null;
    }

    @Override
    public void setCurrentUserId(Long userId) {

    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return 0;
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {

    }

    @Override
    public void setCurrentUserName(String userName) {

    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return null;
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {

    }

    @Override
    public void setUserAsLoggedOut() {

    }

    @Override
    public void updateApiHeader(Long userID, String accessToken) {

    }

    @Override
    public void updateUserInfo(Long userID, String fullName, String username, String password, String email, String profilePicUrl, LoggedInMode loggedInMode) {

    }
}

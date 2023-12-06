package com.example.gallery.data;

import androidx.lifecycle.LiveData;

import com.example.gallery.data.local.db.AppDBHelper;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.api.LoginRequest;
import com.example.gallery.data.models.api.LoginResponse;
import com.example.gallery.data.models.api.RegisterRequest;
import com.example.gallery.data.models.api.RegisterResponse;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.remote.ApiHeader;
import com.example.gallery.data.remote.ApiHelper;
import com.example.gallery.data.local.db.DBHelper;
import com.example.gallery.data.local.prefs.PreferencesHelper;
import com.example.gallery.data.remote.AppApiHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;

public class AppDataManager implements DataManager {
    private final ApiHelper mApiHelper;

    private final DBHelper mDbHelper;


    private final PreferencesHelper mPreferencesHelper;

    public  AppDataManager() {
        mApiHelper = AppApiHelper.getInstance();
        mDbHelper = AppDBHelper.getInstance();
        mPreferencesHelper = AppPreferencesHelper.getInstance();
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
        mPreferencesHelper.setCurrentUserEmail(email);
    }

    @Override
    public String getCurrentUserEmail() {
        return mPreferencesHelper.getCurrentUserEmail();
    }

    @Override
    public String getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public void setCurrentUserId(String userId) {
        mPreferencesHelper.setCurrentUserId(userId);
    }

    @Override
    public int getCurrentUserLoggedInMode() {

        return mPreferencesHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
         mPreferencesHelper.setCurrentUserLoggedInMode(mode);

    }

    @Override
    public void setCurrentUserName(String userName) {
        mPreferencesHelper.setCurrentUserName(userName);
    }

    @Override
    public String getCurrentUserName() {
        return mPreferencesHelper.getCurrentUserName();
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
        mPreferencesHelper.setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT);

    }

    @Override
    public void updateApiHeader(String userID, String accessToken) {

    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }

    @Override
    public void updateUserInfo(String userID, String fullName, String username, String accessToken, String email, String profilePicUrl, LoggedInMode loggedInMode) {

    }

    @Override
    public void insertUser(User user) {
//        mAppDatabase.userDao().insert(user);
    }

    @Override
    public Call<LoginResponse> doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest request) {
        return mApiHelper.doFacebookLoginApiCall(request);
    }

    @Override
    public Call<LoginResponse> doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest request) {
        return mApiHelper.doGoogleLoginApiCall(request);
    }

    @Override
    public Call<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest request) {
        return mApiHelper.doServerLoginApiCall(request);
    }

    @Override
    public Call<RegisterResponse> doFacebookRegisterApiCall(@Body RegisterRequest.FacebookRegisterRequest request) {
        return mApiHelper.doFacebookRegisterApiCall(request);
    }

    @Override
    public Call<RegisterResponse> doGoogleRegisterApiCall(@Body RegisterRequest.GoogleRegisterRequest request) {
        return mApiHelper.doGoogleRegisterApiCall(request);
    }

    @Override
    public Call<RegisterResponse> doServerRegisterApiCall(@Body RegisterRequest.ServerRegisterRequest request) {
        return mApiHelper.doServerRegisterApiCall(request);
    }

    @Override
    public void updateAlbum(Album alb) {
        mDbHelper.updateAlbum(alb);
    }


    @Override
    public void insertMediaItem(MediaItem item) {
    }

    @Override
    public void insertAlbum(Album alb) {

    }

    @Override
    public void updateUser(User user) {
    }

    @Override
    public void updateMediaItem(MediaItem item) {
//        mAppDatabase.mediaItemDao().update(item);
    }

    @Override
    public void clearPreferences() {
        mPreferencesHelper.clearPreferences();
    }




}

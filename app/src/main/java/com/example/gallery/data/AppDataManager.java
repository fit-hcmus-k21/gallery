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
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }

    @Override
    public void updateUserInfo(Long userID, String fullName, String username, String accessToken, String email, String profilePicUrl, LoggedInMode loggedInMode) {

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


}

package com.example.gallery.data.local.prefs;

import com.example.gallery.data.DataManager;

public interface PreferencesHelper {
    String getAccessToken();

    void setAccessToken(String accessToken);


    void setCurrentUserEmail(String email);

    Long getCurrentUserId();

    void setCurrentUserId(Long userId);

    int getCurrentUserLoggedInMode();

    void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode);


    void setCurrentUserName(String userName);

    String getCurrentUserProfilePicUrl();

    void setCurrentUserProfilePicUrl(String profilePicUrl);
}

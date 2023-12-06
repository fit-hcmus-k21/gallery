package com.example.gallery.data.local.prefs;

import com.example.gallery.data.DataManager;

public interface PreferencesHelper {
    String getAccessToken();

    void setAccessToken(String accessToken);


    void setCurrentUserEmail(String email);
    String getCurrentUserEmail();

    String getCurrentUserId();

    void setCurrentUserId(String userId);

    int getCurrentUserLoggedInMode();

    void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode);


    void setCurrentUserName(String userName);
    String getCurrentUserName();

    String getCurrentUserProfilePicUrl();

    void setCurrentUserProfilePicUrl(String profilePicUrl);

    void clearPreferences();
}

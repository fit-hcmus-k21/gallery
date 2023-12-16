package com.example.gallery.data;

import com.example.gallery.data.remote.ApiHelper;
import com.example.gallery.data.local.db.DBHelper;
import com.example.gallery.data.local.prefs.PreferencesHelper;

public interface DataManager extends DBHelper, PreferencesHelper, ApiHelper {



    // update this file later ****


    void setUserAsLoggedOut();
    void updateApiHeader(String userID, String accessToken);

    void updateUserInfo (
            String userID,
            String fullName,
            String username,
            String email,
            String accessToken,
            String profilePicUrl,
            LoggedInMode loggedInMode
    );


    enum LoggedInMode {
        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3),
        LOGGED_IN_MODE_FINGERPRINT(4);

        private final int mType;

        LoggedInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }

    }


}

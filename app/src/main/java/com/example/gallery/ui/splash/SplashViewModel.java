package com.example.gallery.ui.splash;

import android.os.AsyncTask;

import com.example.gallery.data.DataManager;
import com.example.gallery.ui.base.BaseViewModel;

/**
 * Created on 1/11/2023
 */
public class SplashViewModel extends BaseViewModel<SplashNavigator> {


    public SplashViewModel() {
    }

    public void startSeeding() {
        // do something here such as seed db
        decideNextActivity();

    }


    private void decideNextActivity() {
        /**
         * if user login before, open main activity
         * else: open login activity
         */


        if (getDataManager().getCurrentUserLoggedInMode() == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            getNavigator().openLoginActivity();
        } else {
            getNavigator().openMainActivity();
        }


    }


}

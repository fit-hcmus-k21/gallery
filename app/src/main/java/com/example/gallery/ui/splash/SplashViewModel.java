package com.example.gallery.ui.splash;


import com.example.gallery.data.DataManager;
import com.example.gallery.data.local.db.AppDBHelper;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.data.repositories.models.Repository.UserRepository;
import com.example.gallery.ui.base.BaseViewModel;

/**
 * Created on 1/11/2023
 */
public class SplashViewModel extends BaseViewModel<SplashNavigator> {


    public SplashViewModel() {
    }

    public void startSeeding() {
        // get data from database local, if not exist, get data from server
        if (AppDBHelper.getInstance().isUserExist(AppPreferencesHelper.getInstance().getCurrentUserId())) {
            // TODO: nothing :))
        } else {
            // TODO: get data from server

            // TODO: save data to database local
            User user = new User();
            user.setId(AppPreferencesHelper.getInstance().getCurrentUserId());

            UserRepository.getInstance().insertUser(user);
        }

    }


    public void decideNextActivity() {
        /**
         * if user login before, open main activity
         * else: open login activity
         */

        // check pref if exist

        //  System.out.println("CurrentUserId() : " + getDataManager().getCurrentUserId());
        //  System.out.println("CurrentUserLoggedInMode(): " + getDataManager().getCurrentUserLoggedInMode());


        if (getDataManager().getCurrentUserLoggedInMode() == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            //  System.out.println("Open login activity from splash");
            getNavigator().openLoginActivity();
        } else {
            //  System.out.println("Start seeding from splash");

            startSeeding();
            //  System.out.println("Open main activity from splash");

            getNavigator().openMainActivity();
        }


    }


}

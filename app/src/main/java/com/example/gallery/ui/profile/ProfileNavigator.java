package com.example.gallery.ui.profile;

import android.app.Activity;

import com.example.gallery.databinding.ProfileBinding;
import com.facebook.CallbackManager;

/**
 * Created on 15/11/2023
 */
public interface ProfileNavigator {

    void openLoginActivity();


    ProfileBinding getmProfileBinding();

    ProfileViewModel getmProfileViewModel() ;




}

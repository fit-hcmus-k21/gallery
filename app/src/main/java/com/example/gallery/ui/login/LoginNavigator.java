package com.example.gallery.ui.login;

import android.app.Activity;

import com.facebook.CallbackManager;

/**
 * Created on 15/11/2023
 */
public interface LoginNavigator {
    void openMainActivity();
    void openRegisterActivity();

    void handleError(Object throwable);

    void login();

    Activity getActivity();

    void openResetPasswordActivity();


    void loginWithGoogle();





}

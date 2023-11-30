package com.example.gallery.ui.resetPassword;
import android.app.Activity;

import com.facebook.CallbackManager;

/**
 * Created on 15/11/2023
 */
public interface ResetPasswordNavigator {
    void openLoginActivity();

    Activity getActivity();

    void handleResetPassword();

    void showNotification() ;


}

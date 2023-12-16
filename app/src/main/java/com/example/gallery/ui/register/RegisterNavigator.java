package com.example.gallery.ui.register;

/**
 * Created on 15/11/2023
 */
public interface RegisterNavigator {
    void openMainActivity();
    void openLoginActivity();

    void handleError(Object throwable);

    void register();

    void signUpWithGoogle();

    void signUpWithFacebook();
}

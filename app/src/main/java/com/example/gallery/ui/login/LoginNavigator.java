package com.example.gallery.ui.login;

/**
 * Created on 15/11/2023
 */
public interface LoginNavigator {
    void openMainActivity();
    void openRegisterActivity();

    void handleError(Object throwable);

    void login();

}

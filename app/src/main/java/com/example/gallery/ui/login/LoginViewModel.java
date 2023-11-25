package com.example.gallery.ui.login;

import android.app.Activity;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;


import com.example.gallery.App;
import com.example.gallery.ui.base.BaseViewModel;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.bolts.Task;
import com.facebook.login.LoginManager;

import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;


/**
 * Created on 15/11/2023
 */

public class LoginViewModel extends BaseViewModel<LoginNavigator> {


    public void login(String email, String password) {
        setIsLoading(true);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(App.getInstance(), "Login success", Toast.LENGTH_SHORT).show();
                    getNavigator().openMainActivity();
                } else {
                    setIsLoading(false);
                    Toast.makeText(App.getInstance(), "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                setIsLoading(false);
                Toast.makeText(App.getInstance(), "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    // Method to perform Facebook login

    public void onFacebookLoginClicked() {
        setIsLoading(true);
        Toast.makeText(App.getInstance(), "logging with facebook ...", Toast.LENGTH_SHORT).show();


//        ---------------------
// Create a callback manager to handle login responses
        CallbackManager callbackManager = CallbackManager.Factory.create();

        // Set up Facebook Login
        LoginManager.getInstance().logInWithReadPermissions(getNavigator().getActivity(),
                Arrays.asList("public_profile", "email"));

        // Register a callback for login result
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Handle successful login
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // Handle canceled login
                    }

                    @Override
                    public void onError(FacebookException error) {
                        // Handle error during login
                    }
                });

    }

    // Handle the Facebook access token to authenticate with your server or perform other actions
    private void handleFacebookAccessToken(AccessToken accessToken) {
        // You can use the access token to authenticate with your server or perform other actions
        // onFacebookLoginSuccess();
//        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

    }

    public void onGoogleLoginClicked() {

    }

    public void onFingerprintLoginClicked() {

    }

    public void onServerLoginClicked() {

//        Toast.makeText(App.getInstance(), "onServerLoginClick", Toast.LENGTH_SHORT).show();
        getNavigator().login();
    }

    public void onRegisterClicked() {
        setIsLoading(true);
        getNavigator().openRegisterActivity();
    }

    public void onForgotPasswordClicked() {
        setIsLoading(true);
        getNavigator().openResetPasswordActivity();
    }

}




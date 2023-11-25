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
        LoginManager.getInstance().logInWithReadPermissions(getNavigator().getActivity(),
                Arrays.asList("email", "public_profile"));
        Toast.makeText(App.getInstance(), "logged with facebook ...", Toast.LENGTH_SHORT).show();

        CallbackManager callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(App.getInstance(), "Login Success", Toast.LENGTH_SHORT).show();

                        Object request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                (object, response) -> {
                                    Toast.makeText(App.getInstance(), "Login Success and get accessToken", Toast.LENGTH_SHORT).show();

                                    String id = object.optString("id");
                                    String name = object.optString("name");
                                    String email = object.optString("email");
                                    String profilePicUrl = object.optJSONObject("picture").optJSONObject("data").optString("url");

                                    Toast.makeText(App.getInstance(), "id: " + id + "\nname: " + name + "\nemail: " + email + "\nprofilePicUrl: " + profilePicUrl, Toast.LENGTH_SHORT).show();

                                    getNavigator().openMainActivity();
                                }).executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(App.getInstance(), "Login Cancelled", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(@NonNull FacebookException e) {
                        Toast.makeText(App.getInstance(), "Login Error", Toast.LENGTH_SHORT).show();

                    }
                }
        );

            getNavigator().openMainActivity();
//        Toast.makeText(App.getInstance(), "come here ...", Toast.LENGTH_SHORT).show();
//        handleFacebookAccessToken(AccessToken.getCurrentAccessToken());

    }

    public void handleFacebookAccessToken(AccessToken token) {
        Log.d("LoginViewModel", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(App.getInstance(), "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(App.getInstance(), "signInWithCredential:failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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




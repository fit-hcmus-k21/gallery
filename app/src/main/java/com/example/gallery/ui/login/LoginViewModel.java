package com.example.gallery.ui.login;

import android.app.Activity;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;


import com.example.gallery.App;
import com.example.gallery.data.AppDataManager;
import com.example.gallery.data.DataManager;
import com.example.gallery.data.local.db.AppDBHelper;
import com.example.gallery.data.local.db.DBHelper;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;
import com.example.gallery.ui.backup.BackupManager;
import com.example.gallery.ui.base.BaseViewModel;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.bolts.Task;
import com.facebook.login.LoginManager;

import com.facebook.login.LoginResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;


/**
 * Created on 15/11/2023
 */

public class LoginViewModel extends BaseViewModel<LoginNavigator> {
    private CallbackManager callbackmanager;

    public void login(String email, String password) {
        setIsLoading(true);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //  System.out.println("in login 60");
                    Toast.makeText(App.getInstance(), "Login success", Toast.LENGTH_SHORT).show();

                    // set login mode to LoggedInMode.LOGGED_IN_MODE_SERVER
                    getDataManager().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                    // set userID
                    getDataManager().setCurrentUserId(mAuth.getCurrentUser().getUid());
                    getDataManager().setCurrentUserEmail(mAuth.getCurrentUser().getEmail());
                    getDataManager().setCurrentUserName(mAuth.getCurrentUser().getDisplayName());

                    //  System.out.println("in login 69");



                    // open main activity
                    startSeeding();
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

    public void startSeeding() {
        // TODO: check if not have data in local db, then fetch data from server
        if (AppDBHelper.getInstance().isUserExist(getDataManager().getCurrentUserId())) {
            //  System.out.println("User" + getDataManager().getCurrentUserId() + " exist");
        } else {
            //  System.out.println("User not exist");
            // fetch data from server

            User user = new User();
            user.setId(getDataManager().getCurrentUserId());
            user.setEmail(getDataManager().getCurrentUserEmail());

            AppDBHelper.getInstance().insertUser(user);

            // TODO: fetch data from server
//            Toast.makeText(App.getInstance(), "Đang đồng bộ dữ liệu, bạn chờ xíu nhé :))", Toast.LENGTH_SHORT).show();
            BackupManager.RestoreCloudToLocal();


        }

    }


    // Method to perform Facebook login

    public void onFacebookLoginClicked() {
        setIsLoading(true);
        Toast.makeText(App.getInstance(), "Logging in with Facebook...", Toast.LENGTH_SHORT).show();

        callbackmanager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(getNavigator().getActivity(),
                Arrays.asList("public_profile", "email"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {


                        //  System.out.println("On Success");

                        AccessToken accessToken = loginResult.getAccessToken();
                        //  System.out.println("accessToken: " + accessToken.toString());

                        handleFacebookAccessToken(accessToken);
                        getNavigator().openMainActivity();

                        setIsLoading(false); // Move setIsLoading(false) inside onSuccess

                    }

                    @Override
                    public void onCancel() {
                        setIsLoading(false); // Move setIsLoading(false) inside onCancel
                        //  System.out.println("On Cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        setIsLoading(false); // Move setIsLoading(false) inside onError
                        //  System.out.println( "On Error");
                    }


                });



    }






    // Handle the Facebook access token to authenticate with your server or perform other actions
    private void handleFacebookAccessToken(AccessToken accessToken) {
        Toast.makeText(App.getInstance(), "Retrieving Information ", Toast.LENGTH_SHORT).show();

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject json, GraphResponse response) {
                if (response.getError() != null) {
                    // handle error
                    //  System.out.println("ERROR");
                } else {
                    //  System.out.println("Success");
                    try {
                        String jsonResult = String.valueOf(json);
                        //  System.out.println("JSON Result" + jsonResult);

                        String strEmail = json.getString("email");
                        String strId = json.getString("id");
                        String strFirstName = json.getString("first_name");
                        String strLastName = json.getString("last_name");

                        // Now you can use strEmail, strId, strFirstName, and strLastName as needed
                        // For example, you can pass these values to your server or update UI elements
                        //  System.out.println( "ID: " + strId + ", Email: " + strEmail + ", FirstName: " + strFirstName + ", LastName: " + strLastName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        request.executeAsync();
    }


    public void onGoogleLoginClicked() {
        getNavigator().loginWithGoogle();
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




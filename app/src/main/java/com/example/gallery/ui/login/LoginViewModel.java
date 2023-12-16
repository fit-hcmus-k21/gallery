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
        Toast.makeText(App.getInstance(), "Đang xử lý ...", Toast.LENGTH_SHORT).show();

        getNavigator().loginWithFacebook();



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




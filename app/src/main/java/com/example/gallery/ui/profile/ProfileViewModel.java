package com.example.gallery.ui.profile;

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
import com.example.gallery.data.DataManager;
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

public class ProfileViewModel extends BaseViewModel<ProfileNavigator> {



    // define methods in ProfileViewModel
    public void logout() {
        System.out.println("logout");
        getDataManager().clearPreferences();
        getNavigator().openLoginActivity();
    }

    public void addImageFromDevice() {
//        getNavigator().openAddImageFromDeviceActivity();
    }

    public void addImageFromCamera() {
//        getNavigator().openAddImageFromCameraActivity();
    }

    public void addImageFromLink() {

    }

    public void backup() {

    }

    public void syncToCloudStorage() {

    }


}




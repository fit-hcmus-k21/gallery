package com.example.gallery.ui.resetPassword;

import android.app.Activity;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;


import com.example.gallery.App;
import com.example.gallery.ui.base.BaseViewModel;

import com.example.gallery.ui.login.LoginActivity;
import com.example.gallery.ui.register.RegisterActivity;
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

public class ResetPasswordViewModel extends BaseViewModel<ResetPasswordNavigator> {


    public void onResetPasswordClicked() {
        Toast.makeText(App.getInstance(), "Reset password clicked", Toast.LENGTH_SHORT).show();
        setIsLoading(true);
           getNavigator().handleResetPassword();
    }

    public void onBackClicked() {
        setIsLoading(true);
        getNavigator().openLoginActivity();
    }

    public void resetPassword(String email) {
        setIsLoading(true);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            setIsLoading(false); // Set loading indicator to false regardless of success or failure

            if (task.isSuccessful()) {
                Toast.makeText(App.getInstance(), "Reset password successful", Toast.LENGTH_SHORT).show();
                getNavigator().showNotification();
            } else {
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Reset password failed";
                Toast.makeText(App.getInstance(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }




}




package com.example.gallery.ui.register;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.example.gallery.App;
import com.example.gallery.data.DataManager;
import com.example.gallery.data.models.api.RegisterRequest;
import com.example.gallery.data.models.api.RegisterResponse;
import com.example.gallery.data.remote.AppApiHelper;
import com.example.gallery.ui.base.BaseViewModel;
import com.example.gallery.utils.ValidateText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import retrofit2.Call;


/**
 * Created on 15/11/2023
 */

public class RegisterViewModel extends BaseViewModel<RegisterNavigator> {


    public void register(String email, String password, String fullname) {
        setIsLoading(true);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            setIsLoading(false); // Set loading indicator to false regardless of success or failure

            if (task.isSuccessful()) {
                Toast.makeText(App.getInstance(), "Registration successful", Toast.LENGTH_SHORT).show();

                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    // Update user profile
                    currentUser.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(fullname)
                            .build()
                    );
                }

                setIsLoading(true);
                getNavigator().openMainActivity();
            } else {
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                Toast.makeText(App.getInstance(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onFbRegisterClick() {
//        setIsLoading(true);

    }

    public void onGoogleRegisterClick() {
//        setIsLoading(true);

    }

    public void onServerRegisterClicked() {

//        Toast.makeText(App.getInstance(), "onServerRegisterClick", Toast.LENGTH_SHORT).show();
        getNavigator().register();
    }

    public void onLoginClicked() {

        setIsLoading(true);
        getNavigator().openLoginActivity();
    }

    public void onBackClicked() {

        setIsLoading(true);
        getNavigator().openLoginActivity();
    }

}




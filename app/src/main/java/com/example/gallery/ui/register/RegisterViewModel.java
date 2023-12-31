package com.example.gallery.ui.register;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.example.gallery.App;
import com.example.gallery.data.AppDataManager;
import com.example.gallery.data.DataManager;
import com.example.gallery.data.local.db.AppDBHelper;
import com.example.gallery.data.local.db.AppDatabase;
import com.example.gallery.data.models.api.RegisterRequest;
import com.example.gallery.data.models.api.RegisterResponse;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.remote.AppApiHelper;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.UserRepository;
import com.example.gallery.data.repositories.models.ViewModel.AlbumViewModel;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;
import com.example.gallery.ui.base.BaseViewModel;
import com.example.gallery.utils.ValidateText;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.concurrent.Executors;

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

                //  System.out.println("Register: 52");
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    // Update user profile

                    // get avatar default from drawable
//                    Uri uri = Uri.parse("android.resource://com.example.gallery/drawable/avatar_default");


                    currentUser.updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(fullname)
                            .build()
                    );
                }

                User user = new User();
                user.setId(mAuth.getCurrentUser().getUid());
                user.setFullName(fullname);
                user.setEmail(email);
                user.setCreationDate(new Date().getTime());


                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();


                DatabaseReference usersRef = database.getReference("users");
                usersRef.child(mAuth.getCurrentUser().getUid() ).child("user_info").setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Ghi dữ liệu thành công
                                  System.out.println("Ghi dữ liệu user_info thành công");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý lỗi
                                  System.out.println("Xử lý lỗi khi ghi dữ liệu user_info" + e.toString());
                            }
                        });

//                set logged in mode in prefs, userID
                getDataManager().setCurrentUserId(mAuth.getCurrentUser().getUid());
                getDataManager().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                getDataManager().setCurrentUserName(fullname);
                getDataManager().setCurrentUserEmail(email);

                // insert user, default albums  to room/
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        // background work here

                        UserRepository.getInstance().insertUser(user);
                       // insert default albums
//                        insertDefaultAlbums(mAuth.getCurrentUser().getUid());

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // UI thread work here
                            }
                        });
                    }
                });

                setIsLoading(true);
                getNavigator().openMainActivity();
            } else {
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                Toast.makeText(App.getInstance(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void insertDefaultAlbums(String userID) {
        // Camera
        Album cameraAlbum = new Album();
        cameraAlbum.setAlbumName("Camera");
        cameraAlbum.setUserID(userID);
        AlbumRepository.getInstance().insert(cameraAlbum);

        // Facebook
        Album facebookAlbum = new Album();
        facebookAlbum.setAlbumName("Facebook");
        facebookAlbum.setUserID(userID);
        AlbumRepository.getInstance().insert(facebookAlbum);

        // Messenger
        Album messengerAlbum = new Album();
        messengerAlbum.setAlbumName("Messenger");
        messengerAlbum.setUserID(userID);
        AlbumRepository.getInstance().insert(messengerAlbum);

        // Instagram
        Album instagramAlbum = new Album();
        instagramAlbum.setAlbumName("Instagram");
        instagramAlbum.setUserID(userID);
        AlbumRepository.getInstance().insert(instagramAlbum);

        // Favorite
        Album favoriteAlbum = new Album();
        favoriteAlbum.setAlbumName("Favorite");
        favoriteAlbum.setUserID(userID);
        AlbumRepository.getInstance().insert(favoriteAlbum);

        // Bin
        Album binAlbum = new Album();
        binAlbum.setAlbumName("Bin");
        binAlbum.setUserID(userID);
        AlbumRepository.getInstance().insert(binAlbum);

        // ScreenShot
        Album screenShotAlbum = new Album();
        screenShotAlbum.setAlbumName("ScreenShot");
        screenShotAlbum.setUserID(userID);
        AlbumRepository.getInstance().insert(screenShotAlbum);

        // Download
        Album downloadAlbum = new Album();
        downloadAlbum.setAlbumName("Download");
        downloadAlbum.setUserID(userID);
        AlbumRepository.getInstance().insert(downloadAlbum);
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

    public void onGoogleSignUpClicked() {
        getNavigator().signUpWithGoogle();
    }

    public void onFacebookSignUpClicked() {
        getNavigator().signUpWithFacebook();
    }

}




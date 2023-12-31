package com.example.gallery;


import android.app.Application;

import com.example.gallery.data.AppDataManager;
import com.example.gallery.data.DataManager;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 27/10/2023
 */
public class App extends Application {
    private static  App sInstance;
    private static Retrofit retrofitInstance;

    private static DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // init retrofit instance
        retrofitInstance = new Retrofit.Builder()
                .baseUrl("https://gallery-android-app-22-default-rtdb.asia-southeast1.firebasedatabase.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        FirebaseApp.initializeApp(this);


    }

    public static App getInstance() {
        return sInstance;
    }

    public static Retrofit getRetrofitInstance() { return retrofitInstance;}

    public static DataManager getDataManager() {
        if (dataManager == null) {
            dataManager = new AppDataManager();
        }
        return dataManager;
    }


}

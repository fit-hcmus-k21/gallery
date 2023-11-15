package com.example.gallery;


import android.app.Application;

import com.example.gallery.data.AppDataManager;
import com.example.gallery.data.DataManager;

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

package com.example.gallery;


import android.app.Application;

/**
 * Created on 27/10/2023
 */
public class App extends Application {
    private static  App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static App getInstance() {
        return sInstance;
    }
}

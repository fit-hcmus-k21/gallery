package com.example.gallery.data.remote;

import com.example.gallery.App;
import com.example.gallery.data.models.api.LoginRequest;
import com.example.gallery.data.models.api.LoginResponse;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import retrofit2.Call;

/**
 * Created on 16/11/2023

 */

public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;
    private ApiService mApiService;

    private static AppApiHelper mApiHelper;

    public AppApiHelper() {
        mApiHeader = ApiHeader.getInstance();
        mApiService = App.getRetrofitInstance().create(ApiService.class);
    }

    @Override
    public Call<LoginResponse> doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest request) {
        // send request with retrofit
        return mApiService.doFacebookLoginApiCall(request);
    }

    @Override
    public Call<LoginResponse> doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest request) {
        return mApiService.doGoogleLoginApiCall(request);
    }



    @Override
    public Call<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest request) {
        return mApiService.doServerLoginApiCall(request);
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }

    public static AppApiHelper getInstance() {
        if (mApiHelper == null) {
            mApiHelper = new AppApiHelper();
        }
        return mApiHelper;
    }



}
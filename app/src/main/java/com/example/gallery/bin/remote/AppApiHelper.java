package com.example.gallery.data.remote;

import com.example.gallery.App;
import com.example.gallery.data.models.api.LoginRequest;
import com.example.gallery.data.models.api.LoginResponse;

import com.example.gallery.data.models.api.RegisterRequest;
import com.example.gallery.data.models.api.RegisterResponse;
import com.rx2androidnetworking.Rx2AndroidNetworking;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }

    public static AppApiHelper getInstance() {
        if (mApiHelper == null) {
            mApiHelper = new AppApiHelper();
        }
        return mApiHelper;
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
    public Call<RegisterResponse> doFacebookRegisterApiCall(@Body RegisterRequest.FacebookRegisterRequest request) {
        return mApiService.doFacebookRegisterApiCall(request);
    }

    @Override
    public Call<RegisterResponse> doGoogleRegisterApiCall(@Body RegisterRequest.GoogleRegisterRequest request) {
        return mApiService.doGoogleRegisterApiCall(request);
    }

    @Override
    public Call<RegisterResponse> doServerRegisterApiCall(@Body RegisterRequest.ServerRegisterRequest request) {
        return mApiService.doServerRegisterApiCall(request);
    }




}
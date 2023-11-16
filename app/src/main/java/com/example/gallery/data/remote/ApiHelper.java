package com.example.gallery.data.remote;

import com.example.gallery.data.models.api.LoginRequest;
import com.example.gallery.data.models.api.LoginResponse;

import retrofit2.Call;

/**
 * Created on 15/11/2023
 */
public interface ApiHelper {
    // update this file later ****

        Call<LoginResponse> doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest request);

        Call<LoginResponse> doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest request);

//        Call<LogoutResponse> doLogoutApiCall();

        Call<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest request);

        ApiHeader getApiHeader();



}

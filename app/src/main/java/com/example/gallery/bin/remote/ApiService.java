package com.example.gallery.data.remote;

import com.example.gallery.data.models.api.LoginRequest;
import com.example.gallery.data.models.api.LoginResponse;
import com.example.gallery.data.models.api.RegisterRequest;
import com.example.gallery.data.models.api.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created on 16/11/2023
 */

public interface ApiService {

    // TODO: Add API endpoints here

    // Login APIs
    @POST("login")
    Call<LoginResponse> doFacebookLoginApiCall(@Body LoginRequest.FacebookLoginRequest request);

    @POST("login")
    Call<LoginResponse> doGoogleLoginApiCall(@Body LoginRequest.GoogleLoginRequest request);

    @POST("login")
    Call<LoginResponse> doServerLoginApiCall(@Body LoginRequest.ServerLoginRequest request);

    // Register APIs

    @POST("register")
    Call<RegisterResponse> doFacebookRegisterApiCall(@Body RegisterRequest.FacebookRegisterRequest request);

    @POST("register")
    Call<RegisterResponse> doGoogleRegisterApiCall(@Body RegisterRequest.GoogleRegisterRequest request);

    @POST("register")
    Call<RegisterResponse> doServerRegisterApiCall(@Body RegisterRequest.ServerRegisterRequest request);


}

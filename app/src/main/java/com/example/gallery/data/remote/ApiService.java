package com.example.gallery.data.remote;

import com.example.gallery.data.models.api.LoginRequest;
import com.example.gallery.data.models.api.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> doFacebookLoginApiCall(@Body LoginRequest.FacebookLoginRequest request);

    @POST("login")
    Call<LoginResponse> doGoogleLoginApiCall(@Body LoginRequest.GoogleLoginRequest request);

    @POST("login")
    Call<LoginResponse> doServerLoginApiCall(@Body LoginRequest.ServerLoginRequest request);
}

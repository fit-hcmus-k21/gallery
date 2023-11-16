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

import retrofit2.Call;


/**
 * Created on 15/11/2023
 */

public class RegisterViewModel extends BaseViewModel<RegisterNavigator> {


    public boolean isUsernameAndPasswordValid(String username, String password) {
        // validate username and password
        if (TextUtils.isEmpty(username)) {
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    public void register(String username, String password) {

        Toast.makeText(App.getInstance(), "handle register", Toast.LENGTH_SHORT).show();

        Call<RegisterResponse> call = AppApiHelper.getInstance().doServerRegisterApiCall(new RegisterRequest.ServerRegisterRequest(username, password));
        call.enqueue(new retrofit2.Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(App.getInstance(), "Register success", Toast.LENGTH_SHORT).show();
                    RegisterResponse loginResponse = response.body();
                    getDataManager().updateUserInfo(
                            loginResponse.getUserId(),
                            loginResponse.getFullName(),
                            loginResponse.getAccessToken(),
                            loginResponse.getUserName(),
                            loginResponse.getUserEmail(),
                            loginResponse.getGoogleProfilePicUrl(),
                            DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);

                    getNavigator().openMainActivity();
                } else {
                    Toast.makeText(App.getInstance(), "Register failed", Toast.LENGTH_SHORT).show();

                    // print error
                    Log.e("RegisterViewModel", "onResponse: " + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                getNavigator().handleError(t);
            }
        });
    }

    public void onFbRegisterClick() {
//        setIsLoading(true);
//        getCompositeDisposable().add(getDataManager()
//                .doFacebookRegisterApiCall(new RegisterRequest.FacebookRegisterRequest("test3", "test4"))
//                .doOnSuccess(response -> getDataManager()
//                        .updateUserInfo(
//                                response.getAccessToken(),
//                                response.getUserId(),
//                                DataManager.LoggedInMode.LOGGED_IN_MODE_FB,
//                                response.getUserName(),
//                                response.getUserEmail(),
//                                response.getGoogleProfilePicUrl()))
//                .subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(response -> {
//                    setIsLoading(false);
//                    getNavigator().openMainActivity();
//                }, throwable -> {
//                    setIsLoading(false);
//                    getNavigator().handleError(throwable);
//                }));
    }

    public void onGoogleRegisterClick() {
//        setIsLoading(true);
//        getCompositeDisposable().add(getDataManager()
//                .doGoogleRegisterApiCall(new RegisterRequest.GoogleRegisterRequest("test1", "test1"))
//                .doOnSuccess(response -> getDataManager()
//                        .updateUserInfo(
//                                response.getAccessToken(),
//                                response.getUserId(),
//                                DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
//                                response.getUserName(),
//                                response.getUserEmail(),
//                                response.getGoogleProfilePicUrl()))
//                .subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(response -> {
//                    setIsLoading(false);
//                    getNavigator().openMainActivity();
//                }, throwable -> {
//                    setIsLoading(false);
//                    getNavigator().handleError(throwable);
//                }));
    }

    public void onServerRegisterClicked() {

        Toast.makeText(App.getInstance(), "onServerRegisterClick", Toast.LENGTH_SHORT).show();
        getNavigator().register();
    }

    public void onLoginClicked() {
        getNavigator().openLoginActivity();
    }

    public void onBackClicked() {
        getNavigator().openLoginActivity();
    }

}




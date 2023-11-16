package com.example.gallery.ui.login;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.gallery.App;
import com.example.gallery.data.DataManager;
import com.example.gallery.data.models.api.LoginRequest;
import com.example.gallery.data.models.api.LoginResponse;
import com.example.gallery.data.remote.AppApiHelper;
import com.example.gallery.ui.base.BaseViewModel;

import retrofit2.Call;


/**
 * Created on 15/11/2023
 */

public class LoginViewModel extends BaseViewModel<LoginNavigator> {
    public LoginViewModel() {
    }


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

    public void login(String username, String password) {
        Toast.makeText(App.getInstance(), "handle login", Toast.LENGTH_SHORT).show();

        Call<LoginResponse> call = AppApiHelper.getInstance().doServerLoginApiCall(new LoginRequest.ServerLoginRequest(username, password));
        call.enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(App.getInstance(), "Login success", Toast.LENGTH_SHORT).show();
                    LoginResponse loginResponse = response.body();
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
                    Toast.makeText(App.getInstance(), "Login failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                getNavigator().handleError(t);
            }
        });
    }

    public void onFbLoginClick() {
//        setIsLoading(true);
//        getCompositeDisposable().add(getDataManager()
//                .doFacebookLoginApiCall(new LoginRequest.FacebookLoginRequest("test3", "test4"))
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

    public void onGoogleLoginClick() {
//        setIsLoading(true);
//        getCompositeDisposable().add(getDataManager()
//                .doGoogleLoginApiCall(new LoginRequest.GoogleLoginRequest("test1", "test1"))
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

    public void onServerLoginClick() {

        Log.e("LoginViewModel", "onServerLoginClick");
        Toast.makeText(App.getInstance(), "onServerLoginClick", Toast.LENGTH_SHORT).show();

        getNavigator().login();
    }

}




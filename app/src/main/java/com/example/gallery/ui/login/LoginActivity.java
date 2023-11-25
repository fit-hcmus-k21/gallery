package com.example.gallery.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.lifecycle.ViewModelProvider;

import com.example.gallery.BR;
import com.example.gallery.R;
import com.example.gallery.databinding.Slide02LoginScreenBinding;
import com.example.gallery.ui.base.BaseActivity;
import com.example.gallery.ui.main.MainActivity;
import com.example.gallery.ui.main.Slide07_PhotosGridviewScreenActivity;
import com.example.gallery.ui.register.RegisterActivity;
import com.example.gallery.ui.resetPassword.ResetPasswordActivity;
import com.example.gallery.utils.ValidateText;
import com.facebook.CallbackManager;

/**
 * Created on 15/11/2023
 */


public class LoginActivity extends BaseActivity<Slide02LoginScreenBinding, LoginViewModel> implements LoginNavigator {

    private Slide02LoginScreenBinding mLoginBinding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.slide02_login_screen;
    }

    @Override
    public void handleError(Object throwable) {
        // handle error
    }

    @Override
    public Activity getActivity() {
        return this;
    }






    @Override
    public void login() {
        String email = mLoginBinding.textEmail.getText().toString();
        String password = mLoginBinding.textPassword.getText().toString();

        // toast to show email and password
//        Toast.makeText(this, "Email: " + email + "\nPassword: " + password, Toast.LENGTH_SHORT).show();


        if (ValidateText.isEmailAndPasswordValid(email, password)) {
//            View view = this.getCurrentFocus();
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            mViewModel.login(email, password);
        } else {
            Toast.makeText(this, "Invalid email/ password !" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Toast.makeText(this, "LoginActivity", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        mLoginBinding = getViewDataBinding();


        // Inside LoginActivity
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        if (mViewModel != null) {
            Log.e("LoginActivity", "mViewModel is not null");
            mViewModel.setNavigator(this);

        } else {
            // print to log
            Log.e("LoginActivity", "mViewModel is null");

        }

        mLoginBinding.setViewModel(mViewModel);

        mViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                // Show the loading indicator
                mLoginBinding.progressBar.setVisibility(android.view.View.VISIBLE);
            } else {
                // Hide the loading indicator
                mLoginBinding.progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void openRegisterActivity() {
        // open register activity
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void openResetPasswordActivity() {
        // open reset password activity
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }



}


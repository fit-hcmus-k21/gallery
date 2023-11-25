package com.example.gallery.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.gallery.BR;
import com.example.gallery.R;
import com.example.gallery.databinding.Slide03RegisterScreenBinding;
import com.example.gallery.ui.base.BaseActivity;
import com.example.gallery.ui.login.LoginActivity;
import com.example.gallery.ui.main.MainActivity;
import com.example.gallery.utils.ValidateText;

/**
 * Created on 16/11/2023
 */


public class RegisterActivity extends BaseActivity<Slide03RegisterScreenBinding, RegisterViewModel> implements RegisterNavigator {

    private Slide03RegisterScreenBinding mRegisterBinding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.slide03_register_screen;
    }

    @Override
    public void handleError(Object throwable) {
        // handle error
    }

    @Override
    public void register() {
//        set is loading
        mRegisterBinding.progressBar.setVisibility(android.view.View.VISIBLE);

        String email = mRegisterBinding.textEmail.getText().toString();
        String password = mRegisterBinding.textPassword.getText().toString();
        String confirmPassword = mRegisterBinding.textConfirmPassword.getText().toString();
        String fullname = mRegisterBinding.textFullname.getText().toString();

        // check if password and confirm password are the same
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and confirm password are not the same", Toast.LENGTH_SHORT).show();
            mRegisterBinding.progressBar.setVisibility(View.GONE);
            return;
        }

        // validate email and password
        if (ValidateText.isEmailAndPasswordValid(email, password)) {

            mViewModel.register(email, password, fullname);
        } else {
            Toast.makeText(this, "Invalid email/ password !" , Toast.LENGTH_SHORT).show();
        }
        mRegisterBinding.progressBar.setVisibility(View.GONE);


    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Toast.makeText(this, "RegisterActivity", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        mRegisterBinding = getViewDataBinding();


        // Inside RegisterActivity
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        if (mViewModel != null) {
            Log.e("RegisterActivity", "mViewModel is not null");
            mViewModel.setNavigator(this);

        } else {
            // print to log
            Log.e("RegisterActivity", "mViewModel is null");

        }

        mRegisterBinding.setViewModel(mViewModel);

        mViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                // Show the loading indicator
                mRegisterBinding.progressBar.setVisibility(android.view.View.VISIBLE);
            } else {
                // Hide the loading indicator
                mRegisterBinding.progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void openLoginActivity() {
        // open login activity
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}


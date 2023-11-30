package com.example.gallery.ui.resetPassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.lifecycle.ViewModelProvider;

import com.example.gallery.BR;
import com.example.gallery.R;
import com.example.gallery.databinding.Slide02ResetPasswordScreenBinding;
import com.example.gallery.ui.base.BaseActivity;
import com.example.gallery.ui.login.LoginActivity;

import com.example.gallery.utils.ValidateText;

/**
 * Created on 15/11/2023
 */


public class ResetPasswordActivity extends BaseActivity<Slide02ResetPasswordScreenBinding, ResetPasswordViewModel> implements ResetPasswordNavigator {

    private Slide02ResetPasswordScreenBinding mResetPassBinding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.slide02_reset_password_screen;
    }


    @Override
    public Activity getActivity() {
        return this;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Toast.makeText(this, "ResetPasswordActivity", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        mResetPassBinding = getViewDataBinding();


        // Inside ResetPasswordActivity
        mViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);

        if (mViewModel != null) {
            Log.e("ResetPasswordActivity", "mViewModel is not null");
            mViewModel.setNavigator(this);

        } else {
            // print to log
            Log.e("ResetPasswordActivity", "mViewModel is null");

        }

        mResetPassBinding.setViewModel(mViewModel);

        mViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                // Show the loading indicator
                mResetPassBinding.progressBar.setVisibility(android.view.View.VISIBLE);
            } else {
                // Hide the loading indicator
                mResetPassBinding.progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void openLoginActivity() {
        // open login activity
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void handleResetPassword() {
        String email = mResetPassBinding.editTextEmail.getText().toString().trim();

        if (ValidateText.isEmailValid(email)) {
            mViewModel.resetPassword(email);
        } else {
            Toast.makeText(this, "Invalid email !" , Toast.LENGTH_SHORT).show();
        }
        mResetPassBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNotification() {
        String message = "Please check your email to reset password";
        mResetPassBinding.textViewNotify.setText(message);
        mResetPassBinding.textViewNotify.setVisibility(View.VISIBLE);
    }


}


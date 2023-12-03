package com.example.gallery.ui.login;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.app.Activity;
import android.content.Intent;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.lifecycle.ViewModelProvider;

import com.example.gallery.BR;
import com.example.gallery.R;
import com.example.gallery.databinding.Slide02LoginScreenBinding;
import com.example.gallery.ui.base.BaseActivity;
import com.example.gallery.ui.main.doing.MainActivity;
import com.example.gallery.ui.main.Slide07_PhotosGridviewScreenActivity;
import com.example.gallery.ui.register.RegisterActivity;
import com.example.gallery.ui.resetPassword.ResetPasswordActivity;
import com.example.gallery.utils.ValidateText;
import com.facebook.CallbackManager;
import com.google.android.gms.common.SignInButton;

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



        // set google sign in button text
//        setGooglePlusButtonText(mLoginBinding.buttonGoogleLogin, "Continue with Google");


//        BiometricManager biometricManager = BiometricManager.from(this);
//        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
//            case BiometricManager.BIOMETRIC_SUCCESS:
//                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
//                mLoginBinding.buttonFingerprintLogin.setVisibility(View.VISIBLE);
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//                Log.e("MY_APP_TAG", "No biometric features available on this device.");
////                break;
//            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
////                break;
//            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                Toast.makeText(this, "Please setup fingerprint or face ID", Toast.LENGTH_SHORT).show();
//                mLoginBinding.buttonFingerprintLogin.setVisibility(View.GONE);
//                break;
//            // Prompts the user to create credentials that your app accepts.
////                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
////                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
////                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
////                startActivityForResult(enrollIntent, REQUEST_CODE);
////                break;
//        }

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

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setTextSize(12);
                return;
            }
        }
    }



}


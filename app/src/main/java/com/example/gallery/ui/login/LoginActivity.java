package com.example.gallery.ui.login;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.content.IntentSender;
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
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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


    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;




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

            //--------------------------------
            // config for one tap sign-in


            oneTapClient = Identity.getSignInClient(this);
            signInRequest = BeginSignInRequest.builder()
                    .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                            .setSupported(true)
                            .build())
                    .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            // Your server's client ID, not your Android client ID.
                            .setServerClientId(getString(R.string.default_web_client_id))
                            // Only show accounts previously used to sign in.
                            .setFilterByAuthorizedAccounts(true)
                            .build())
                    // Automatically sign in when exactly one credential is retrieved.
                    .setAutoSelectEnabled(true)
                    .build();
            // ...


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




//    ----------------------------------------

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    // ...



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        try {
            SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
            String idToken = googleCredential.getGoogleIdToken();
            if (idToken != null) {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    System.out.println("Auth with google ok !");
                                    // ...


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());

                                    //...
                                }
                            }
                        });

            }
        }
            catch (ApiException e) {
                                    // ...
                    switch (e.getStatusCode()) {
                        case CommonStatusCodes.CANCELED:
                            Log.d(TAG, "One-tap dialog was closed.");
                            // Don't re-prompt the user.
                            showOneTapUI = false;
                            break;
                        case CommonStatusCodes.NETWORK_ERROR:
                            Log.d(TAG, "One-tap encountered a network error.");
                            // Try again or just ignore.
                            break;
                        default:
                            Log.d(TAG, "Couldn't get credential from result."
                                    + e.getLocalizedMessage());
                            break;
                    }
            }

    }

    @Override
    public void loginWithGoogle() {
        System.out.println("Login with google");

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                            System.out.println("Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
//                        Log.d(TAG, e.getLocalizedMessage());
                        System.out.println("No saved credentials found. Launch the One Tap sign-up flow, or do nothing and continue presenting the signed-out UI.");
                        System.out.println("error: " + (e.getLocalizedMessage()));
                        Toast.makeText(LoginActivity.this, "Please sign up first to use sign in one tap!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}


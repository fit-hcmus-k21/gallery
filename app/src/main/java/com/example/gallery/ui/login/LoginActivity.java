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
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.lifecycle.ViewModelProvider;

import com.example.gallery.App;
import com.example.gallery.BR;
import com.example.gallery.R;
import com.example.gallery.data.DataManager;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.Repository.UserRepository;
import com.example.gallery.databinding.Slide02LoginScreenBinding;
import com.example.gallery.ui.base.BaseActivity;
import com.example.gallery.ui.main.doing.MainActivity;
import com.example.gallery.ui.main.Slide07_PhotosGridviewScreenActivity;
import com.example.gallery.ui.register.RegisterActivity;
import com.example.gallery.ui.resetPassword.ResetPasswordActivity;
import com.example.gallery.utils.ValidateText;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created on 15/11/2023
 */


public class LoginActivity extends BaseActivity<Slide02LoginScreenBinding, LoginViewModel> implements LoginNavigator {


    private CallbackManager mCallbackManager;

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
                            .setFilterByAuthorizedAccounts(false)
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
    // ...



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("onActivityResult");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (requestCode != REQ_ONE_TAP) {
            System.out.println("requestCode: " + requestCode + " data: " + data);
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
            return;
        }

        // requestCode == GOOGLE_LOGIN_REQUEST_CODE
        try {
            System.out.println("requestCode: " + requestCode + " data: " + data);
            SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
            System.out.println("googleCredential: " + googleCredential);
            String idToken = googleCredential.getGoogleIdToken();
//            System.out.println("idToken: " + idToken);
            if (idToken != null) {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                        // Nếu đây là lần đăng nhập đầu tiên, thực hiện các bước tạo tài khoản mới ở đây
                                        // Ví dụ: Lưu thông tin mới vào cơ sở dữ liệu
                                        System.out.println("New user");

                                        FirebaseUser userFirebase = mAuth.getCurrentUser();
                                        System.out.println("Auth with google ok !");
                                        // ...


                                        User user = new User();
                                        user.setId(mAuth.getCurrentUser().getUid());
                                        user.setFullName(userFirebase.getDisplayName());
                                        user.setEmail(userFirebase.getEmail());
                                        user.setAvatarURL(userFirebase.getPhotoUrl().toString());
                                        user.setCreationDate(new Date().getTime());


                                        // Write a message to the database
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();


                                        DatabaseReference usersRef = database.getReference("users");
                                        usersRef.child(mAuth.getCurrentUser().getUid() ).child("user_info").setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Ghi dữ liệu thành công
                                                        System.out.println("Ghi dữ liệu user_info thành công");

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Xử lý lỗi
                                                        System.out.println("Xử lý lỗi khi ghi dữ liệu user_info" + e.toString());
                                                    }
                                                });

                                        //                set logged in mode in prefs, userID
                                        AppPreferencesHelper.getInstance().setCurrentUserId(mAuth.getCurrentUser().getUid());
                                        AppPreferencesHelper.getInstance().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE);
                                        AppPreferencesHelper.getInstance().setCurrentUserName(userFirebase.getDisplayName());
                                        AppPreferencesHelper.getInstance().setCurrentUserEmail(userFirebase.getEmail());

                                        // insert user, default albums  to room/
                                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                // background work here

                                                UserRepository.getInstance().insertUser(user);
                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // UI thread work here
                                                    }
                                                });
                                            }
                                        });

                                        mViewModel.setIsLoading(true);
                                        Toast.makeText(App.getInstance(), "Sign up with google successfully !", Toast.LENGTH_SHORT).show();
                                        System.out.println("Sign up with google successfully 314 !");
                                        openMainActivity();





                        } else {
                                        System.out.println("Old user");

                                        // Nếu đây là lần đăng nhập lại, thực hiện các bước đăng nhập ở đây
                                        // Ví dụ: Hiển thị màn hình chính của ứng dụng

                                        Toast.makeText(App.getInstance(), "Login success", Toast.LENGTH_SHORT).show();

                                        // set login mode to LoggedInMode.LOGGED_IN_MODE_SERVER
                                        AppPreferencesHelper.getInstance().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                                        // set userID
                                        AppPreferencesHelper.getInstance().setCurrentUserId(mAuth.getCurrentUser().getUid());
                                        AppPreferencesHelper.getInstance().setCurrentUserEmail(mAuth.getCurrentUser().getEmail());
                                        AppPreferencesHelper.getInstance().setCurrentUserName(mAuth.getCurrentUser().getDisplayName());

                                        //  System.out.println("in login 69");

                                        // open main activity
                                        mViewModel.startSeeding();
                                        openMainActivity();

                                    }

                                    // ...


                                } else {
                                    // If sign in fails, display a message to the user.
                                    System.out.println(  "signInWithCredential:failure | " + task.getException());

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
                            System.out.println( "One-tap dialog was closed.");
                            // Don't re-prompt the user.
                            break;
                        case CommonStatusCodes.NETWORK_ERROR:
                            System.out.println(  "One-tap encountered a network error.");
                            // Try again or just ignore.
                            break;
                        default:

                            System.out.println("Couldn't get credential from result."
                                    + e.getLocalizedMessage());
                            break;
                    }
            }

    }

    @Override
    public void loginWithGoogle() {
        System.out.println("Login with google");

        // set progress bar visible
        mViewModel.setIsLoading(true);

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        System.out.println("onSuccess");
                        try {
                            mViewModel.setIsLoading(false);

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

    // Handle the Facebook access token to authenticate with your server or perform other actions
    private void handleFacebookAccessToken(AccessToken token) {
        Toast.makeText(App.getInstance(), "Retrieving Information ", Toast.LENGTH_SHORT).show();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (token == null) {
            System.out.println("token is null");
            return;
        } else {
            System.out.println("token is not null");
        }
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        System.out.println("credential: " + credential + " auth: " + mAuth);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("signInWithCredential:success");



                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                // Nếu đây là lần đăng nhập đầu tiên, thực hiện các bước tạo tài khoản mới ở đây
                                // Ví dụ: Lưu thông tin mới vào cơ sở dữ liệu
                                System.out.println("New user");

                                FirebaseUser userFirebase = mAuth.getCurrentUser();
                                System.out.println("Auth with google ok !");
                                // ...
                                Toast.makeText(App.getInstance(), "Authentication success, " + userFirebase.getDisplayName() +".",
                                        Toast.LENGTH_SHORT).show();


                                User user = new User();
                                user.setId(mAuth.getCurrentUser().getUid());
                                user.setFullName(userFirebase.getDisplayName());
                                user.setEmail(userFirebase.getEmail());
                                user.setAvatarURL(userFirebase.getPhotoUrl().toString());
                                user.setCreationDate(new Date().getTime());


                                // Write a message to the database
                                FirebaseDatabase database = FirebaseDatabase.getInstance();


                                DatabaseReference usersRef = database.getReference("users");
                                usersRef.child(mAuth.getCurrentUser().getUid() ).child("user_info").setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Ghi dữ liệu thành công
                                                System.out.println("Ghi dữ liệu user_info thành công");

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Xử lý lỗi
                                                System.out.println("Xử lý lỗi khi ghi dữ liệu user_info" + e.toString());
                                            }
                                        });

                                //                set logged in mode in prefs, userID
                                AppPreferencesHelper.getInstance().setCurrentUserId(mAuth.getCurrentUser().getUid());
                                AppPreferencesHelper.getInstance().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_FB);
                                AppPreferencesHelper.getInstance().setCurrentUserName(userFirebase.getDisplayName());
                                AppPreferencesHelper.getInstance().setCurrentUserEmail(userFirebase.getEmail());

                                // insert user, default albums  to room/
                                Executors.newSingleThreadExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        // background work here

                                        UserRepository.getInstance().insertUser(user);
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                // UI thread work here
                                            }
                                        });
                                    }
                                });

                                mViewModel.setIsLoading(true);
                                Toast.makeText(App.getInstance(), "Sign up with google successfully !", Toast.LENGTH_SHORT).show();
                                System.out.println("Sign up with google successfully 314 !");
                                openMainActivity();





                            } else {
                                System.out.println("Old user");

                                // Nếu đây là lần đăng nhập lại, thực hiện các bước đăng nhập ở đây
                                // Ví dụ: Hiển thị màn hình chính của ứng dụng

                                Toast.makeText(App.getInstance(), "Login success", Toast.LENGTH_SHORT).show();

                                // set login mode to LoggedInMode.LOGGED_IN_MODE_SERVER
                                AppPreferencesHelper.getInstance().setCurrentUserLoggedInMode(DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                                // set userID
                                AppPreferencesHelper.getInstance().setCurrentUserId(mAuth.getCurrentUser().getUid());
                                AppPreferencesHelper.getInstance().setCurrentUserEmail(mAuth.getCurrentUser().getEmail());
                                AppPreferencesHelper.getInstance().setCurrentUserName(mAuth.getCurrentUser().getDisplayName());

                                //  System.out.println("in login 69");

                                // open main activity
                                mViewModel.startSeeding();
                                openMainActivity();

                            }





                        } else {
                            System.out.println("signInWithCredential:failure " + task.getException());
                            // If sign in fails, display a message to the user.
                            Toast.makeText(App.getInstance(), "Authentication failed. " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                });


    }

    @Override
    public void loginWithFacebook() {

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(getActivity(),
                Arrays.asList("public_profile", "email"));

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {


                        AccessToken accessToken = loginResult.getAccessToken();
                        System.out.println("On Success " + accessToken);

                        handleFacebookAccessToken(accessToken);


                    }

                    @Override
                    public void onCancel() {
                        mViewModel.setIsLoading(false); // Move setIsLoading(false) inside onCancel
                        //  System.out.println("On Cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        mViewModel.setIsLoading(false); // Move setIsLoading(false) inside onError
                        System.out.println( "On Error");
                    }


                });
    }

}


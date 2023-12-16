package com.example.gallery.ui.register;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.gallery.App;
import com.example.gallery.BR;
import com.example.gallery.R;
import com.example.gallery.data.DataManager;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.Repository.UserRepository;
import com.example.gallery.databinding.Slide03RegisterScreenBinding;
import com.example.gallery.ui.base.BaseActivity;
import com.example.gallery.ui.login.LoginActivity;
import com.example.gallery.ui.main.doing.MainActivity;
import com.example.gallery.utils.ValidateText;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.concurrent.Executors;

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

    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;

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


        // config for sign up with gg
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();


    }

    @Override
    public void openLoginActivity() {
        // open login activity
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // ------------------  ------------------ //    SIGNUP WITH GOOGLE    ------------------  ------------------ //
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    ActivityResultLauncher<IntentSenderRequest> startIntentSenderForResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), res -> {
                // Xử lý kết quả ở đây
                if (res.getResultCode() == Activity.RESULT_OK) {
                    // Kết quả OK
                    // TODO: Xử lý thành công
                    System.out.println("Sign up with google ok 194 !");
                    SignInCredential credential = null;
                    try {
                        credential = oneTapClient.getSignInCredentialFromIntent(res.getData());
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                    handleOneTapSignInResult(credential);

                } else {
                    // Kết quả không thành công hoặc hủy bỏ
                    // TODO: Xử lý không thành công hoặc hủy bỏ
                    System.out.println("Sign up with google not ok 199 !" + res.getData().toString() + " " + res.getResultCode());
                }
            });
    @Override
    public void signUpWithGoogle() {
        // sign up with google
        showOneTapDialog();

    }

    // Trong phương thức xử lý sự kiện khi người dùng chọn tùy chọn đăng nhập
    public void handleOneTapSignInResult(SignInCredential signUpCredential) {
        System.out.println("come here 208 !");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String idToken = signUpCredential.getGoogleIdToken();
        if (idToken != null) {
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser userFirebase = mAuth.getCurrentUser();
                            System.out.println("Auth with google ok !");

                            // ... Các bước xử lý sau đăng nhập thành công

                            // Tiến hành chuyển hướng hoặc xử lý sau khi đăng nhập thành công
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            System.out.println("Auth with google not ok !");
                        }
                    });
        }
    }

    // Trong phương thức nơi bạn mở One Tap dialog (ví dụ: khi nhấn nút đăng nhập với Google)
    public void showOneTapDialog() {
//        System.out.println("come here 234 !");
        // Cấu hình và hiển thị One Tap dialog

        // set progress bar visible
        mRegisterBinding.progressBar.setVisibility(android.view.View.VISIBLE);

        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this, result -> {
                    mRegisterBinding.progressBar.setVisibility(android.view.View.GONE);

                        // Mở One Tap dialog và chờ người dùng chọn
                        startIntentSenderForResultLauncher.launch(new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build());

                })
                .addOnFailureListener(this, e -> {
                    // Xử lý lỗi khi không tìm thấy thông tin đăng nhập
                    System.out.println("No saved credentials found. Launch the One Tap sign-up flow, or do nothing and continue presenting the signed-out UI.");
                    System.out.println("error: " + (e.getLocalizedMessage()));
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // set progress bar gone
        System.out.println("come here 252!");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        try {
            System.out.println("inside try block 257!");
            SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
            String idToken = googleCredential.getGoogleIdToken();

            if (idToken != null) {
//                System.out.println("inside try block 262: " + idToken + " ");

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
                                    FirebaseUser userFirebase = mAuth.getCurrentUser();
                                    System.out.println("Auth with google ok !");
                                    // ...
                                    // Update user profile
//                                    userFirebase.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
//                                            .setDisplayName(userFirebase.getDisplayName())
//                                            .setPhotoUri(userFirebase.getPhotoUrl())
//                                            .build()
//                                    );

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
                                    // If sign in fails, display a message to the user.
                                    System.out.println( "signInWithCredential:failure "  +  task.getException());
                                    System.out.println("Auth with google not ok 321!");

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
                    System.out.println( "One-tap encountered a network error.");
                    // Try again or just ignore.
                    break;
                default:
                    System.out.println( "Couldn't get credential from result."
                            + e.getLocalizedMessage());
                    break;
            }
        }

    }







    // ------------------  ------------------ //    SIGNUP WITH FACEBOOK    ------------------  ------------------ //
    public void signUpWithFacebook() {
        // sign up with facebook
        //...
    }

}


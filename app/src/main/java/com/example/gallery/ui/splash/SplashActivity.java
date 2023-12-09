package com.example.gallery.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.example.gallery.BR;
import com.example.gallery.R;
import com.example.gallery.databinding.Slide01SplashScreenBinding;
import com.example.gallery.ui.base.BaseActivity;
import com.example.gallery.ui.login.LoginActivity;
import com.example.gallery.ui.main.doing.MainActivity;

public class SplashActivity extends BaseActivity<Slide01SplashScreenBinding, SplashViewModel> implements SplashNavigator {

    private Slide01SplashScreenBinding mSplashBinding;
    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.slide01_splash_screen;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashBinding = getViewDataBinding();
        //  System.out.println("Currently on splash screen");

        // Inside SplashActivity
        mViewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        if (mSplashBinding != null && mViewModel != null) {
            Log.e("SplashActivity", "mSplashBinding and mViewModel are not null");
        } else {
            Log.e("SplashActivity", "mSplashBinding or mViewModel is null");
        }

        mViewModel.setNavigator(this);


        // set transition for gallery icon
        ImageView rotatingImageView = mSplashBinding.galleryIcon;

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000); //  (milliseconds)
//        rotateAnimation.setRepeatMode(Animation.REVERSE);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        rotatingImageView.startAnimation(rotateAnimation);

        new Handler().postDelayed(() -> {
            mSplashBinding.setViewModel(mViewModel);
            mViewModel.decideNextActivity();
        }, 500);







    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

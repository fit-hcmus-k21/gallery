package com.example.gallery.ui.splash;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.gallery.ui.main.MainActivity;

public class SplashActivity extends BaseActivity<Slide01SplashScreenBinding, SplashViewModel> implements SplashNavigator {


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


        // Inside SplashActivity
        mViewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        if (mViewModel != null) {
            Log.e("SplashActivity", "mViewModel is not null");
            mViewModel.setNavigator(this);
            mViewModel.startSeeding();

        } else {
            // print to log
            Log.e("SplashActivity", "mViewModel is null");

        }



        // set transition for gallery icon
        ImageView rotatingImageView = findViewById(R.id.gallery_icon);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000); //  (milliseconds)
//        rotateAnimation.setRepeatMode(Animation.REVERSE);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        rotatingImageView.startAnimation(rotateAnimation);

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

package com.example.gallery.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.gallery.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide01_splash_screen);

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
}

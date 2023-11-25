package com.example.gallery.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.gallery.R;

public class MainActivity  extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, Slide07_PhotosGridviewScreenActivity.class);
        startActivity(intent);
        finish();
    }
}

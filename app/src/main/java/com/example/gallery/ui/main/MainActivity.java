package com.example.gallery.ui.main;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.gallery.R;

public class MainActivity  extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide07_photos_gridview_screen);
    }
}
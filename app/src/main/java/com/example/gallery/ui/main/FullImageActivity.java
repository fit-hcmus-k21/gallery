package com.example.gallery.ui.main;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.gallery.R;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        ImageView fullImageView = findViewById(R.id.fullImageView);

        // Nhận đường dẫn hình ảnh từ Intent
        if (getIntent().hasExtra("image_path")) {
            String imagePath = getIntent().getStringExtra("image_path");

            // Sử dụng thư viện Glide để hiển thị hình ảnh
            Glide.with(this)
                    .load(imagePath)
                    .into(fullImageView);
        }
    }
}


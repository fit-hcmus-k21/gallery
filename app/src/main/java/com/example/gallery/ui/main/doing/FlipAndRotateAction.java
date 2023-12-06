package com.example.gallery.ui.main.doing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gallery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FlipAndRotateAction extends AppCompatActivity implements View.OnClickListener {

    ImageView FAndRImage;
    BottomNavigationView navigationView;
    Button btnSave, btnNotSave;
    Bitmap input = null, root = null;
    int angle = 0;
    int screenWidth, screenHeight;      // giá trị này không được thay đổi trong quá trình điều chỉnh ảnh
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.flip_rotate_layout);
        FAndRImage = (ImageView) findViewById(R.id.FlipAndRotateImage);
        navigationView = (BottomNavigationView) findViewById(R.id.FAndRNavigation);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnNotSave = (Button) findViewById(R.id.btnNotSave);

        Intent intent = getIntent();

        screenWidth = FAndRImage.getMaxWidth();
        screenHeight = FAndRImage.getMaxHeight();
        input = EditActivity.saveImage;
        root = input;
        FAndRImage.setImageBitmap(input);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.btnRotate){
                    angle += 90;
                    if (angle > 360)
                        angle = angle % 360;
                    Matrix matrix = new Matrix();
                    float ratio;
                    if(angle % 180 == 0)
                        ratio = (float)screenHeight/screenWidth;
                    else ratio = (float)screenWidth/screenHeight;

                    matrix.setScale(ratio,ratio);
                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createBitmap(input,0,0,input.getWidth(),input.getHeight(),matrix,true);
                    FAndRImage.setImageBitmap(scaledBitmap);
                    input = scaledBitmap;
                }
                if(item.getItemId() == R.id.btnFlip){
                    Matrix matrix = new Matrix();
                    matrix.setScale(-1,1);
                    Bitmap scaledBitmap = Bitmap.createBitmap(input,0,0,input.getWidth(),input.getHeight(),matrix,true);
                    input = scaledBitmap;
                    FAndRImage.setImageBitmap(scaledBitmap);
                }
                return true;
            }
        });
        btnSave.setOnClickListener(this);
        btnNotSave.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave){
            String path = saveBitmapToStorage(input);
            Intent save = new Intent(FlipAndRotateAction.this,EditActivity.class);
            save.putExtra("path",path);
            setResult(RESULT_OK,save);
            ++EditActivity.time;
            finish();
        }
        if(v.getId() == R.id.btnNotSave){
            String path = saveBitmapToStorage(root);
            Intent save = new Intent(FlipAndRotateAction.this,EditActivity.class);
            save.putExtra("path",path);
            setResult(RESULT_OK,save);

            finish();
        }
    }
    private String saveBitmapToStorage(Bitmap bitmap) {
        String fileName = "image.jpg"; // Tên tệp hình ảnh
        File storageDir = getExternalCacheDir(); // Lấy thư mục tạm ngoài
        File imageFile = new File(storageDir, fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            // Xảy ra lỗi khi lưu tệp hình ảnh
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}

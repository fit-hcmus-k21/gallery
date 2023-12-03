package com.example.gallery.ui.main.doing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gallery.R;
import com.example.gallery.ui.main.adapter.EffectRenderer;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.slider.Slider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL;

public class SharpAction extends AppCompatActivity implements View.OnClickListener {
    public float scale = 1.0f;
    GLSurfaceView view;
    Slider sldSharp;
    Button btnSave,btnNotSave;

    private EffectRenderer render ;
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.sharpen_layout);
        Intent intent = getIntent();

        view = (GLSurfaceView) findViewById(R.id.sharpImage);
        sldSharp = (Slider) findViewById(R.id.sharpSlider);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnNotSave = (Button) findViewById(R.id.btnNotSave);

        btnSave.setOnClickListener(this);
        btnNotSave.setOnClickListener(this);
        view.setEGLContextClientVersion(2);
        render = new EffectRenderer(SharpAction.this,"SHARPEN",EditActivity.saveImage);
        view.setRenderer(render);
        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        sldSharp.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                scale = slider.getValue();
                view.requestRender();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave){
            String path = saveBitmapToStorage(EditActivity.temp);
            Intent save = new Intent(SharpAction.this,EditActivity.class);
            save.putExtra("path",path);
            setResult(RESULT_OK,save);
            ++EditActivity.time;

            finish();
        }
        if(v.getId() == R.id.btnNotSave){
            String path = saveBitmapToStorage(EditActivity.saveImage);
            Intent save = new Intent(SharpAction.this,EditActivity.class);
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


}

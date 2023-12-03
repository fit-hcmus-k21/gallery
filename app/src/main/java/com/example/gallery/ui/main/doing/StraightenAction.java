package com.example.gallery.ui.main.doing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gallery.R;
import com.example.gallery.ui.main.adapter.EffectRenderer;
import com.google.android.material.slider.Slider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StraightenAction extends AppCompatActivity implements View.OnClickListener {
    public  int ScaleParameter = -1;
    GLSurfaceView view ;
    Slider sldStraighten;
    EffectRenderer render;
    Button btnSave,btnNotSave;
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.straighten_layout);
        Intent intent = getIntent();

        view = (GLSurfaceView)  findViewById(R.id.straightenImage);
        sldStraighten = (Slider) findViewById(R.id.straightenSlider);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnNotSave = (Button) findViewById(R.id.btnNotSave);
        btnNotSave.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        view.setEGLContextClientVersion(2);
        render = new EffectRenderer(this,"STRAIGHTEN",EditActivity.saveImage);
        view.setRenderer(render);
        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        sldStraighten.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                ScaleParameter = (int)slider.getValue();
                view.requestRender();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave){
            String path = saveBitmapToStorage(EditActivity.temp);
            Intent save = new Intent(StraightenAction.this,EditActivity.class);
            save.putExtra("path",path);
            setResult(RESULT_OK,save);
            ++EditActivity.time;
            finish();
        }
        if(v.getId() == R.id.btnNotSave){
            String path = saveBitmapToStorage(EditActivity.saveImage);
            Intent save = new Intent(StraightenAction.this,EditActivity.class);
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

package com.example.gallery.ui.main.doing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gallery.R;
import com.example.gallery.ui.main.adapter.EffectRenderer;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.slider.Slider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FilterAction extends AppCompatActivity implements View.OnClickListener {
    public Bitmap saveFilter;
    public GLSurfaceView view;
    public ViewGroup scrollViewGroup;
    public Slider slider;
    Button btnSave,btnNotSave;
    EffectRenderer render;
    public int curEffect = -1;
    public float parameterEffect =1.0f;
    MaterialToolbar toolbar;
    Integer[] thumbnails = {R.drawable.none_effect,R.drawable.brightness_effect,R.drawable.documentary_effect ,R.drawable.doutone_effect,R.drawable.grain_effect,R.drawable.grayscale_effect,R.drawable.lomoish_effect,R.drawable.negative_effect,R.drawable.posterize_effect, R.drawable.sepia_effect, R.drawable.tint_effect, R.drawable.sature_effect,R.drawable.temperature_effect,R.drawable.fill_light_effect};
    String[] items = {"None","Bright","Documentary","Doutone","Grain","GrayScale","Lomoish","Negative","Poterize","Sepia","Tint","Saturate","Temperature","FillLight"};

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.filter_layout);
        Intent intent = getIntent();
        view = (GLSurfaceView) findViewById(R.id.surfaceView);
        scrollViewGroup =(ViewGroup) findViewById(R.id.scrollView);
        slider = (Slider) findViewById(R.id.slider);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnNotSave = (Button) findViewById(R.id.btnNotSave);

        btnNotSave.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        view.setEGLContextClientVersion(2);
        render = new EffectRenderer(FilterAction.this,"FILTER",EditActivity.saveImage);
        view.setRenderer(render);
        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        //populate the scroll view
        for(int i = 0; i < items.length; ++i){
        //create single frame [icon & description] using XML inflate
        final View singleFrame =getLayoutInflater().inflate(R.layout.edit_item,null);
        singleFrame.setId(i);
        //internal plumbing to reach elements inside single frame
        TextView txtDescription = singleFrame.findViewById(R.id.desciption);
        ImageView icon = singleFrame.findViewById(R.id.Icon);
        //put data [icon,description] in each frame
        icon.setImageResource(thumbnails[i]);
        txtDescription.setText(items[i]);
        // add frame to the scroll view
        scrollViewGroup.addView(singleFrame);
        int finalI = i;
        singleFrame.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                //đặt toàn bộ màu nền của các item về trong suốt
                for (int j = 0; j < items.length; ++j) {
                    View child = scrollViewGroup.getChildAt(j);
                    child.setBackgroundColor(android.R.color.transparent);
                }
                curEffect = finalI;
                if(finalI == 1 || finalI == 11 || finalI == 12 || finalI == 13) {
                    slider.setVisibility(View.VISIBLE);
                    slider.addOnChangeListener(new Slider.OnChangeListener() {
                        @Override
                        public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                            parameterEffect = slider.getValue();
                            view.requestRender();
                        }
                    });
                }
                else{
                    slider.setVisibility(View.INVISIBLE);
                    view.requestRender();
                }
                view.requestRender();
                v.setBackgroundColor(getResources().getColor(R.color.teal_700));
            }
        });

        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave){
            String path = saveBitmapToStorage(EditActivity.temp);
            Intent save = new Intent(FilterAction.this,EditActivity.class);
            save.putExtra("path",path);
            setResult(RESULT_OK,save);
            ++EditActivity.time;
            finish();
        }
        if(v.getId() == R.id.btnNotSave){
            String path = saveBitmapToStorage(EditActivity.saveImage);
            Intent save = new Intent(FilterAction.this,EditActivity.class);
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

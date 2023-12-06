package com.example.gallery.ui.main.doing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.gallery.App;
import com.example.gallery.R;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    public static Bitmap rootImage, saveImage;
    public static Bitmap temp;
    ImageView view;
    public String type ="MAIN";
    BottomNavigationView navigation;
    int screenWidth, screenHeight;      // not change
    public static int deviceWidth;
    public static int deviceHeight;
    AppCompatButton btnSave,btnCancel;
    public static int time = 0;
    MediaItem mediaItem;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.edit_screen);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        mediaItem = (MediaItem) data.get("meidaItem");
        String path = mediaItem.getPath();

        view = (ImageView) findViewById(R.id.mainImage);
        rootImage = BitmapFactory.decodeFile(path);
        saveImage = rootImage;
        navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        btnCancel = (AppCompatButton) findViewById(R.id.btnCancel);
        btnSave = (AppCompatButton) findViewById(R.id.btnSave);

        view.setImageBitmap(rootImage);
        screenWidth = view.getWidth();
        screenHeight = view.getHeight();

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        // Lấy chiều rộng và chiều cao của màn hình
        deviceWidth = displayMetrics.widthPixels;
        deviceHeight = displayMetrics.heightPixels;

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.btnFlipAndRotate){
                    Intent rotateIntent = new Intent(EditActivity.this,FlipAndRotateAction.class);
                    startActivityForResult(rotateIntent,123456);

                }
                if(item.getItemId() == R.id.btnDraw){
                    Intent drawIntent = new Intent(EditActivity.this,DrawAction.class);
                    startActivityForResult(drawIntent,123456);
                }
                if(item.getItemId() == R.id.btnFilter){
                    Intent filterIntent = new Intent(EditActivity.this,FilterAction.class);
                    startActivityForResult(filterIntent,123456);
                }
                if(item.getItemId() == R.id.btnSharp){
                    Intent sharpIntent = new Intent(EditActivity.this,SharpAction.class);
                    startActivityForResult(sharpIntent,123456);

                }
                if(item.getItemId() == R.id.btnStraighten){
                    Intent straightenIntent = new Intent(EditActivity.this,StraightenAction.class);
                    startActivityForResult(straightenIntent,123456);

                }

                return true;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent receive ) {
        super.onActivityResult(requestCode, resultCode, receive);
        if(requestCode == 123456  && resultCode == RESULT_OK){
            String path = receive.getStringExtra("path");
            Toast.makeText(EditActivity.this,path,Toast.LENGTH_SHORT).show();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            File imageFile = new File(path);

            if (imageFile.exists()) {
                boolean deleted = imageFile.delete();
                if (deleted) {
                } else {
                    // Xảy ra lỗi khi xóa tệp hình ảnh tạm thời
                }
            }
            saveImage = bitmap;
            temp = bitmap;
            view.setImageBitmap(bitmap);
            if(time != 0){
                btnSave.setClickable(true);
                btnSave.setEnabled(true);
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave){
            String path = saveBitmapToStorage(saveImage);
            Intent respondSingleActivity = new Intent(EditActivity.this,SingleMediaActivity.class);
            Bundle data = new Bundle();
            data.putString("result","SAVE");
            data.putString("afterEdit",path);
            data.putSerializable("received",mediaItem);
            respondSingleActivity.putExtras(data);
            setResult(RESULT_OK,respondSingleActivity);
            finish();
        }
        if(v.getId() == R.id.btnCancel){
            Intent respondSingleActivity = new Intent(EditActivity.this,SingleMediaActivity.class);
            Bundle data = new Bundle();
            data.putString("result","NOTSAVE");
            respondSingleActivity.putExtras(data);
            setResult(RESULT_OK,respondSingleActivity);
            finish();
        }
    }
    public  String saveBitmapToStorage(Bitmap bitmap) {
        String fileName = getFileNameFromPath(mediaItem.getPath()) + "_edited" + new Random().nextInt(1000) + mediaItem.getFileExtension();

        File storageDir = getExternalCacheDir(); // Lấy thư mục tạm ngoài
        File imageFile = new File(storageDir, fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            String absolutePath = imageFile.getAbsolutePath();

            return absolutePath;
        } catch (IOException e) {
            // Xảy ra lỗi khi lưu tệp hình ảnh
            e.printStackTrace();
            return null;
        }
    }

    public  String getFileNameFromPath(String path){
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}

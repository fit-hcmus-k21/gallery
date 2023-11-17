package com.example.gallery.ui.main;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.data.local.entities.MediaItem;
import com.example.gallery.data.repositories.models.MediaItemViewModel;
import com.example.gallery.data.repositories.models.TestAdapter;

import java.util.List;

public class ViewTestDatabase extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 101;
    TextView textView;

    MediaItemViewModel mediaItemViewModel;
    RecyclerView recyclerView;
    TestAdapter testAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testdatabase_layout);

        textView = (TextView) findViewById(R.id.textTestView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTest);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        testAdapter = new TestAdapter();
        recyclerView.setAdapter(testAdapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_MEDIA_IMAGES
                                , android.Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_PERMISSION_CODE);
            } else {
                Log.e("Tag onCreate", "Permission is granted");
                LoadDataAndUpdateUI();
            }
        }

    }

    private void LoadDataAndUpdateUI() {
        mediaItemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MediaItemViewModel.class);

        mediaItemViewModel.fetchDataFromExternalStorageAndSaveToDataBase();

        mediaItemViewModel.getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                testAdapter.setData(mediaItems);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            Log.e("Tag requestResult", "Permission is granted");
            LoadDataAndUpdateUI();

        }
    }



}

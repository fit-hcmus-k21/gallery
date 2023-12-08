package com.example.gallery.data.repositories.models.Test;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.HelperFunction.RequestPermissionHelper;
import com.example.gallery.data.repositories.models.ViewModel.AlbumViewModel;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;

import java.util.List;

public class ViewTestMediaItem extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 101;
    TextView textView;


    RecyclerView recyclerView;
    MediaItemAdapter_Test mediaItemAdapterTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testdatabase_layout);

        textView = (TextView) findViewById(R.id.textTestView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTest);


        if(RequestPermissionHelper.checkAndRequestPermission(this, REQUEST_PERMISSION_CODE)){
            LoadDataAndUpdateUI();
        }
        else{
//            Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show();
        }

    }

    private void LoadDataAndUpdateUI() {
        UserViewModel.getInstance().getAllUserData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null ){
                    //  System.out.println("on user change");
                    AlbumViewModel.getInstance().fetchData();
                }
            }
        });
        AlbumViewModel.getInstance().getAllAlbums().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                if(albums != null && !albums.isEmpty()){
                    MediaItemViewModel.getInstance().fetchData( ViewTestMediaItem.this);
                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        mediaItemAdapterTest = new MediaItemAdapter_Test();
        recyclerView.setAdapter(mediaItemAdapterTest);


        MediaItemViewModel.getInstance().getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                mediaItemAdapterTest.setData(mediaItems);
            }
        });
        mediaItemAdapterTest.setOnItemClickListener(new MediaItemAdapter_Test.onItemClickListener() {
            @Override
            public void onItemClick(MediaItem mediaItem) {
                Toast.makeText(ViewTestMediaItem.this, mediaItem.getPath(), Toast.LENGTH_SHORT).show();
                MediaItemViewModel.getInstance().updateDeletedTs(mediaItem.getId(), System.currentTimeMillis());
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

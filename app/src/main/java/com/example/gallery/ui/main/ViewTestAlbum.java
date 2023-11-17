package com.example.gallery.ui.main;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.gallery.R;
import com.example.gallery.data.local.dao.UserDao;
import com.example.gallery.data.local.entities.Album;
import com.example.gallery.data.local.entities.User;
import com.example.gallery.data.repositories.models.AlbumViewModel;
import com.example.gallery.data.repositories.models.TestAlbumAdapter;
import com.example.gallery.data.repositories.models.UserViewModel;
import com.example.gallery.utils.RequestPermissionHelper;

import java.util.List;

public class ViewTestAlbum extends AppCompatActivity {
    private ListView albumListView;
    private AlbumViewModel albumViewModel;
    private UserViewModel userViewModel;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testalbumview_layout);

        albumListView = (ListView) findViewById(R.id.albumListView);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.READ_MEDIA_IMAGES
//                                , android.Manifest.permission.READ_MEDIA_IMAGES
//                        , Manifest.permission.READ_MEDIA_VIDEO},
//                        102);
//            } else {
//                Log.e("Tag onCreate", "Permission is granted");
//                loadAndDisplayAlbum();
//            }
//        }
        if(RequestPermissionHelper.checkAndRequestPermission(this, 102)){
            loadAndDisplayAlbum();
        }
        else{
            Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            Log.e("Tag requestResult", "Permission is granted");
            loadAndDisplayAlbum();
        }
    }

    private void loadAndDisplayAlbum() {
        albumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.insertUser(new User(1, "User1", "", "user1", "123",
                "user1@example.com", "", "", "", ""));
        userViewModel.insertUser(new User(10, "User2", "", "user2", "123",
                "user2@example.com", "", "", "", ""));

        userViewModel.fetchUsersFromDatabase();
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for(int i = 0 ; i < users.size(); i++){
                    Log.e("TagUserGetID", "UserID: " +  users.get(i).getId());
                }
            }
        });
        albumViewModel.fetchDataFromExternalStorageAndSaveToDataBase();
        albumViewModel.getAllAlbums().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                TestAlbumAdapter albumArrayAdapter = new TestAlbumAdapter(ViewTestAlbum.this, albums);
                albumListView.setAdapter(albumArrayAdapter);
            }
        });


    }
}

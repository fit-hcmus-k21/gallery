package com.example.gallery.data.repositories.models.Test;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gallery.App;
import com.example.gallery.R;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.ViewModel.AlbumViewModel;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;
import com.example.gallery.data.repositories.models.HelperFunction.RequestPermissionHelper;

import java.util.List;

public class ViewTestAlbum extends AppCompatActivity {
    private ListView albumListView;

    private static String userID = AppPreferencesHelper.getInstance().getCurrentUserId();
    
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



//        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> users) {
//                for(int i = 0 ; i < users.size(); i++){
//                    Log.e("TagUserGetID", "UserID: " +  users.get(i).getId());
//                }
//            }
//        });
        AlbumRepository.getInstance().fetchData();
        AlbumAdapter_Test albumArrayAdapter = new AlbumAdapter_Test(ViewTestAlbum.this);
        albumListView.setAdapter(albumArrayAdapter);

        AlbumRepository.getInstance().getAlbums().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                albumArrayAdapter.setData(albums);
            }
        });
        albumArrayAdapter.setOnItemClickListener(new AlbumAdapter_Test.onItemClickListener() {
            @Override
            public void onItemClick(Album album) {
                Toast.makeText(ViewTestAlbum.this, album.getPath(), Toast.LENGTH_SHORT).show();
                AlbumRepository.getInstance().updateAlbumDeletedTs(album.getPath(), System.currentTimeMillis());
            }
        });
    }
}

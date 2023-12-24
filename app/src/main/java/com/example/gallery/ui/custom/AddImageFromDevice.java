package com.example.gallery.ui.custom;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.example.gallery.App;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.annotation.Nullable;

public class AddImageFromDevice extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2;
    String albumNameFromIntent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate in AddImageFromDevice");
        // --------------------

        // Get album name from intent
        Intent intent = getIntent();
        albumNameFromIntent = intent.getStringExtra("albumName");

        // --------------------

        openImagePicker();
    }




//    --------------------




//    --------------------
    private String getPathFromUri(Uri uri, int typeMedia) {
        String path = null;

        if(typeMedia == 1){
            // Handle image file
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if(cursor != null){
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
        }
        else if(typeMedia == 2){
            // Handle video file
            String[] projection = new String[]{MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if(cursor != null){
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                path = cursor.getString(columnIndex);
            }
        }
        return path;
    }

//    --------------------


    // Handle the result from the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            // Check if the permission has been granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the image picker
                openImagePicker();
            } else {
                // Permission denied, you may want to inform the user
                Log.d("MainActivity", "Permission denied");
            }
        }
    }

}

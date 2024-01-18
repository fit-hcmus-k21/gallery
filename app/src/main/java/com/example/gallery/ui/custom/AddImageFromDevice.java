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

        onRequestPermissionsResult(REQUEST_READ_EXTERNAL_STORAGE, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new int[]{PackageManager.PERMISSION_GRANTED});
    }




//    --------------------

    private void openImagePicker() {
        int maxSelection = 10;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && android.os.ext.SdkExtensions.getExtensionVersion(android.os.Build.VERSION_CODES.R) >= 2) {
            maxSelection = MediaStore.getPickImagesMaxLimit();
        }



        ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
                registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(maxSelection), uris -> {
                    if (!uris.isEmpty()) {
                        for (Uri uri : uris) {
                            // Check if the selected media is an image or video
                            if (isVideoFile(uri)) {
                                // Handle video file
                                MediaItem item = new MediaItem();

                                String path = getPathFromUri(uri, item, 2);
                                if (path != null) {

                                    MediaItemRepository.getInstance().insert(item);

                                    // Update thumbnail for "Videos" Album
                                    AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), albumNameFromIntent != null && !albumNameFromIntent.isEmpty() ? albumNameFromIntent : "Videos", path);

                                    System.out.println("add video from device success: " + item.getPath());
                                }
                            } else {
                                // Handle image file (similar to your existing code)
                                MediaItem item = new MediaItem();

                                String path = getPathFromUri(uri, item, 1);
                                if (path != null) {
                                    item.setCreationDate(System.currentTimeMillis());
                                    item.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                                    item.setAlbumName(albumNameFromIntent != null && !albumNameFromIntent.isEmpty() ? albumNameFromIntent : "Tất cả");
                                    item.setPath(path);

                                    MediaItemRepository.getInstance().insert(item);

                                    // Update thumbnail for "All" Album
                                    AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), albumNameFromIntent != null && !albumNameFromIntent.isEmpty() ? albumNameFromIntent : "Tất cả", path);

                                    System.out.println("add image from device success: " + item.getPath());
                                }
                            }
                        }

                        Toast.makeText(this, "Thêm ảnh thành công !", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build());
    }



    // Function to check if the selected media is a video file
    private boolean isVideoFile(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        return mimeType != null && mimeType.startsWith("video/");
    }


//    --------------------
    private String getPathFromUri(Uri uri, MediaItem item, int typeMedia) {
        String path = null;

        String[] projections = new String[]{
                MediaStore.Images.Media._ID,
                // USER ID IS NOT HERE, MAYBE WE CAN ASSIGN IT TO 0
                MediaStore.Images.Media.DISPLAY_NAME,
                // TAG IS NOT HERE, MAYBE WE CAN ASSIGN IT TO ""
                MediaStore.Images.Media.DESCRIPTION, // DESCRIPTION MAY BE NULL, SO WE INITIALIZE IT TO ""
                MediaStore.Images.Media.DATA, // THIS IS PATH
                MediaStore.Images.Media.WIDTH, // THIS IS WIDTH
                MediaStore.Images.Media.HEIGHT, // THIS IS HEIGHT
                MediaStore.Images.Media.SIZE, // THIS IS FILE SIZE
                MediaStore.Images.Media.MIME_TYPE, // THIS IS FILE EXTENSION
                MediaStore.Images.Media.DATE_ADDED, // THIS IS CREATION DATE
//                MediaStore.Images.Media.RELATIVE_PATH, // THIS IS LOCATION
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // THIS IS ALBUM NAME
                MediaStore.Images.Media.DATE_MODIFIED,
//                MediaStore.Images.Media.IS_FAVORITE
                // URL IS NOT HERE, MAYBE WE CAN ASSIGN IT TO ""
        };

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

            Cursor cursor = getContentResolver().query(uri, projections, null, null, null);
            if(cursor != null){
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                path = cursor.getString(columnIndex);

                String userID = AppPreferencesHelper.getInstance().getCurrentUserId();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String tag = "";

                String discription = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION));
                if (discription == null) {
                    discription = "";
                }

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                int width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH));
                int height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT));
                long fileSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                String fileExtension = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));

                // CreationDate is milisecond format
                long creationDate = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)) * 1000L; // convert to millisecond. Must multiply by 1000L

                String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                String url = "";



                // lastModifed is milisecond format
                long lastModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)) * 1000L; // convert to millisecond. Must multiply by 1000L

                // Get parentPath
                String parentPath = path.substring(0, path.lastIndexOf("/"));

                item.setPath(path);
                item.setUserID(userID);
                item.setName(name);
                item.setTag(tag);
                item.setDescription(discription);
                item.setWidth(width);
                item.setHeight(height);
                item.setFileSize(fileSize);
                item.setFileExtension(fileExtension);
                item.setCreationDate(creationDate);
                item.setAlbumName(albumName);
                item.setUrl(url);
                item.setLastModified(lastModified);
                item.setParentPath(parentPath);

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

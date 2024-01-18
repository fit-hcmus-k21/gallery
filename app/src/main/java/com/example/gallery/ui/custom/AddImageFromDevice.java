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
                                String path = moveVideoToExternalStorage(uri);
                                if (path != null) {
                                    MediaItem item = new MediaItem();
                                    item.setCreationDate(System.currentTimeMillis());
                                    item.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                                    item.setAlbumName( albumNameFromIntent != null && !albumNameFromIntent.isEmpty() ? albumNameFromIntent : "Videos");
                                    item.setPath(path);
                                    item.setFileExtension("mp4");


                                    MediaItemRepository.getInstance().insert(item);

                                    // Update thumbnail for "Videos" Album
                                    AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), albumNameFromIntent != null && !albumNameFromIntent.isEmpty() ? albumNameFromIntent : "Videos", path);

                                    System.out.println("add video from device success: " + item.getPath());
                                }
                            } else {
                                // Handle image file (similar to your existing code)

                                String path = saveImageToExternalStorage(uri);
                                if (path != null) {
                                    MediaItem item = new MediaItem();
                                    item.setCreationDate(System.currentTimeMillis());
                                    item.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                                    item.setAlbumName(albumNameFromIntent != null && !albumNameFromIntent.isEmpty() ? albumNameFromIntent : "Từ thiết bị");
                                    item.setPath(path);

                                    MediaItemRepository.getInstance().insert(item);

                                    // Update thumbnail for "All" Album
                                    AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), albumNameFromIntent != null && !albumNameFromIntent.isEmpty() ? albumNameFromIntent : "Từ thiết bị", path);

                                    System.out.println("add image from device success: " + item.getPath());
                                }
                            }
                        }

                        Toast.makeText(this, "Đã thêm thành công !", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                        finish();
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

    // Function to save the video to internal storage and return the path
    private String moveVideoToExternalStorage(Uri videoUri) {
        ContentResolver contentResolver = getContentResolver();

        try {
            // Tạo cursor để truy vấn thông tin về video
            String[] projection = {MediaStore.Video.Media.DATA};
            Cursor cursor = contentResolver.query(videoUri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Lấy đường dẫn của video từ cursor
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                String videoPath = cursor.getString(columnIndex);

                File appDirectory = new File(App.getInstance().getExternalFilesDir(null), "Videos/FromDevice");
                if (!appDirectory.exists()) {
                    appDirectory.mkdirs();
                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String videoFileName = "VIDEO_" + timeStamp + ".mp4";
                File videoFile = new File(appDirectory, videoFileName);

                // Di chuyển dữ liệu từ tệp gốc đến tệp mới
                FileInputStream inputStream = new FileInputStream(videoPath);
                FileOutputStream outputStream = new FileOutputStream(videoFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();

                cursor.close();

                return videoFile.getAbsolutePath();

            } else {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }




    // Function to save the image to internal storage and return the path
    private String saveImageToExternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);

            String random = String.valueOf(System.currentTimeMillis()) + new Random().nextInt(1000);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + random + "." + getExtensionFromUri(uri);


            File appDirectory = new File(App.getInstance().getExternalFilesDir(null), "Images/FromDevice");
            if (!appDirectory.exists()) {
                appDirectory.mkdirs();
            }

            File imageFile = new File(appDirectory, imageFileName);


            OutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return imageFile.getAbsolutePath();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getExtensionFromUri(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        String mimeType = contentResolver.getType(uri);
        String extension = null;

        if (mimeType != null) {
            extension = mimeType.substring(mimeType.lastIndexOf("/") + 1);
        }

        if (extension == null) {
            extension = "jpg"; // default extension for unknown file types
        }

        return extension;
    }


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
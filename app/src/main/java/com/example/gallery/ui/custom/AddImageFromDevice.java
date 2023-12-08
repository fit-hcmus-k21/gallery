package com.example.gallery.ui.custom;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.database.Cursor;
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
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;

import java.io.File;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate in AddImageFromDevice");
        openImagePicker();
    }

    private String currentPhotoPath;


    // Function to open the image picker
    private void openImagePicker() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        System.out.println("openImagePicker | before");
//
//        File photoFile = null;
//        try {
//            photoFile = createImageFile();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        if (photoFile != null) {
//            Uri photoURI = FileProvider.getUriForFile(
//                    App.getInstance(),
//                    App.getProcessName() + ".provider",
//                    new File(photoFile.getPath())
//            );
//
//            System.out.println("MediaItemFragment : pick a picture: uri: " + photoURI);
//
//
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//            startActivityForResult(intent, REQUEST_IMAGE_PICK);
//
//            System.out.println("openImagePicker | after");
//        }
        int maxSelection = 10;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && android.os.ext.SdkExtensions.getExtensionVersion(android.os.Build.VERSION_CODES.R) >= 2) {
            maxSelection = MediaStore.getPickImagesMaxLimit();
        }

        // Registers a photo picker activity launcher in multi-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
                registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(maxSelection), uris -> {
                    // Callback is invoked after the user selects media items or closes the
                    // photo picker.
                    // TODO: này chỉ mới xử l thêm ảnh từ device, chưa thêm video
                    if (!uris.isEmpty()) {
                        Log.d("PhotoPicker", "Number of items selected: " + uris.size());

                        // Handle the selected URIs (e.g., save to database, display in UI)
                        for (Uri uri : uris) {
                            // Perform actions with each selected URI
                            String path = saveImageToInternalStorage(uri);
                            if (path != null) {
                                // Your code to save or process the URI
                                MediaItem item = new MediaItem();
                                item.setCreationDate(System.currentTimeMillis());
                                item.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                                item.setAlbumName("All");
                                item.setPath(path);


                                MediaItemRepository.getInstance().insert(item);
                                System.out.println("add img from device success : " + item.getPath());
                            }

                            // Log the path for demonstration
                            Log.d("PhotoPicker", "Selected URI: " + path);
                        }
                        Toast.makeText(this, "add img from device successfully", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

// For this example, launch the photo picker and let the user choose images
// and videos. If you want the user to select a specific type of media file,
// use the overloaded versions of launch(), as shown in the section about how
// to select a single media item.
        pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build());

    }

                        // Function to save the image to internal storage and return the path
        private String saveImageToInternalStorage(Uri uri) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                File internalStorageDir = getFilesDir();

                String random = String.valueOf(System.currentTimeMillis()) + new Random().nextInt(1000);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String imageFileName = "JPEG_" + timeStamp + random + ".jpg";

                File internalImageFile = new File(internalStorageDir, imageFileName);

                OutputStream outputStream = new FileOutputStream(internalImageFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();

                return internalImageFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
//            if (data == null) {
//                //Display an error
//                System.out.println("Pick image | data is null");
//                return;
//            }
//            if (currentPhotoPath != null) {
//                MediaItem item = new MediaItem();
//                item.setCreationDate(System.currentTimeMillis());
//                item.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
//                item.setAlbumName("All");
//                item.setPath(currentPhotoPath);
//
//                MediaItemRepository.getInstance().insert(item);
//                Toast.makeText(this, "add img from device successfully", Toast.LENGTH_SHORT).show();
//                System.out.println("add img from device success : " + currentPhotoPath);
//            }
//
//        }
//        finish();
//    }

//    private File createImageFile() throws IOException {
//        System.out.println("MediaItemFragment : createImageFile: ");
//        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
//
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ;
//        File imageFile = File.createTempFile(
//                imageFileName,
//                ".jpg",
//                storageDir
//        );
//
//        currentPhotoPath = imageFile.getAbsolutePath();
//        return imageFile;
//    }


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

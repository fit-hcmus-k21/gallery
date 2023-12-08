package com.example.gallery.ui.custom;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;

import javax.annotation.Nullable;

public class AddImageFromDevice extends Activity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openImagePicker();
    }


    // Function to open the image picker
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
        // trở về màn hình trước
        finish();
    }


    // Handle the result from the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult in AddImageFromDevice");
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // The user has successfully picked an image
            // You can get the URI of the selected image from the Intent's data
            Uri selectedImageUri = data.getData();

            // Now, you can do something with the selected image URI, such as display it or save it.
            // For simplicity, let's just print the URI to the log.
            Log.d("MainActivity", "Selected Image URI: " + selectedImageUri.toString());

            // lấy đường dẫn của ảnh
            String path = selectedImageUri.getPath();
            MediaItem item = new MediaItem();
            item.setPath(path);
            item.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
            item.setAlbumName("All");   // tạm thời để là All

            MediaItemRepository.getInstance().insert(item);
            System.out.println("inserted img from device   ");
            Toast.makeText(this, "inserted img successfully", Toast.LENGTH_SHORT).show();


        }
    }

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

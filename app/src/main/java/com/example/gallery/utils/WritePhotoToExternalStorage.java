package com.example.gallery.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.gallery.data.repositories.models.MediaItem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class WritePhotoToExternalStorage {
    public static MediaItem writePhotoToExternalStorage(Context context, Bitmap bitmap) {
         OutputStream outputStream;

        String fileName = System.currentTimeMillis() + ".jpg";

//        Phải sử dụng ContentValues để tiến hành thao tác với vùng nhớ/ bộ nhớ
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QRCodeImage");
        Uri imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        try {
            outputStream = context.getContentResolver().openOutputStream(imageUri);
            if(outputStream != null){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MediaItem mediaItem = new MediaItem();
        return mediaItem;

    }
}

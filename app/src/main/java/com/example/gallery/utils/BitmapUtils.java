package com.example.gallery.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {

        public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }

        public static Bitmap convertByteArrayToBitmap(byte[] byteArray) {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }



}

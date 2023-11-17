package com.example.gallery.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class RequestPermissionHelper {
    public static boolean checkAndRequestPermission(Activity activity, final int REQUEST_PERMISSION_CODE) {
        boolean permissionReadExternalStorage = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean permissionWriteExternalStorage = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean permissionReadMediaImages = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        boolean permissionReadMediaAudio = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        boolean permissionReadMediaVideo = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        boolean permissionCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;


        List<String> listPermissionNeeded = new ArrayList<>();

        if (!permissionReadExternalStorage) {
            listPermissionNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!permissionReadMediaImages) {
            listPermissionNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
        }

        if (!permissionReadMediaAudio) {
            listPermissionNeeded.add(Manifest.permission.READ_MEDIA_AUDIO);
        }

        if (!permissionReadMediaVideo) {
            listPermissionNeeded.add(Manifest.permission.READ_MEDIA_VIDEO);
        }

        if (!permissionCamera) {
            listPermissionNeeded.add(Manifest.permission.CAMERA);
        }

        if(!permissionWriteExternalStorage){
            listPermissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), REQUEST_PERMISSION_CODE);
            return false;
        }
        return true;
    }

}

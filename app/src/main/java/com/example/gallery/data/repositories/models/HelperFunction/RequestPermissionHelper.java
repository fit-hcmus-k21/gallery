package com.example.gallery.data.repositories.models.HelperFunction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;

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

        if (!permissionReadExternalStorage || !permissionWriteExternalStorage || !permissionReadMediaImages || !permissionReadMediaAudio || !permissionReadMediaVideo || !permissionCamera) {
            // Yêu cầu quyền
            if (shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_IMAGES)
                    || shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_AUDIO)
                    || shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_VIDEO)
                    || shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                // Hiển thị giải thích cho người dùng về lý do cần quyền
                new AlertDialog.Builder(activity)
                        .setMessage("Ứng dụng cần quyền truy cập để tải ảnh lên từ thiết bị.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Yêu cầu quyền
                                ActivityCompat.requestPermissions(activity, new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_MEDIA_IMAGES,
                                        Manifest.permission.READ_MEDIA_AUDIO,
                                        Manifest.permission.READ_MEDIA_VIDEO,
                                        Manifest.permission.CAMERA
                                }, REQUEST_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            } else {
                // Yêu cầu quyền mà không hiển thị giải thích (lần đầu hoặc người dùng đã chọn "Không hiển thị lần nữa")
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.CAMERA
                }, REQUEST_PERMISSION_CODE);
            }
        }


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

    private static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

}

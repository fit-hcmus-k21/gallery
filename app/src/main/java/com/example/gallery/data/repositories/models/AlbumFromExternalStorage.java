package com.example.gallery.data.repositories.models;

import  com.example.gallery.data.local.entities.Album;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AlbumFromExternalStorage {
    /*
        private int id;
        private String name;
        private String description;
        private String creationDate;
        private String coverPhotoPath;
        private int userID;
        private String path;*/
    public static ArrayList<Album> listAlbums(Application application) {
        ArrayList<Album> albums = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projections = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
        };

        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = application.getContentResolver().query(
                uri,
                projections,
                null,
                null,
                sortOrder
        );
        List<String> folderNames = new ArrayList<>();
        if(cursor != null){
            cursor.moveToFirst();
            do{
                String folderName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if(folderName != null && !folderNames.contains(folderName)){
                    folderNames.add(folderName);

                    String coverPhotoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    String folderPath = coverPhotoPath.substring(0, coverPhotoPath.lastIndexOf(File.separator));

                    long creationDate = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)) * 1000L; // convert to millisecond. Must multiply by 1000L

                    albums.add(new Album(folderName, "", creationDate, coverPhotoPath, 1, folderPath));
                }
            }while(cursor.moveToNext());
        }

        return albums;
    }

}

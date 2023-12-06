package com.example.gallery.data.repositories.models.HelperFunction;

import com.example.gallery.App;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import  com.example.gallery.data.models.db.Album;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;

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
        private String userID;
        private String path;*/
    private static String userID = AppPreferencesHelper.getInstance().getCurrentUserId();
    public static ArrayList<Album> listAlbums(Application application) {
        ArrayList<Album> albums = new ArrayList<>();

        boolean isFavoriteAlbumExist = false;

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projections = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
//                MediaStore.Images.Media.IS_FAVORITE
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
//                Log.e("Mytag", "Folder name: " + folderName);
                if(isFavoriteAlbumExist == false){
                    if(folderName != null && cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.IS_FAVORITE)) == 1){

                        long creationDateFav = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)) * 1000L; // convert to millisecond. Must multiply by 1000L
                        String coverPhotoPathFav = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
//                        Log.e("Mytag", "Favorite: " + coverPhotoPathFav);
                        String folderPathFav = coverPhotoPathFav.substring(0, coverPhotoPathFav.lastIndexOf(File.separator));

                        //TODO xem xét để hình ảnh là hình trái tym
                        albums.add(new Album("Favorite", "",creationDateFav , coverPhotoPathFav, userID, "favoritePath", 0));

                        isFavoriteAlbumExist = true;
                    }

                }



                if(folderName != null && !folderNames.contains(folderName)){

                    folderNames.add(folderName);

                    String coverPhotoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                    String folderPath = coverPhotoPath.substring(0, coverPhotoPath.lastIndexOf(File.separator));

                    long creationDate = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)) * 1000L; // convert to millisecond. Must multiply by 1000L

                    albums.add(new Album(folderName, "", creationDate, coverPhotoPath, userID, folderPath, 0));
                }
            }while(cursor.moveToNext());
        }

        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String projectionsVideo[] = new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED
        };

        sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";

        cursor = application.getContentResolver().query(
                uri,
                projections,
                null,
                null,
                sortOrder
        );

        if(cursor != null){
            cursor.moveToFirst();
            do{
                String folderName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                if(folderName != null && !folderNames.contains(folderName)){
                    folderNames.add(folderName);

                    String coverPhotoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    String folderPath = coverPhotoPath.substring(0, coverPhotoPath.lastIndexOf(File.separator));

                    long creationDate = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)) * 1000L; // convert to millisecond. Must multiply by 1000L

                    albums.add(new Album(folderName, "", creationDate, coverPhotoPath, userID, folderPath, 0));
                }
            }while(cursor.moveToNext());
        }

        return albums;
    }

}

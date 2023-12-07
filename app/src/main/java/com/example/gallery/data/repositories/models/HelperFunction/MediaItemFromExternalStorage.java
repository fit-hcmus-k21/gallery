package com.example.gallery.data.repositories.models.HelperFunction;

import android.app.Application;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.gallery.App;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MediaItemFromExternalStorage {
    public static ArrayList<MediaItem> listMediaItems(Application application) {
        System.out.println("Media from external storage 25");
        ArrayList<MediaItem> listMediaItems = new ArrayList<>();
       /*Data is
        private int id;
    private String userID;
        private String name;
    private String tag;
        private String description; => ""
        private String path; => MediaStore.Images.Media.DATA
        private int width;
        private int height;
        private long fileSize;
        private String fileExtension; => MediaStore.Images.Media.MIME_TYPE
        private String creationDate;
        private String location; => MediaStore.Images.Media.RELATIVE_PATH
        private String albumName; => MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    private String url;??

        */
        System.out.println("Media from external storage 45");

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
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
        String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
        Cursor cursor = application.getContentResolver().query(
                uri,
                projections,
                null,
                null,
                orderBy
        );
        if (cursor != null) {
            cursor.moveToFirst();
            do {
//                Log.e("====================================", "====================================");
//                Log.e("ID", "XKLD: " + cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
//                Log.e("DISPLAY NAME", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
//                Log.e("DESCRIPTION", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION)));
//                Log.e("PATH", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
//                Log.e("WIDTH", "" + cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)));
//                Log.e("HEIGHT", "" + cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)));
//                Log.e("SIZE", "" + cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
//                Log.e("MIME TYPE", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)));
//                Log.e("DATE ADDED", "" + "" + cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)));
//                Log.e("RELATIVE PATH", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)));
//                Log.e("BUCKET DISPLAY NAME", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
//                Log.e("DATE MODIFIED", "" + cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)));
//                Log.e("IS FAVORITE", "" + cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.IS_FAVORITE)));
//
//                Log.e("====================================", "====================================");

                int ID = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                String userID = AppPreferencesHelper.getInstance().getCurrentUserId();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String tag = "";

                String discription = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION));
                if (discription == null) {
                    discription = "";
                }

                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                int width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH));
                int height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT));
                long fileSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                String fileExtension = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));

                // CreationDate is milisecond format
                long creationDate = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)) * 1000L; // convert to millisecond. Must multiply by 1000L

//                String location = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH));
                String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String url = "";

                // Get ? favorite
//                boolean favorite = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.IS_FAVORITE)) == 1;

                // lastModifed is milisecond format
                long lastModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)) * 1000L; // convert to millisecond. Must multiply by 1000L

                // Get parentPath
                String parentPath = path.substring(0, path.lastIndexOf("/"));

                System.out.println("Media from external storage 126");

                listMediaItems.add(new MediaItem(ID, userID, name, tag, discription,
                        path, width, height, fileSize, fileExtension, creationDate, "", albumName, url, false, parentPath, lastModified, 0));
            } while (cursor.moveToNext());
        }

        // Get Video from External Storage
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        projections = new String[]{
                MediaStore.Video.Media._ID,
                // USER ID IS NOT HERE, MAYBE WE CAN ASSIGN IT TO 0
                MediaStore.Video.Media.DISPLAY_NAME,
                // TAG IS NOT HERE, MAYBE WE CAN ASSIGN IT TO ""
                MediaStore.Video.Media.DESCRIPTION, // DESCRIPTION MAY BE NULL, SO WE INITIALIZE IT TO ""
                MediaStore.Video.Media.DATA, // THIS IS PATH
                MediaStore.Video.Media.WIDTH, // THIS IS WIDTH
                MediaStore.Video.Media.HEIGHT, // THIS IS HEIGHT
                MediaStore.Video.Media.SIZE, // THIS IS FILE SIZE
                MediaStore.Video.Media.MIME_TYPE, // THIS IS FILE EXTENSION
                MediaStore.Video.Media.DATE_ADDED, // THIS IS CREATION DATE
//                MediaStore.Video.Media.RELATIVE_PATH, // THIS IS LOCATION
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME, // THIS IS ALBUM NAME
                MediaStore.Video.Media.DATE_MODIFIED,
//                MediaStore.Video.Media.IS_FAVORITE
                // URL IS NOT HERE, MAYBE WE CAN ASSIGN IT TO ""
        };
        orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC";
        cursor = application.getContentResolver().query(
                uri,
                projections,
                null,
                null,
                orderBy
        );
        if (cursor != null) {
            cursor.moveToFirst();
            do {
//                Log.e("====================================", "====================================");
//                Log.e("ID", "XKLD: " + cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)));
//                Log.e("DISPLAY NAME", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
//                Log.e("DESCRIPTION", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION)));
//                Log.e("PATH", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
//                Log.e("WIDTH", "" + cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)));
//                Log.e("HEIGHT", "" + cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)));
//                Log.e("SIZE", "" + cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
//                Log.e("MIME TYPE", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
//                Log.e("DATE ADDED", "" + "" + cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)));
//                Log.e("RELATIVE PATH", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH)));
//                Log.e("BUCKET DISPLAY NAME", "" + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)));
//                Log.e("DATE MODIFIED", "" + cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)));
//                Log.e("IS FAVORITE", "" + cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.IS_FAVORITE)));
//
//                Log.e("====================================", "====================================");

                int ID = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                String userID = "";
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String tag = "";

                String discription = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION));
                if (discription == null) {
                    discription = "";
                }


                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                int width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH));
                int height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT));
                long fileSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                String fileExtension = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));

                // CreationDate is milisecond format
                long creationDate = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)) * 1000L; // convert to millisecond. Must multiply by 1000L

//                String location = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH));
                String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                String url = "";

                // Get ? favorite
//                boolean favorite = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.IS_FAVORITE)) == 1;

                // lastModifed is milisecond format
                long lastModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)) * 1000L; // convert to millisecond. Must multiply by 1000L

                // Get parentPath
                String parentPath = path.substring(0, path.lastIndexOf("/"));
                listMediaItems.add(new MediaItem(ID, userID, name, tag, discription,
                        path, width, height, fileSize, fileExtension, creationDate, " ", albumName, url, false, parentPath, lastModified, 0));
            } while (cursor.moveToNext());
        }

        // Sort listMediaItems by creationDate using lambda expression sắp xếp theo kiểu giảm dần theo creationDate
//        listMediaItems.sort((o1, o2) -> {
//            if (o1.getCreationDate() > o2.getCreationDate()) {
//                return -1;
//            } else if (o1.getCreationDate() < o2.getCreationDate()) {
//                return 1;
//            } else {
//                return 0;
//            }
//        });
        listMediaItems.sort(new Comparator<MediaItem>() {
            @Override
            public int compare(MediaItem o1, MediaItem o2) {
                if(o1.getCreationDate() > o2.getCreationDate()){
                    return -1;
                }
                else if(o1.getCreationDate() < o2.getCreationDate()){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        });

        return listMediaItems;
    }
}

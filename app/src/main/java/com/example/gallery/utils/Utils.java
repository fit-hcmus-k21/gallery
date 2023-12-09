package com.example.gallery.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {

    public static String getStoragePathFile(String id, long creationDate, String fileExtension) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(creationDate);
        return fileExtension + "/" + id + "_" + formattedDate + fileExtension;
    }
}

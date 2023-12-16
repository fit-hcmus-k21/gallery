package com.example.gallery.utils;

import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utils {

    public static String getStoragePathFile(String id, long creationDate, String fileExtension) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(creationDate);
        return fileExtension + "/" + id + "_" + formattedDate + fileExtension;
    }

    public static boolean isImageContentType(String contentType) {
        // Check if the content type corresponds to an image type
        // You can customize this method based on the specific image types you want to support
        return contentType != null && contentType.startsWith("image/");
    }

    public static boolean isValidUrl(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static String getFileExtension(String contentType) {
        if (contentType != null && contentType.contains("/")) {
            return "." + contentType.substring(contentType.lastIndexOf("/") + 1);
        }
        return "";
    }

    public static void fetchImageContentType(String imageUrl) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(imageUrl)
                .head() // Use a HEAD request to fetch headers only
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                // Get content type from the response headers
                String contentType = response.header("Content-Type");
                //  System.out.println("Content Type: " + contentType);
                // Handle the content type as needed
            }
        });
    }

    public static String getFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        // Use URI to parse the URL
        Uri uri = Uri.parse(url);

        // Get the last path segment, which is usually the filename
        String lastPathSegment = uri.getLastPathSegment();

        // You can perform additional checks or modifications if needed
        return lastPathSegment;
    }

    public static String addRandomNumberToFileName(String fileName) {
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Adjust the range as needed

        // Append the random number to the filename
        return randomNumber + "_" + fileName;
    }
}

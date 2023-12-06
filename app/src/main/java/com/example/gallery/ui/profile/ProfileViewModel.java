package com.example.gallery.ui.profile;


import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.gallery.App;
import com.example.gallery.data.DataManager;
import com.example.gallery.data.local.db.AppDBHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.data.repositories.models.ViewModel.AlbumViewModel;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.base.BaseViewModel;

import com.example.gallery.utils.BitmapUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.bolts.Task;
import com.facebook.login.LoginManager;

import com.facebook.login.LoginResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created on 15/11/2023
 */

public class ProfileViewModel extends BaseViewModel<ProfileNavigator> {


    public LiveData<Integer> getNumberOfImages() {
        return MediaItemRepository.getInstance().getNumberOfMediaItems();
    }

    public LiveData<Integer> getNumberOfAlbums() {
        return AlbumRepository.getInstance().getNumberOfAlbums();
    }




    // define methods in ProfileViewModel
    public void logout() {
        System.out.println("logout");
        getDataManager().clearPreferences();
        getNavigator().openLoginActivity();
    }

    public void addImageFromDevice() {
//        getNavigator().openAddImageFromDeviceActivity();
    }

    public void addImageFromCamera() {
//        getNavigator().openAddImageFromCameraActivity();
    }

    public void addImageFromLink_Ignore() {
        String imageUrl = getNavigator().getmProfileBinding().editTextImageUrl.getText().toString();
        System.out.println("Image Url: " + imageUrl);
        Glide.with(App.getInstance())
                .asBitmap()
                .load(imageUrl)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        // Xử lý khi tải ảnh thất bại
                        // Ví dụ: Hiển thị một hình ảnh mặc định hoặc thông báo lỗi
                        System.out.println("Have something wrong ");
                        return false; // Trả về false để để Glide xử lý lỗi tiếp theo (nếu có)
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        // Lưu bitmap vào cơ sở dữ liệu và hiển thị trong RecyclerView
                        System.out.println("Success load image with url");
//                        saveImageToDatabase(resource);

                        File appDirectory = new File(App.getInstance().getExternalFilesDir(null), "Images/FromUrls");
                        if (!appDirectory.exists()) {
                            appDirectory.mkdirs();
                        }

                        System.out.println("Resource: " + resource);
                        System.out.println("Model: " + model);
                        System.out.println("Target: " + target);
                        System.out.println("DataSource: " + dataSource);
                        System.out.println("isFirstResource: " + isFirstResource);

                        fetchImageContentType(imageUrl);
                        String fileExtension = resource.toString().substring(resource.toString().lastIndexOf("."));
                        String fileName = getFileNameFromUrl(imageUrl) + fileExtension;
                        System.out.println("File name: " + fileName);
                        File imageFile = new File(appDirectory, fileName);

                        // Save bitmap to folder

                        try (OutputStream stream = new FileOutputStream(imageFile)) {
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // get image path
                        String imgPath = imageFile.getAbsolutePath();

                        // save mediaitem inf to database
                        MediaItem item = new MediaItem();
                        item.setPath(imgPath);
                        item.setOrigin(imageUrl);
                        item.setCreationDate(Date.parse(new Date().toString()));
                        item.setId(MediaItemViewModel.getInstance().getNumberOfMediaItems().getValue() + 1);
                        item.setUserID(getDataManager().getCurrentUserId());
                        item.setAlbumName("From Urls");

                        getDataManager().insertMediaItem(item);

//                        get number rows in database
                        System.out.println("Number of media items after insert : " + MediaItemViewModel.getInstance().getNumberOfMediaItems());






                        return false; // Trả về false để để Glide xử lý sự kiện tiếp theo (nếu có)
                    }
                })
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        // Không cần thực hiện gì ở đây vì đã xử lý trong onResourceReady của RequestListener
                    }
                });


    }

    public void addImageFromLink() {

        getNavigator().getmProfileBinding().txtDownloadStatus.setText("Downloaded Status: downloading");
        getNavigator().getmProfileBinding().txtDownloadStatus.setTextColor(Color.parseColor("#808080"));

        String imageUrl = getNavigator().getmProfileBinding().editTextImageUrl.getText().toString();

        System.out.println("Image Url: " + imageUrl);
        if (!isValidUrl(imageUrl)) {
            getNavigator().getmProfileBinding().txtDownloadStatus.setText("Error: Invalid URL");
            getNavigator().getmProfileBinding().txtDownloadStatus.setTextColor(Color.parseColor("#FF0000"));

            return;
        }


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(imageUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle OkHttp failure...
                System.out.println("Have something wrong ");
                getNavigator().getmProfileBinding().txtDownloadStatus.setText("Downloaded Status: failed");
                getNavigator().getmProfileBinding().txtDownloadStatus.setTextColor(Color.parseColor("#FF0000"));


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Get the file extension from the Content-Type header
                    String contentType = response.header("Content-Type");
                    String fileExtension = getFileExtension(contentType);
                    if (!isImageContentType(contentType)) {
                        getNavigator().getmProfileBinding().txtDownloadStatus.setText("Error: Invalid image URL");
                        getNavigator().getmProfileBinding().txtDownloadStatus.setTextColor(Color.parseColor("#FF0000"));

                        return;
                    }

                    System.out.println("Content Type: " + contentType);

                    // Get the file name from the URL
                    String fileName = addRandomNumberToFileName(getFileNameFromUrl(imageUrl)) + fileExtension;
                    System.out.println("File name from url: " + fileName);

                    // Save the image to a file
                    File appDirectory = new File(App.getInstance().getExternalFilesDir(null), "Images/FromUrls");
                    if (!appDirectory.exists()) {
                        appDirectory.mkdirs();
                    }

                    File imageFile = new File(appDirectory, fileName);

                    try (OutputStream stream = new FileOutputStream(imageFile)) {
                        stream.write(response.body().bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Get image path
                    String imgPath = imageFile.getAbsolutePath();
                    System.out.println("Image path: " + imgPath);


                    // Save media item info to database
                    MediaItem item = new MediaItem();
                    item.setPath(imgPath);
                    item.setOrigin(imageUrl);
                    item.setCreationDate(Date.parse(new Date().toString()));
//                    item.setId(new Random().nextInt(10000));
                    item.setUserID(getDataManager().getCurrentUserId());
                    item.setAlbumName("Download");

                    // check if album not exist in table albums

                    MediaItemViewModel.getInstance().insert(item);
//                    Toast.makeText(App.getInstance(), "Insert successfully", Toast.LENGTH_SHORT).show();
                    System.out.println("Insert media item success from profile view model");



                    // Get the number of rows in the database
//                    System.out.println("Number of media items after download image: " + MediaItemViewModel.getInstance().getNumberOfMediaItems().getValue());
                    getNavigator().getmProfileBinding().txtDownloadStatus.setText("Status: download a " + fileExtension +" successfully!");
                    getNavigator().getmProfileBinding().txtDownloadStatus.setTextColor(Color.parseColor("#008000"));
                }
            }
        });

        // display image
        Glide.with(App.getInstance())
                .asBitmap()
                .load(imageUrl)
                .into(getNavigator().getmProfileBinding().imageViewDownloaded);
        getNavigator().getmProfileBinding().imageViewDownloaded.setVisibility(View.VISIBLE);


    }

    public void displayImageDownloaded(String imgPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);

    // Set the bitmap to the ImageView
        ImageView imageView = getNavigator().getmProfileBinding().imageViewDownloaded;
        imageView.setImageBitmap(bitmap);
        getNavigator().getmProfileBinding().imageViewDownloaded.setVisibility(View.VISIBLE);



    }


    private boolean isImageContentType(String contentType) {
        // Check if the content type corresponds to an image type
        // You can customize this method based on the specific image types you want to support
        return contentType != null && contentType.startsWith("image/");
    }
    private boolean isValidUrl(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
    private static String getFileExtension(String contentType) {
        if (contentType != null && contentType.contains("/")) {
            return "." + contentType.substring(contentType.lastIndexOf("/") + 1);
        }
        return "";
    }
    private void fetchImageContentType(String imageUrl) {
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
                System.out.println("Content Type: " + contentType);
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

    private static String addRandomNumberToFileName(String fileName) {
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Adjust the range as needed

        // Append the random number to the filename
        return  randomNumber + "_" +  fileName ;
    }


    public void backup() {

    }

    public void syncToCloudStorage() {

    }

    public void clearText() {

        getNavigator().getmProfileBinding().editTextImageUrl.setText("");
        getNavigator().getmProfileBinding().imageViewDownloaded.setVisibility(View.GONE);
    }


}




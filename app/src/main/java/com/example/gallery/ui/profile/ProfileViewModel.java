package com.example.gallery.ui.profile;


import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.data.repositories.models.Repository.UserRepository;
import com.example.gallery.data.repositories.models.ViewModel.AlbumViewModel;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.backup.BackupManager;
import com.example.gallery.ui.base.BaseViewModel;

import com.example.gallery.utils.BitmapUtils;
import com.example.gallery.utils.Utils;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public LiveData<User> getUser() {
        return UserRepository.getInstance().getAllUserData();
    }


    // define methods in ProfileViewModel
    public void logout() {
        //  System.out.println("logout");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        getDataManager().clearPreferences();
        getNavigator().openLoginActivity();
    }

    public void addImageFromDevice() {
        getNavigator().openAddImageFromDeviceActivity();
    }




    public void backup() {
        // TODO: backup all user data to local storage
        BackupManager.RestoreCloudToLocal();

    }


    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    public void syncToCloudStorage() {
        // TODO: upload all images, albums and user info to cloud storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(AppPreferencesHelper.getInstance().getCurrentUserId());

        ExecutorService executorService = Executors.newSingleThreadExecutor();


        // get reference to 'images' node
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users").child(AppPreferencesHelper.getInstance().getCurrentUserId());

        if (getUser().getValue() == null) {
            System.out.println("user is null  " + AppPreferencesHelper.getInstance().getCurrentUserId() );
            return;
        }

        usersRef.child("user_info").setValue(getUser().getValue())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Ghi dữ liệu thành công
                        System.out.println("Đã đồng bộ và ghi dữ liệu xong | user_info");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi
                        System.out.println("Có lỗi khi đồng bộ và ghi dữ liệu | user_info " + e.toString());
                    }
                });



        DatabaseReference albumRef = usersRef.child("user_data").child("albums");
        DatabaseReference mediaItemsRef = usersRef.child("user_data").child("media_items");


        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Album> listAlbums = AlbumRepository.getInstance().getAlbums().getValue();
                if (listAlbums == null) {
                    return;
                }
                for (Album album : listAlbums) {
                    albumRef.child(album.getName()).setValue(album)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Ghi dữ liệu thành công
                                    System.out.println("đồng bộ và ghi dữ liệu thành công | album");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý lỗi
                                    System.out.println("Có lỗi khi đồng bộ và ghi dữ liệu | album " + e.toString());
                                }
                            });
                }
                System.out.println("Đã đồng bộ và ghi dữ liệu xong | album");
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<MediaItem> listItems = MediaItemRepository.getInstance().getAllMediaItems().getValue();
                if (listItems == null) {
                    return;
                }

                String userId = AppPreferencesHelper.getInstance().getCurrentUserId() ;

                for (MediaItem item : listItems) {
                    String id = Utils.getStoragePathFile(item.getId() + "", item.getCreationDate(), item.getFileExtension());
                    StorageReference fileRef = storageRef.child(id);
                    Uri fileUri = Uri.fromFile(new File(item.getPath()));
//                    ------------------------
                    // Nếu là video, sử dụng putStream
                    if (item.getFileExtension().equalsIgnoreCase("mp4")) {
                        try {
                            InputStream inputStream = new FileInputStream(new File(item.getPath()));
                            UploadTask uploadTask = fileRef.putStream(inputStream);

                            // Tiếp tục xử lý tương tự như cho ảnh
                            uploadTask.addOnSuccessListener(taskSnapshot -> {
                                // ...
                                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String videoUrl = uri.toString();
                                    item.setUrl(videoUrl);
                                    //  lưu đường dẫn URL này trong cơ sở dữ liệu hoặc thực hiện các thao tác khác.

                                    mediaItemsRef.child(item.getId() + "").setValue(item)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Ghi dữ liệu thành công
                                                    System.out.println("đồng bộ và ghi dữ liệu thành công | mediaItems");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Xử lý lỗi
                                                    System.out.println("Có lỗi khi đồng bộ và ghi dữ liệu | mediaItems " + e.toString());
                                                }
                                            });

                            }).addOnFailureListener(exception -> {
                                // ...
                                    System.out.println("Error when upload video to cloud storage " + exception.getMessage());
                            });
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Nếu là ảnh, sử dụng putFile
                        UploadTask uploadTask = fileRef.putFile(fileUri);  // Tải lên ảnh lên Cloud Storage
                        uploadTask.addOnSuccessListener(taskSnapshot -> {
                            // Ảnh đã được tải lên thành công
                            // Lấy URL của ảnh trong Cloud Storage
                            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                item.setUrl(imageUrl);
                                //  lưu đường dẫn URL này trong cơ sở dữ liệu hoặc thực hiện các thao tác khác.

                                mediaItemsRef.child(item.getId() + "").setValue(item)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Ghi dữ liệu thành công
                                                System.out.println("đồng bộ và ghi dữ liệu thành công | mediaItems");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Xử lý lỗi
                                                System.out.println("Có lỗi khi đồng bộ và ghi dữ liệu | mediaItems " + e.toString());
                                            }
                                        });


                            });
                        }).addOnFailureListener(exception -> {
                            // Xử lý lỗi khi tải ảnh lên
                            System.out.println("Error when upload image to cloud storage " + exception.getMessage());
                        });
                    }

//                    ------------------------



                }
                System.out.println("Đã đồng bộ và ghi dữ liệu xong | images");
            }
        });

        // TODO: ghi dữ liệu avatarURL của User và coverPhotoURL của Album lên database, nếu có

    }




}




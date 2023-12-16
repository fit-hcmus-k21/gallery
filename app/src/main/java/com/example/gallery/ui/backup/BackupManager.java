package com.example.gallery.ui.backup;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.data.repositories.models.Repository.UserRepository;
import com.example.gallery.ui.main.doing.MainActivity;
import com.example.gallery.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BackupManager {


    /**
     * Backup dữ liệu người dùng lên Firebase Realtime Database
     *
     * @param userInfo   Thông tin người dùng
     * @param albums     Danh sách album
     * @param mediaItems Danh sách media item
     */

    private static User  userInfo;
    private static List<Album> albums = new ArrayList<>();
    private static List<MediaItem> mediaItems = new ArrayList<>();

    // Xác định DatabaseReference cho người dùng, albums, và mediaItems
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference usersRef = database.getReference("users").child(AppPreferencesHelper.getInstance().getCurrentUserId());
    private static DatabaseReference userInfoRef = usersRef.child("user_info");
    private static DatabaseReference userData = usersRef.child("user_data");
    private static DatabaseReference albumsRef = userData.child("albums");
    private static DatabaseReference mediaItemsRef = userData.child("media_items");

    private static CountDownLatch latch = new CountDownLatch(1); // Khởi tạo CountDownLatch với giá trị ban đầu


    private static final Object lock = new Object();    // Đối tượng lock để đồng bộ hóa

    public static void restoreUserInfo() {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here

                userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            // Xử lý dữ liệu người dùng ở đây
                            if (dataSnapshot.exists()) {
                                // Data exists, try to parse it into User object
                                try {
                                    User user = dataSnapshot.getValue(User.class);
                                    if (user != null) {
                                        // User exists, handle it accordingly
                                        userInfo = user;
                                        System.out.println("User info: " + user.getId() + " " + user.getFullName() + " " + user.getEmail() + " " + user.getCreationDate());


                                        if (userInfo.getAvatarURL() != null && !userInfo.getAvatarURL().isEmpty()) {
                                            // TODO: Tải avatar từ Cloud Storage về local storage
                                            // ...


                                        }
                                        UserRepository.getInstance().update(userInfo);
                                        System.out.println("updated user info completed");
//                                        latch.countDown(); // Giảm giá trị của CountDownLatch đi 1

                                    } else {
                                        // Handle case where User is null
                                        System.out.println("User is null, data snapshot exists " + dataSnapshot);
                                    }
                                } catch (Exception e) {
                                    // Handle exception during parsing
                                    System.out.println("Error parsing User data: " + e.getMessage());
                                }
                            } else {
                                // Handle case where dataSnapshot does not exist
                                System.out.println("Data does not exist");
                            }

                        }



                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Xử lý khi đọc dữ liệu người dùng thất bại
                        System.out.println("Failed to read user data");
                    }
                });

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });

    }

    public static void restoreAlbums() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here

                albumsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Xử lý dữ liệu albums ở đây
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                // Lấy key của node, chính là name của album
                                String albumName = itemSnapshot.getKey();


                                // Lấy dữ liệu từ node và ánh xạ vào đối tượng Album
                                Album album = itemSnapshot.getValue(Album.class);

                                if (album != null) {
                                    albums.add(album);
                                    System.out.println("Album: " + album.getName());

                                    // TODO: Tải ảnh bìa của album từ Cloud Storage về local storage nếu có
                                    // ...

                                    AlbumRepository.getInstance().insert(album);
                                    System.out.println("Insert after restore album completed " + album.getName());

                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Xử lý khi đọc dữ liệu albums thất bại
                        System.out.println("Failed to read albums data");
                    }
                });

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here
                    }
                });
            }
        });

    }



    public static void restoreMediaItems() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // background work here

                mediaItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Xử lý dữ liệu media items ở đây
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                // Lấy key của node, chính là id của media item
                                int mediaItemId = Integer.parseInt(itemSnapshot.getKey());

                                // Lấy dữ liệu từ node và ánh xạ vào đối tượng MediaItem
                                MediaItem mediaItem = itemSnapshot.getValue(MediaItem.class);

                                if (mediaItem != null) {
                                    mediaItems.add(mediaItem);
                                    System.out.println("Media item: " + mediaItem.getId());

                                    // TODO: Tải file media item từ Cloud Storage về local storage theo url
                                    String cloudStoragePath = Utils.getStoragePathFile(mediaItem.getId() + "", mediaItem.getCreationDate(), mediaItem.getFileExtension());

                                    RestoreFile(cloudStoragePath, mediaItem.getPath());

                                    MediaItemRepository.getInstance().insert(mediaItem);
                                    System.out.println("Insert after restore media item completed " + mediaItem.getName());

                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Xử lý khi đọc dữ liệu media items thất bại
                        System.out.println("Failed to read media items data");
                    }
                });

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UI thread work here


                    }
                });
            }
        });



    }





    /**
     * Tải file từ Cloud Storage về local storage
     *
     * @param cloudStoragePath Đường dẫn đến file trên Cloud Storage
     * @param localBackupPath  Đường dẫn đến file trên local storage
     */

    // TODO: download file from cloud storage
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static StorageReference storageRef = storage.getReference().child(AppPreferencesHelper.getInstance().getCurrentUserId());
    public static void RestoreFile(String cloudStoragePath, String localBackupPath) {

        System.out.println("Cloud storage path: " + cloudStoragePath);
        System.out.println("Local backup path: " + localBackupPath);


        // Tạo reference đến file trên Cloud Storage
        StorageReference fileRef = storageRef.child(cloudStoragePath);

        // Tạo đối tượng File cho local storage
        File localFile = new File(localBackupPath);

        // Bắt đầu quá trình tải file từ Cloud Storage
        fileRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // File đã được tải thành công, thực hiện các thao tác sau khi backup
                    // Ví dụ: thông báo backup thành công, cập nhật UI, vv.
                    System.out.println("Restore file completed");

                    // TODO: Thực hiện các thao tác sau khi backup thành công
                })
                .addOnFailureListener(exception -> {
                    // Xử lý lỗi khi tải file từ Cloud Storage
                    // Ví dụ: thông báo lỗi, ghi log, vv.
                    System.out.println("Restore file failed " + exception.getMessage());

                    // TODO: Xử lý lỗi khi backup
                });
    }

    public static void RestoreCloudToLocal() {
        // TODO: chờ đến khi restore user_info và user_data thành công

        // Tạo một ExecutorService với số luồng tùy chỉnh
        int numberOfThreads = 3; // Số luồng muốn sử dụng
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);



        // Tạo danh sách các Callable (các tác vụ cần thực hiện)
        List<Callable<Void>> tasks = new ArrayList<>();


        // Thêm tác vụ 1 vào danh sách
        tasks.add(() -> {
            try {
                System.out.println("Restore user info started");
                // Thực hiện tác vụ 1
                restoreUserInfo();
                System.out.println("Restore user info completed");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        // Thêm tác vụ 2 vào danh sách
        tasks.add(() -> {
            try {
                System.out.println("Restore albums started");
                // Thực hiện tác vụ 2
                restoreAlbums();
                System.out.println("Restore albums completed");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });


        // Thêm tác vụ 3 vào danh sách
        tasks.add(() -> {
            try {
                System.out.println("Restore media items started");
                // Thực hiện tác vụ 3
                restoreMediaItems();
                System.out.println("Restore media items completed");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        try {
            // Thực hiện tất cả các tác vụ và chờ chúng hoàn thành
            List<Future<Void>> futures = executor.invokeAll(tasks);


            // Đóng ExecutorService
            executor.shutdown();




            System.out.println("Restore completed");

        } catch ( Exception e) {
            e.printStackTrace();
        }


        // TODO: bắt đầu tải các files từ Cloud Storage về local storage
//        try {
//        latch.await(); // Chờ cho đến khi CountDownLatch có giá trị bằng 0

//            System.out.println("Restore completed 328");


//            if (userInfo.getAvatarURL() != null && !userInfo.getAvatarURL().isEmpty()) {
//                // TODO: Tải avatar từ Cloud Storage về local storage
//                // ...
//
//                UserRepository.getInstance().insertUser(userInfo);
//                System.out.println("Insert user info completed");
//            }


//            if (albums != null) {
//                for (Album album : albums) {
//                    // TODO: Tải ảnh bìa của album từ Cloud Storage về local storage nếu có
//                    // ...
//
//                    AlbumRepository.getInstance().insert(album);
//                    System.out.println("Insert album completed " + album.getName());
//
//
//                }
//            }
//
//            // Số luồng  muốn sử dụng
//            numberOfThreads = mediaItems.size() ;
//
//            // Tạo một ExecutorService với số luồng cho trước
//            executor = Executors.newFixedThreadPool(numberOfThreads);
//
//            if (mediaItems != null) {
//                for (MediaItem mediaItem : mediaItems) {
//                    // Submit mỗi MediaItem vào ExecutorService để thực hiện trên các luồng khác nhau
//                    executor.submit(() -> {
//                        // TODO: Tải file media item từ Cloud Storage về local storage theo url
//                        // ...
//                        RestoreFile(mediaItem.getUrl(), mediaItem.getPath());
//
//                        // TODO: Thực hiện các thao tác cần thiết (ví dụ: insert vào repository)
//                        MediaItemRepository.getInstance().insert(mediaItem);
//                        System.out.println("Insert media item completed " + mediaItem.getName());
//                    });
//                }
//            }
//
//            // Đóng ExecutorService sau khi đã submit tất cả các công việc
//            executor.shutdown();
//
//
////            if (mediaItems != null) {
////                for (MediaItem mediaItem : mediaItems) {
////                    // TODO: Tải file media item từ Cloud Storage về local storage theo url
////                    // ...
////                    RestoreFile(mediaItem.getUrl(), mediaItem.getPath());
////
////                    MediaItemRepository.getInstance().insert(mediaItem);
////                    System.out.println("Insert media items completed " + mediaItem.getName());
////
////                }
////            }
//
//        } catch (Exception e) {
//            Thread.currentThread().interrupt();
//            e.printStackTrace();
//        }



    }

}


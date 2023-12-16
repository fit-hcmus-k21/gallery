package com.example.gallery.data.repositories.models.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.gallery.App;
import com.example.gallery.data.local.db.AppDatabase;
import com.example.gallery.data.local.db.dao.MediaItemDao;
import com.example.gallery.data.local.db.GalleryDatabase;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.HelperFunction.MediaItemFromExternalStorage;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MediaItemRepository {
    private MediaItemDao mediaItemDao;
    LiveData<List<MediaItem>> allMediaItem;
    LiveData<Integer> totalMediaItems;
    Application application;

    private static MediaItemRepository currentMediaItemRepository;

    public static MediaItemRepository getInstance(){
        if(currentMediaItemRepository == null){
            currentMediaItemRepository = new MediaItemRepository(App.getInstance());
        }
        return currentMediaItemRepository;
    }

    public MediaItemRepository(Application application) {
        this.application = application;
        mediaItemDao = AppDatabase.getInstance().mediaItemDao();
        //  System.out.println("on media repos 48");
        allMediaItem = mediaItemDao.getAllMediaItems(AppPreferencesHelper.getInstance().getCurrentUserId());
        totalMediaItems = mediaItemDao.getNumberOfMediaItems(AppPreferencesHelper.getInstance().getCurrentUserId());
        //  System.out.println("on media repos 52");



    }

    public void fetchData(LifecycleOwner lifecycleOwner) {
        allMediaItem.observe(lifecycleOwner, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                // Dữ liệu đã được truy vấn và có sẵn trong allMediaItem
                if (mediaItems == null) {
                    return;
                }

                Log.d("Mytask", "Checking mediaItemDb: " + mediaItems.size());

                ExecutorService executorService = Executors.newFixedThreadPool(2);
                Future<List<MediaItem>> futureExternal = executorService.submit(new Callable<List<MediaItem>>() {
                    @Override
                    public List<MediaItem> call() throws Exception {
                        return MediaItemFromExternalStorage.listMediaItems(application);
                    }
                });

                try {
                    List<MediaItem> mediaItemsFromExternalStorage = futureExternal.get();
                    Log.d("Mytask", "Checking mediafromexternalstorage size: " + mediaItemsFromExternalStorage.size());

                    for (MediaItem mediaItemExternal : mediaItemsFromExternalStorage) {
                        for (MediaItem mediaItemDb : mediaItems) {
                            if (mediaItemExternal.getId() == mediaItemDb.getId()) {
                                if (mediaItemDb.getDeletedTs() != 0) {
                                    mediaItemExternal.setDeletedTs(mediaItemDb.getDeletedTs());
                                }
                                if (!mediaItemDb.getDescription().isEmpty()) {
                                    mediaItemExternal.setDescription(mediaItemDb.getDescription());
                                }
                                if (!mediaItemDb.getTag().isEmpty()) {
                                    mediaItemExternal.setTag(mediaItemDb.getTag());
                                }
                            }
                        }
                    }

                    insertAll(mediaItemsFromExternalStorage);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void fetchDataFromExternalStorage() {
            System.out.println("Lấy dữ liệu từ external storage và insert vào db");
        LiveData<List<MediaItem>> liveData = allMediaItem;

        liveData.observeForever(new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                // Dữ liệu đã được truy vấn và có sẵn trong allMediaItem
                Log.d("Mytask", "Checking mediaItemDb: " + mediaItems.size());
                // Gỡ bỏ observer sau khi đã kiểm tra dữ liệu
                liveData.removeObserver(this);

                ExecutorService executorService = Executors.newFixedThreadPool(2);
                Future<List<MediaItem>> futureExternal = executorService.submit(new Callable<List<MediaItem>>() {
                    @Override
                    public List<MediaItem> call() throws Exception {
                        List<MediaItem> mediaItems = MediaItemFromExternalStorage.listMediaItems(application);
                        return mediaItems;
                    }
                });
                try {
                    List<MediaItem> mediaItemsFromExternalStorage = futureExternal.get();
                    Log.d("Mytask", "Checking mediafromexternalstorage size: " + mediaItemsFromExternalStorage.size());

                    if(mediaItems != null){

                        for(MediaItem mediaItemExternal : mediaItemsFromExternalStorage){

                            for(MediaItem mediaItemDb : mediaItems){

                                if(mediaItemExternal.getId() == mediaItemDb.getId()){
                                    if(mediaItemDb.getDeletedTs() != 0){
                                        mediaItemExternal.setDeletedTs(mediaItemDb.getDeletedTs());
                                    }
                                    if(mediaItemDb.getDescription() != ""){
                                        mediaItemExternal.setDescription(mediaItemDb.getDescription());

                                    }
                                    if(mediaItemDb.getTag() != ""){
                                        mediaItemExternal.setTag(mediaItemDb.getTag());
                                    }
                                }
                            }
                        }
                    }
                    insertAll(mediaItemsFromExternalStorage);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });


    }
    public LiveData<List<MediaItem>> getAllMediaItems(){

        return allMediaItem;
    }

    public synchronized void insert(MediaItem mediaItem) {
        // Check if album name exists
        String albumName = mediaItem.getAlbumName();
        boolean albumExists = AlbumRepository.getInstance().isExistAlbum(albumName);

        if (!albumExists) {
            // Album does not exist, create and insert
            Album album = new Album();
            album.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
            album.setName(albumName);

            AlbumRepository.getInstance().insert(album);
        }

        // Media item can be inserted without waiting for the album insertion
//        //  System.out.println("MediaItemRepository: insert: " + mediaItem.getId() + " " + mediaItem.getUserID() + " " + albumName);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            // Use synchronized block to ensure proper order of insertions
            synchronized (this) {
                // Show id, userID, album name
                //  System.out.println("MediaItemRepository: insert on run: 121" + mediaItem.getPath());
                mediaItemDao.insert(mediaItem);
                //  System.out.println("after insert in Media Repos: 123");
            }
            executorService.shutdown();
        });
    }

    public  void insertAll(List<MediaItem> mediaItems) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                // Tạo một ExecutorService với số lượng luồng là 4
                ExecutorService executorService = Executors.newFixedThreadPool(4);

                for (MediaItem mediaItem : mediaItems) {
                    executorService.execute(() -> {

                            try {
                                mediaItemDao.insert(mediaItem);
                            } catch (Exception e) {
                                System.out.println("MediaItemRepository: insert on run:  " + e.getMessage());
                            }

                    });
                }

            // Đợi cho tất cả các luồng kết thúc
                executorService.shutdown();
                try {
                    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        Set<String> uniqueAlbumNames = new HashSet<>();

        // Lấy ra danh sách albumName và thêm vào uniqueAlbumNames
        for (MediaItem mediaItem : mediaItems) {
            String albumName = mediaItem.getAlbumName();
            uniqueAlbumNames.add(albumName);
        }

        // Kiểm tra và chèn album nếu không tồn tại
        for (String albumName : uniqueAlbumNames) {
            boolean albumExists = AlbumRepository.getInstance().isExistAlbum(albumName);

            if (!albumExists) {
                // Album does not exist, create and insert
                Album album = new Album();
                album.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                album.setName(albumName);

                AlbumRepository.getInstance().insert(album);
            }
        }


    }





    public void delete(List<MediaItem> mediaItems){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.delete(mediaItems);
            }
        });
    }

    public LiveData<List<MediaItem>> getAllMediaItemsByUserID(String userID){
        return mediaItemDao.getAllMediaItemsByUserID(userID);
    }
    public LiveData<List<MediaItem>> getMediaFromPath(String path){
        return mediaItemDao.getMediaFromPath(path);
    }

    public void updateMediaItemAlbum(int id, String newAlbum){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.updateMediaItemAlbum(id,newAlbum);
            }
        });
    }

    public void updateMediaItemDeleteTs(int id, long newTime){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.updateMediaItemDeleteTs(id,newTime);
            }
        });
    }

    public void updateMediaPreviousAlbum(int id, String previous){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.updateMediaPreviousAlbum(id,previous);
            }
        });
    }
    public void updateDeletedTs(int id, long deletedTs){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.updatedDeletedTs(id, deletedTs);
            }
        });
    }
    public void deleteMediaItemFromPath(String path){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.deleteMediaItemPath(path);
            }
        });
    }
    public void updateMediaItem(int id, String newFileName, String newPath, String newParentPath){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.updateMediaItem(id, newFileName, newPath, newParentPath);
            }
        });
    }
    public void updateFavorite(int id, boolean favorite){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.updateFavorite(id, favorite);
            }
        });
    }
    public void clearAllFavorite(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.clearAllFavorite();
            }
        });
    }
    public void clearAllRecycleBin(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.clearAllRecycleBin();
            }
        });
    }
    public void clearRecycleBinItemWithId(int id){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.clearRecycleBinItemWithId(id);
            }
        });
    }
    public void updateMediaItemDescription(int id, String description){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.updateMediaItemDescription(id, description);
            }
        });
    }
    public void updateMediaItemTag(int id, String tag){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.updateMediaItemTag(id, tag);
            }
        });
    }

    public LiveData<Integer> getNumberOfMediaItems(){
        return mediaItemDao.getNumberOfMediaItems(AppPreferencesHelper.getInstance().getCurrentUserId());
    }

    public void updateAlbumName(String oldAlbumName, String newAlbumName){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.updateAlbumName(AppPreferencesHelper.getInstance().getCurrentUserId(), oldAlbumName, newAlbumName);
            }
        });
    }
    public LiveData<List<MediaItem>> getAllMediaItemsByAlbumName(String albumName) {
        return mediaItemDao.getAllMediaItemsByAlbumName(AppPreferencesHelper.getInstance().getCurrentUserId(), albumName);
    }
    public LiveData<List<MediaItem>> getDeletedMediaItems(){
        return mediaItemDao.getDeletedMediaItems(AppPreferencesHelper.getInstance().getCurrentUserId());
    }
    public LiveData<List<MediaItem>> getAllFavoriteMediaItem(String userID){
        return mediaItemDao.getAllFavoriteMediaItem(AppPreferencesHelper.getInstance().getCurrentUserId(), true);
    }
    public LiveData<List<MediaItem>> getAllPublicMediaItem(String userID) {
        return mediaItemDao.getAllPublicMediaItem(userID);
    }
}

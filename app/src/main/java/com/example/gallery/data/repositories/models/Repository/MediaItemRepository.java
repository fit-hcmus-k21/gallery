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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        System.out.println("on media repos 48");
        allMediaItem = mediaItemDao.getAllMediaItems(AppPreferencesHelper.getInstance().getCurrentUserId());
        totalMediaItems = mediaItemDao.getNumberOfMediaItems(AppPreferencesHelper.getInstance().getCurrentUserId());
        System.out.println("on media repos 52");



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

    public void fetchData() {

        allMediaItem.observeForever(new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                // Dữ liệu đã được truy vấn và có sẵn trong allMediaItem
                Log.d("Mytask", "Checking mediaItemDb: " + mediaItems.size());
                // Gỡ bỏ observer sau khi đã kiểm tra dữ liệu
                allMediaItem.removeObserver(this);

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
        System.out.println("MediaItemRepository: insert: " + mediaItem.getId() + " " + mediaItem.getUserID() + " " + albumName);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            // Use synchronized block to ensure proper order of insertions
            synchronized (this) {
                // Show id, userID, album name
                System.out.println("MediaItemRepository: insert on run: 121" + mediaItem.getPath());
                mediaItemDao.insert(mediaItem);
                System.out.println("after insert in Media Repos: 123");
            }
            executorService.shutdown();
        });
    }

    public  void insertAll(List<MediaItem> mediaItems) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                for (MediaItem item : mediaItems) {
                    String albName = item.getAlbumName();
                    if (!AlbumRepository.getInstance().isExistAlbum(albName)) {
                        Album alb = new Album();
                        alb.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                        alb.setName(albName);

                        AlbumRepository.getInstance().insert(alb);
                    }
                    mediaItemDao.insert(item);
                }
                executorService.shutdown();
            }

        });
    }


    public void _insert(MediaItem mediaItem){
        // if album name not exist in albums table, then insert alb
        if (AlbumRepository.getInstance().isExistAlbum(mediaItem.getAlbumName()) == false) {
            Album alb = new Album();
            alb.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
            alb.setName(mediaItem.getAlbumName());

            AlbumRepository.getInstance().insert(alb);

        }
            System.out.println("MediaItemRepository: insert: " + mediaItem.getId() + " " + mediaItem.getUserID() + " " + mediaItem.getAlbumName());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // show id, userID, album name
                    System.out.println("MediaItemRepository: insert on run: 121" + mediaItem.getPath());
                    mediaItemDao.insert(mediaItem);
                    System.out.println("after insert in Media Repos: 123");
                }
            });


    }
//    public synchronized void insertAll(List<MediaItem> mediaItems) {
//        for (MediaItem item : mediaItems) {
//            String albName = item.getAlbumName();
//            if (!AlbumRepository.getInstance().isExistAlbum(albName)) {
//                Album alb = new Album();
//                alb.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
//                alb.setName(albName);
//
//                AlbumRepository.getInstance().insert(alb);
//            }
//        }
//
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
//            // Use synchronized block to ensure proper order of insertions
//            synchronized (this) {
//                for (MediaItem item : mediaItems) {
//                    mediaItemDao.insert(item);
//                }
//            }
//            executorService.shutdown();
//        });

//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        MediaItemDao localMediaItemDao = mediaItemDao;  // Tạo một biến cục bộ cuối cùng
//        executorService.execute(() -> {
//            localMediaItemDao.insertAll(mediaItems);  // Sử dụng biến cục bộ trong lambda
//            executorService.shutdown();
//        });
//    }


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

}

package com.example.gallery.data.repositories.models.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.gallery.data.local.dao.MediaItemDao;
import com.example.gallery.data.local.db.GalleryDatabase;
import com.example.gallery.data.local.entities.MediaItem;
import com.example.gallery.data.repositories.models.HelperFunction.MediaItemFromExternalStorage;

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
    Application application;

    public MediaItemRepository(Application application) {
        this.application = application;
        GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(application);
        mediaItemDao = galleryDatabase.mediaItemDao();
        allMediaItem = mediaItemDao.getAllMediaItems();
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
                                    if(mediaItemDb.getDeletedTs() != 0 || mediaItemDb.getDescription() != ""){
                                        Log.d("Mytask", "Checking mediaItemDb: " + mediaItemDb.getName());
                                        mediaItemExternal.setDeletedTs(mediaItemDb.getDeletedTs());
                                        mediaItemExternal.setDescription(mediaItemDb.getDescription());
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
    public void insert(MediaItem mediaItem){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.insert(mediaItem);
            }
        });
    }
    public void insertAll(List<MediaItem> mediaItems){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItemDao.insertAll(mediaItems);
            }
        });
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

    public LiveData<List<MediaItem>> getAllMediaItemsByUserID(int userID){
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

}

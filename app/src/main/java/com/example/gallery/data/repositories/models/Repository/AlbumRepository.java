package com.example.gallery.data.repositories.models.Repository;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.gallery.App;
import com.example.gallery.data.local.db.AppDatabase;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.local.db.dao.AlbumDao;
import com.example.gallery.data.repositories.models.HelperFunction.AlbumFromExternalStorage;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AlbumRepository {
    private AlbumDao albumDao;
    private LiveData<List<Album>> allAlbums ;
    Application application;

    private static AlbumRepository currentAlbumRepository;

    public static AlbumRepository getInstance(){
        if(currentAlbumRepository == null){
            currentAlbumRepository = new AlbumRepository(App.getInstance());
        }
        return currentAlbumRepository;
    }
    public AlbumRepository(Application application){
        this.application = application;
        AppDatabase galleryDatabase = AppDatabase.getInstance();
        albumDao = galleryDatabase.albumDao();
        allAlbums = albumDao.getAllAlbums(AppPreferencesHelper.getInstance().getCurrentUserId());
    }

    public LiveData<List<Album>> getAlbums(){

        return allAlbums;
    }

    public boolean isExistAlbum(String albName) {
        LiveData<List<Album>> albumsLiveData = getAlbums();

        if (albumsLiveData != null && albumsLiveData.getValue() != null) {
            for (Album album : albumsLiveData.getValue()) {
                if (album != null && album.getName() != null && album.getName().equals(albName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void fetchData(){
        allAlbums.observeForever(new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albumsDatabase) {
                allAlbums.removeObserver(this); // Cái dòng này quan trong vãi cả nồi @@ không có nó thì hiệu suất giảm đáng kể
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Future<List<Album>> futureExternal = executorService.submit(new Callable<List<Album>>() {
                    @Override
                    public List<Album> call() throws Exception {
                        List<Album> albums = AlbumFromExternalStorage.listAlbums(application);
                        return  albums;
                    }
                });

                try {
                    List<Album> albumsExternal = futureExternal.get();
                    if(albumsDatabase != null){
                        if(albumsExternal != null){
                            for(Album albumExternal : albumsExternal){
                                for(Album albumDatabase : albumsDatabase){
                                    if(albumExternal.getPath().equals(albumDatabase.getPath())){
                                        if(albumDatabase.getDeletedTs() != 0){
                                            albumExternal.setDeletedTs(albumDatabase.getDeletedTs());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(albumsExternal != null){
                        insertAll(albumsExternal);
                    }
                } catch (ExecutionException e) {
                    System.out.println("Error insert all album");
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    System.out.println("Error insert all album 2");

                    throw new RuntimeException(e);
                }

            }
        });



    }


    public void insertAll(List<Album> album){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
//                albumDao.insertAll(album);
            }
        });
    }
    public void insert(Album album){


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance().runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        albumDao.insert(album);
                    }
                });
            }
        });
    }
    public void deleteAlbumPath(String path){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                albumDao.deleteAlbumPath(path);
            }
        });
    }
    public void updateAlbumCoverPhotoPath(String path, String newCoverPhotoPath){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                albumDao.updateAlbumCoverPhotoPath(path, newCoverPhotoPath);
            }
        });
    }
    public void updateAlbumAfterRename(String oldPath, String newName, String newCoverPhotoPath, String newPath){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                albumDao.updateAlbumAfterRename(oldPath, newName, newCoverPhotoPath, newPath);
            }
        });
    }
    public String getAlbumCoverPhotoPath(String path){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String coverPhotoPath = albumDao.getAlbumCoverPhotoPath(path);
                return coverPhotoPath;
            }
        });
        try {
            String coverPhotoPath = future.get();
            return coverPhotoPath;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateAlbumDeletedTs(String path, long deletedTs){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                albumDao.updateAlbumDeletedTs(path, deletedTs);
            }
        });
    }

//    --------------------------------------------
    public LiveData<Integer> getNumberOfAlbums(){
        return albumDao.getNumberOfAlbums(AppPreferencesHelper.getInstance().getCurrentUserId());
    }

}

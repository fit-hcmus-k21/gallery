package com.example.gallery.data.repositories.models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.App;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;

import java.util.List;

public class AlbumViewModel extends AndroidViewModel {
    private AlbumRepository albumRepository;
    private LiveData<List<Album>> allAlbums;

    private static AlbumViewModel currentAlbumViewModel;

    public static AlbumViewModel getInstance(){
        if(currentAlbumViewModel == null){
            currentAlbumViewModel = new AlbumViewModel(App.getInstance());
        }
        return currentAlbumViewModel;
    }

    public AlbumViewModel(@NonNull Application application) {
        super(application);
        albumRepository = AlbumRepository.getInstance();
        allAlbums = albumRepository.getAlbums();
    }
    public void fetchData(){
        albumRepository.fetchData();
    }
    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }
    public void insertAll(List<Album> albums){
        albumRepository.insertAll(albums);
    }
    public void insert(Album album){
        albumRepository.insert(album);
    }
    public void deleteAlbumPath(String path){
        albumRepository.deleteAlbumPath(path);
    }

    public void updateAlbumAfterRename(String oldPath, String newName, String newCoverPhotoPath, String newPath){
        albumRepository.updateAlbumAfterRename(oldPath, newName, newCoverPhotoPath, newPath);
    }
    public String getAlbumCoverPhotoPath(String path){
        return albumRepository.getAlbumCoverPhotoPath(path);
    }
    public void updateAlbumDeletedTs(String path, long deletedTs){
        albumRepository.updateAlbumDeletedTs(path, deletedTs);
    }

    public void insertAlbum(Album album){
        albumRepository.insert(album);
    }

}

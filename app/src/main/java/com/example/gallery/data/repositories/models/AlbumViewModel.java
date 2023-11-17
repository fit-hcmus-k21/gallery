package com.example.gallery.data.repositories.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.data.local.entities.Album;

import java.util.List;

public class AlbumViewModel extends AndroidViewModel {
    private AlbumRepository albumRepository;
    private LiveData<List<Album>> allAlbums;
    public AlbumViewModel(@NonNull Application application) {
        super(application);
        albumRepository = new AlbumRepository(application);
        allAlbums = new MutableLiveData<>();
    }
    public void fetchDataFromExternalStorageAndSaveToDataBase(){
        albumRepository.fetchDataFromExternalStorageAndSaveToDataBase();
        allAlbums = albumRepository.getAlbums();
    }
    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }
}

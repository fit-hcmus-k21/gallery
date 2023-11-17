package com.example.gallery.data.repositories.models;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.data.local.entities.MediaItem;

import java.util.List;

public class MediaItemViewModel extends AndroidViewModel {
    private MediaItemRepository mediaItemRepository;
    LiveData<List<MediaItem>> allMediaItems;

    public MediaItemViewModel(Application application) {
        super(application);
        mediaItemRepository = new MediaItemRepository(application);
        allMediaItems = new MutableLiveData<>();
    }

    public void fetchDataFromExternalStorageAndSaveToDataBase(){
        Log.e("MyTag", "getImages: " + allMediaItems.getValue());

        mediaItemRepository.fetchDataFromExternalStorageAndSaveToDataBase();
        allMediaItems = mediaItemRepository.getImages();
    }
    public LiveData<List<MediaItem>> getAllMediaItems() {
        return allMediaItems;
    }


}

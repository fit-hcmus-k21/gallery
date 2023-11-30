package com.example.gallery.data.repositories.models.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MediaItemViewModel extends AndroidViewModel {
    private MediaItemRepository mediaItemRepository;
    LiveData<List<MediaItem>> allMediaItems;

    public MediaItemViewModel(Application application) {
        super(application);
        mediaItemRepository = new MediaItemRepository(application);
        allMediaItems = mediaItemRepository.getAllMediaItems();
    }
    public void fetchData() {
        mediaItemRepository.fetchData();
    }
    public LiveData<List<MediaItem>> getAllMediaItems() {
        return allMediaItems;
    }
    public void insert(MediaItem mediaItem) {
        mediaItemRepository.insert(mediaItem);
    }
    public void insertAll(List<MediaItem> mediaItems) {
        mediaItemRepository.insertAll(mediaItems);
    }
    public void delete(List<MediaItem> mediaItems) {
        mediaItemRepository.delete(mediaItems);
    }
    public void updateDeletedTs(int id, long deletedTs) {
        mediaItemRepository.updateDeletedTs(id, deletedTs);
    }
    public void updateFavorite(int id, boolean favorite) {
        mediaItemRepository.updateFavorite(id, favorite);
    }
    public void deleteMediaItemFromPath(String path) {
        mediaItemRepository.deleteMediaItemFromPath(path);
    }
    public void updateMediaItem(int id, String newFileName, String newPath, String newParentPath) {
        mediaItemRepository.updateMediaItem(id, newFileName, newPath, newParentPath);
    }
    public void clearAllFavorite() {
        mediaItemRepository.clearAllFavorite();
    }
    public void clearAllRecycleBin() {
        mediaItemRepository.clearAllRecycleBin();
    }
    public void clearRecycleBinItemWithId(int id) {
        mediaItemRepository.clearRecycleBinItemWithId(id);
    }
    public void updateMediaItemDescription(int id, String description) {
        mediaItemRepository.updateMediaItemDescription(id, description);
    }
    public void updateMediaItemTag(int id, String tag) {
        mediaItemRepository.updateMediaItemTag(id, tag);
    }

}

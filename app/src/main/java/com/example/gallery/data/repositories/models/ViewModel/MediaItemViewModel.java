package com.example.gallery.data.repositories.models.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.App;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MediaItemViewModel extends AndroidViewModel {
    private MediaItemRepository mediaItemRepository ;
    LiveData<List<MediaItem>> allMediaItems ;

    private static MediaItemViewModel currentMediaItemViewModel;

    public static MediaItemViewModel getInstance(){
        if(currentMediaItemViewModel == null){
            currentMediaItemViewModel = new MediaItemViewModel(App.getInstance());
        }
        return currentMediaItemViewModel;
    }

    public MediaItemViewModel(Application application) {
        super(application);
        mediaItemRepository = MediaItemRepository.getInstance();
        allMediaItems = mediaItemRepository.getAllMediaItems();
    }
    public void fetchData(LifecycleOwner lifecycleOwner) {
        mediaItemRepository.fetchData(lifecycleOwner );
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

    public void updateMediaItemAlbum(int id, String newAlbum){
        mediaItemRepository.updateMediaItemAlbum(id,newAlbum);
    }

    public void updateMediaItemDeleteTs(int id, long newTime){
        mediaItemRepository.updateMediaItemDeleteTs(id,newTime);
    }

    public void updateMediaPreviousAlbum(int id, String previous){
        mediaItemRepository.updateMediaPreviousAlbum(id, previous);
    }

    public void updateMediaItemDescription(int id, String description) {
        mediaItemRepository.updateMediaItemDescription(id, description);
    }
    public void updateMediaItemTag(int id, String tag) {
        mediaItemRepository.updateMediaItemTag(id, tag);
    }

    public LiveData<Integer> getNumberOfMediaItems() {

        return mediaItemRepository.getNumberOfMediaItems();
    }

}

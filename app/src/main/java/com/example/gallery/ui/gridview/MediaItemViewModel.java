package com.example.gallery.ui.gridview;

import androidx.lifecycle.LiveData;

import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.ui.base.BaseViewModel;

import java.util.List;

public class MediaItemViewModel extends BaseViewModel<MediaItemNavigator> {
    public LiveData<List<MediaItem>> getAllMediaItems() {
        return MediaItemRepository.getInstance().getAllMediaItems();
    }
}

package com.example.gallery.utils;

import com.example.gallery.data.models.db.MediaItem;

import java.util.List;

public class GetInDexOfHelper {
    public static int getIndexOf(List<MediaItem> mediaItems, MediaItem mediaItem){
        for(int i = 0; i < mediaItems.size(); i++) {
            if(mediaItems.get(i).getPath().equals(mediaItem.getPath())){
                return i;
            }
        }
        return -1;
    }
}

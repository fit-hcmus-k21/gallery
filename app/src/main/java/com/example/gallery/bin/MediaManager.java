package com.example.gallery.bin;

import com.example.gallery.bin.DataSyncService;
import com.example.gallery.bin.MediaItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created on 28/10/2023
 */
public class MediaManager {
    // attributes
    private ArrayList<MediaItem> listMedia;
    private com.example.gallery.data.repositories.models.AlbumManager albumManager;
    private DataSyncService synchronizer;
    private PhotoEditor editor;

    // methods

    // sorters
    public void sortByTag() {

        // handle here

    }

    public void sortByDateCreated() {


        // handle here

    }

    public void sortByLocation() {

        // handle here


    }

    public void sortByFileExtension() {


        // handle here

    }

    // statistics
    public HashMap<String, Integer> statistics () {

        // handle here

        return new HashMap<>();
    }

    // add a new media item to list
    public void addMediaItemToList(MediaItem item) {

        // handle here

    }

    public void transferMediaItemToBin(MediaItem item) {

        // handle here
    }


}

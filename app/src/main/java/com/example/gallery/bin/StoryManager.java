package com.example.gallery.bin;

import com.example.gallery.data.repositories.models.Sound;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created on 28/10/2023
 */
public class StoryManager {
    // attributes
    private ArrayList<MediaItem> picsSelected;
    private HashMap<String, Sound> listSounds;

    // setters and getters

    public ArrayList<MediaItem> getPicsSelected() {
        return picsSelected;
    }

    public void setPicsSelected(ArrayList<MediaItem> picsSelected) {
        this.picsSelected = picsSelected;
    }

    public HashMap<String, Sound> getListSounds() {
        return listSounds;
    }

    public void setListSounds(HashMap<String, Sound> listSounds) {
        this.listSounds = listSounds;
    }

    // methods
    public boolean createStory() {

        // handle here

        return true;
    }
}

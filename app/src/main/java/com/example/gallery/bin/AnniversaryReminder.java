package com.example.gallery.bin;



import com.example.gallery.data.models.db.MediaItem;

import java.util.ArrayList;

/**
 * Created on 28/10/2023
 */

public class AnniversaryReminder {

    // attributes
    private ArrayList<MediaItem> events;

    // constructors
    public AnniversaryReminder() {

    }

    public AnniversaryReminder(ArrayList<MediaItem> events) {
        this.events = events;
    }

    // setters

    public void setEvents(ArrayList<MediaItem> events) {
        this.events = events;
    }


    // getters

    public ArrayList<MediaItem> getEvents() {
        return events;
    }


    // other methods
    public static void addEvent(MediaItem item) {

        // handle here

    }

    public static void removeEvent(MediaItem item) {


        // handle here
    }

    public static ArrayList<MediaItem> getEventsForToday() {

        // handle here

        return new ArrayList<>();
    }

    public static void scheduleReminders() {


        // handle here

    }
}

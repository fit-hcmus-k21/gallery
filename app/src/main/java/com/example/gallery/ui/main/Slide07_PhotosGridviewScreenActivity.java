package com.example.gallery.ui.main;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Slide07_PhotosGridviewScreenActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide07_photos_gridview_screen);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // fake data
        ArrayList<HashMap<String, ArrayList<Integer>>> groups = new ArrayList<>();

        ArrayList<Integer> thumbnails = new ArrayList<>();
        thumbnails.add(R.drawable.icon_photo_01);
        thumbnails.add(R.drawable.icon_photo_01);
        thumbnails.add(R.drawable.icon_photo_01);
        thumbnails.add(R.drawable.icon_photo_01);
        thumbnails.add(R.drawable.icon_photo_01);

        HashMap<String, ArrayList<Integer>> entry1 = new HashMap<>();
        entry1.put("22/12/2021", thumbnails);

        HashMap<String, ArrayList<Integer>> entry2 = new HashMap<>();
        entry2.put("03/11/2021", thumbnails);

        HashMap<String, ArrayList<Integer>> entry3 = new HashMap<>();
        entry3.put("30/10/2021", thumbnails);

        groups.add(entry1);
        groups.add(entry2);
        groups.add(entry3);

        ImageGroupAdapter imageGroupAdapter = new ImageGroupAdapter(groups);
        recyclerView.setAdapter(imageGroupAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
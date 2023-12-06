package com.example.gallery.ui.main.doing;

import static java.util.Collections.sort;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.main.adapter.MainMediaItemAdapter;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ArrangementAction extends AppCompatActivity {
    RecyclerView sortRecyclerView;
    MediaItemViewModel mediaItemViewModel;
    HashMap<String, List<MediaItem>> mediaItemGroupToSort;
    private MainMediaItemAdapter mainMediaItemAdapter;
    List<String> factorListString;
    String kind = "", factor ="", albumName = "";
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.sort_screen);

        sortRecyclerView = findViewById(R.id.sort_recyclerView);
        mediaItemViewModel = ViewModelProviders.of(this).get(MediaItemViewModel.class);

        //lấy dữ liệu từ intent
        Intent receive = getIntent();
        Bundle data = receive.getExtras();
        kind = data.getString("Kind");
        factor = data.getString("Factor");
        albumName = data.getString("Album");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        sortRecyclerView.setLayoutManager(layoutManager);

        mainMediaItemAdapter = new MainMediaItemAdapter();
        sortRecyclerView.setAdapter(mainMediaItemAdapter);

        //get all MediaItem of this album
       mediaItemViewModel.getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
           @Override
           public void onChanged(List<MediaItem> mediaItems) {
               List<MediaItem> rawdata = new ArrayList<>();
               for(MediaItem i : mediaItems){
                   if(i.getAlbumName().equals(albumName))
                       rawdata.add(i);
               }

               //extract data
               if(factor.equals("Location")){
                   SortByLocation(rawdata,kind);
               }
               else{
                   SortByDate(rawdata,kind);
               }
               factorListString = setFactorListString(rawdata,factor);
               if(kind.equals("Ascending")){
                   sort(factorListString);
               }
               else{
                   sort(factorListString,Collections.reverseOrder());
               }



               mediaItemGroupToSort = setMediaItemGroupToSort(factorListString,rawdata,factor);
               mainMediaItemAdapter.setData(rawdata,mediaItemGroupToSort,factorListString);
           }
       });



    }

    public void SortByDate(List<MediaItem> input, String kind){
        if(kind == "Ascending"){
            sort(input, new Comparator<MediaItem>() {
                @Override
                public int compare(MediaItem o1, MediaItem o2) {
                    return o1.getCreationDate().compareTo(o2.getCreationDate());
                }
            });

        }
        else{
            sort(input, new Comparator<MediaItem>() {
                @Override
                public int compare(MediaItem o1, MediaItem o2) {
                    return o2.getCreationDate().compareTo(o1.getCreationDate());
                }
            });
        }
    }
    public void SortByLocation(List<MediaItem> input, String kind){
        if(kind == "Ascending"){
            sort(input, new Comparator<MediaItem>() {
                @Override
                public int compare(MediaItem o1, MediaItem o2) {
                    return o2.getLocation().compareTo(o1.getLocation());
                }
            });
        }
        else{
            sort(input, new Comparator<MediaItem>() {
                @Override
                public int compare(MediaItem o1, MediaItem o2) {
                    return o1.getLocation().compareTo(o2.getLocation());
                }
            });
        }
    }

    public List<String> setFactorListString(List<MediaItem> input, String kind){
        HashSet<String> factor = new HashSet<>();
        if(kind.equals("Location")){
            for(MediaItem iterator: input){
                factor.add(iterator.getLocation());
            }
        }
        else{
            for(MediaItem iterator : input){
                factor.add(new SimpleDateFormat("yyyy/MM/dd").format(iterator.getCreationDate()));
            }
        }
        List<String> output = new ArrayList<>(factor);
        return output;
    }

    public HashMap<String, List<MediaItem>> setMediaItemGroupToSort(List<String> factor, List<MediaItem> input, String kind){
        HashMap<String, List<MediaItem>> output = new HashMap<>();
        if(kind.equals("Location")){
            for(String i : factor) {
                output.put(i, new ArrayList<>());
            }
            for(MediaItem i : input){
               if(output.get(i.getLocation()) != null)
                    output.get(i.getLocation()).add(i);
            }
        }
        else{


            for(String i : factor)
                output.put(i,new ArrayList<>());
            for(MediaItem i : input){
                output.get(new SimpleDateFormat("yyyy/MM/dd").format(i.getCreationDate())).add(i);
            }
        }
        return output;
    }
}

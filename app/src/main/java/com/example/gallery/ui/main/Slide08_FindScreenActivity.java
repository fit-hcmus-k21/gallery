package com.example.gallery.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.google.api.services.drive.Drive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Slide08_FindScreenActivity extends AppCompatActivity{
    String[] info = {"This is information picture 1","This is information picture 2","This is information picture 3","This is information picture 4","This is information picture 5","This is information picture 6","This is information picture 7"};
    Integer[] pic = {R.drawable.icon_photo_01,R.drawable.icon_photo_01,R.drawable.icon_photo_01,R.drawable.icon_photo_01,R.drawable.icon_photo_01,R.drawable.icon_photo_01,R.drawable.icon_photo_01};
    ListView listAlbum;
    @Override
    public void onCreate(Bundle saveStateInstance){
        super.onCreate(saveStateInstance);
        setContentView(R.layout.slide08_find_screen);
        listAlbum = findViewById(R.id.list_Album);
        Slide08_ListAdapter adapter = new Slide08_ListAdapter(Slide08_FindScreenActivity.this,R.layout.slide08_image_list,info,pic);
        listAlbum.setAdapter(adapter);
        listAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // to do nothing
            }
        });
    }
}
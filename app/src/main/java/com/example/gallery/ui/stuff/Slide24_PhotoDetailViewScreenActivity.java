package com.example.gallery.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gallery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Slide24_PhotoDetailViewScreenActivity extends AppCompatActivity{
    String[] info = {"This is information picture 1","This is information picture 2","This is information picture 3","This is information picture 4","This is information picture 5","This is information picture 6","This is information picture 7"};
    Integer[] pic = {R.drawable.icon_photo_01,R.drawable.icon_photo_01,R.drawable.icon_photo_01,R.drawable.icon_photo_01,R.drawable.icon_photo_01,R.drawable.icon_photo_01,R.drawable.icon_photo_01};
    ListView listAlbum;
    FloatingActionButton btnAdd;

    @Override
    public void onCreate(Bundle saveStateInstance){
        super.onCreate(saveStateInstance);
        setContentView(R.layout.slide24_photos_detail_screen);
        listAlbum = (ListView) findViewById(R.id.list_Album);
        Slide24_ListAdapter adapter = new Slide24_ListAdapter(Slide24_PhotoDetailViewScreenActivity.this,R.layout.slide08_image_list,info,pic);
        listAlbum.setAdapter(adapter);
        listAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // to do nothing
            }
        });
        btnAdd = (FloatingActionButton) findViewById(R.id.floating_action_button);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOpenDialogClick();
            }
        });
    }
    private void buttonOpenDialogClick(){
        AddPhotoDialog listener = new AddPhotoDialog(this);
        listener.show();
    }



}
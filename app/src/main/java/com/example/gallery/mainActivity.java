package com.example.gallery;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.findlayout.R;
import com.google.android.material.badge.BadgeUtils;

//slice 24


public class mainActivity extends AppCompatActivity{
    String[] info = {"This is information picture 1","This is information picture 2","This is information picture 3","This is information picture 4","This is information picture 5","This is information picture 6","This is information picture 7"};
    Integer[] pic = {R.drawable.main_pic_1,R.drawable.main_pic_2,R.drawable.main_pic_3,R.drawable.main_pic_4,R.drawable.main_pic_5,R.drawable.main_pic_6,R.drawable.main_pic_7};
    ListView listAlbum;
    Button addPhoto;
    @Override
    public void onCreate(Bundle saveStateInstance){
        super.onCreate(saveStateInstance);
        setContentView(R.layout.detail_view_layout);
        listAlbum = (ListView) findViewById(R.id.list_Album);
        myAdapter adapter = new myAdapter(mainActivity.this,R.layout.item,info,pic);
        listAlbum.setAdapter(adapter);
        listAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //to do nothing
            }
        });
        addPhoto = (Button) findViewById(R.id.btnAddPicture);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOpenDialogClick();
            }
        });
    }
    private void buttonOpenDialogClick(){
        customDialog listener = new customDialog(this);
        listener.show();
    }
}

//slide 8
/*
public class mainActivity extends AppCompatActivity{
    String[] info = {"This is information picture 1","This is information picture 2","This is information picture 3","This is information picture 4","This is information picture 5","This is information picture 6","This is information picture 7"};
    Integer[] pic = {R.drawable.main_pic_1,R.drawable.main_pic_2,R.drawable.main_pic_3,R.drawable.main_pic_4,R.drawable.main_pic_5,R.drawable.main_pic_6,R.drawable.main_pic_7};
    ListView listAlbum;

    @Override
    public void onCreate(Bundle saveStateBundle){
        super.onCreate(saveStateBundle);
        setContentView(R.layout.find_layout);
        listAlbum = (ListView) findViewById(R.id.list_Album);
        myAdapter adapter = new myAdapter(mainActivity.this,R.layout.item,info,pic);
        listAlbum.setAdapter(adapter);
        listAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //to do nothing
            }
        });
    }
}
*/


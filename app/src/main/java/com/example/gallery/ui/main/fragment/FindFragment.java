package com.example.gallery.ui.main.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.ui.main.adapter.FindMediaItemAdapter;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class FindFragment extends Fragment implements View.OnClickListener {
    ViewGroup scrollViewGroup;
    View mView;
    TextInputEditText txtKey;
    RecyclerView rcvFindItem;

    Button btnReturn, btnSearch;
    TextView typeDes,typeDate,typeLocation,typeTag,typeExtension;
    List<MediaItem> listMediaItemFind,listMediaItemDescription, listmediaItemDate,listMediaItemLocation,listMediaItemTag,listMediaItemExtension;
    FindMediaItemAdapter mediaItemAdapter;
    String keyword="";
    public String FLAG = "Description";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_find, container, false);

        // Actionbar
        setHasOptionsMenu(true);
        Toolbar toolbar = mView.findViewById(R.id.toolbar_album);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Gallery - Find");

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        typeDate = view.findViewById(R.id.typeDate);
        typeDes =view.findViewById(R.id.typeDescription);
        typeLocation = view.findViewById(R.id.typeLocation);
        typeTag = view.findViewById(R.id.typeTag);
        typeExtension = view.findViewById(R.id.typeExtension);


        btnSearch = view.findViewById(R.id.btnSearch);
        txtKey = view.findViewById(R.id.txtFindContent);
        rcvFindItem = view.findViewById(R.id.rcv_find_item);
        btnSearch.setOnClickListener(this);

        typeDes.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false);
        rcvFindItem.setLayoutManager(layoutManager);
        mediaItemAdapter = new FindMediaItemAdapter();
        rcvFindItem.setAdapter(mediaItemAdapter);
        listMediaItemFind = new ArrayList<>();
        mediaItemAdapter.setData(listMediaItemFind);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = txtKey.getText().toString();
                if(keyword.isEmpty()){
                    Toast.makeText(view.getContext(),"Hãy nhập từ khóa để bắt đầu tìm kiếm",Toast.LENGTH_SHORT).show();
                    txtKey.setText("");
                }
                else{
                    DataAdapter(FLAG,keyword);
                }
            }
        });
        typeDate.setOnClickListener(this);
        typeDes.setOnClickListener(this);
        typeExtension.setOnClickListener(this);
        typeTag.setOnClickListener(this);
        typeLocation.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.typeDate){
            FLAG = "Date";
            typeDate.setTextColor(Color.BLACK);
            typeLocation.setTextColor(Color.GRAY);
            typeExtension.setTextColor(Color.GRAY);
            typeTag.setTextColor(Color.GRAY);
            typeDes.setTextColor(Color.GRAY);
            typeDate.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));
            typeLocation.setBackgroundColor(Color.WHITE);
            typeTag.setBackgroundColor(Color.WHITE);
            typeExtension.setBackgroundColor(Color.WHITE);
            typeDes.setBackgroundColor(Color.WHITE);
            DataAdapter(FLAG,keyword);

        }
        if(v.getId() == R.id.typeDescription){
            FLAG = "Description";
            typeDes.setTextColor(Color.BLACK);
            typeLocation.setTextColor(Color.GRAY);
            typeExtension.setTextColor(Color.GRAY);
            typeTag.setTextColor(Color.GRAY);
            typeDate.setTextColor(Color.GRAY);
            typeDes.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));
            typeLocation.setBackgroundColor(Color.WHITE);
            typeTag.setBackgroundColor(Color.WHITE);
            typeExtension.setBackgroundColor(Color.WHITE);
            typeDate.setBackgroundColor(Color.WHITE);
            DataAdapter(FLAG,keyword);


        }
        if(v.getId() == R.id.typeExtension){
            FLAG = "Extension";
            typeExtension.setTextColor(Color.BLACK);
            typeLocation.setTextColor(Color.GRAY);
            typeDate.setTextColor(Color.GRAY);
            typeTag.setTextColor(Color.GRAY);
            typeDes.setTextColor(Color.GRAY);
            typeExtension.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));
            typeLocation.setBackgroundColor(Color.WHITE);
            typeTag.setBackgroundColor(Color.WHITE);
            typeDate.setBackgroundColor(Color.WHITE);
            typeDes.setBackgroundColor(Color.WHITE);
            DataAdapter(FLAG,keyword);
        }
        if(v.getId() == R.id.typeTag){
            FLAG = "Tag";
            typeTag.setTextColor(Color.BLACK);
            typeLocation.setTextColor(Color.GRAY);
            typeDate.setTextColor(Color.GRAY);
            typeExtension.setTextColor(Color.GRAY);
            typeDes.setTextColor(Color.GRAY);
            typeTag.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));
            typeLocation.setBackgroundColor(Color.WHITE);
            typeDate.setBackgroundColor(Color.WHITE);
            typeExtension.setBackgroundColor(Color.WHITE);
            typeDes.setBackgroundColor(Color.WHITE);
            DataAdapter(FLAG,keyword);


        }
        if(v.getId() == R.id.typeLocation){
            FLAG = "Location";
            typeLocation.setTextColor(Color.BLACK);
            typeExtension.setTextColor(Color.GRAY);
            typeDate.setTextColor(Color.GRAY);
            typeTag.setTextColor(Color.GRAY);
            typeDes.setTextColor(Color.GRAY);
            typeLocation.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));
            typeDate.setBackgroundColor(Color.WHITE);
            typeTag.setBackgroundColor(Color.WHITE);
            typeExtension.setBackgroundColor(Color.WHITE);
            typeDes.setBackgroundColor(Color.WHITE);
            listMediaItemFind = new ArrayList<>();
            DataAdapter(FLAG,keyword);

        }
    }

    void DataAdapter(String FLAG, String keyword){
        listMediaItemFind = new ArrayList<>();
        if(keyword.isEmpty() || FLAG.isEmpty()){
            mediaItemAdapter.setData(listMediaItemFind);
            return;
        }
        MediaItemRepository.getInstance().getAllMediaItems().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                for(MediaItem i : mediaItems){
                    if(!i.getAlbumName().equals("Bin")){
                        if(FLAG.equals("Description")){
                            String desctiption = i.getDescription();
                            if(desctiption.contains(keyword))
                                listMediaItemFind.add(i);
                        }
                        if(FLAG.equals("Date")){
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
                            String date = simpleDateFormat.format(new Date(i.getCreationDate()));
                            if(date.contains(keyword))
                                listMediaItemFind.add(i);
                        }
                        if(FLAG.equals("Location")){
                            String location = i.getLocation();
                            if(location.contains(keyword))
                                listMediaItemFind.add(i);
                        }
                        if(FLAG.equals("Tag")){
                            String tag = i.getTag();
                            if(tag.contains(keyword))
                                listMediaItemFind.add(i);

                        }
                        if(FLAG.equals("Extension")){
                            String extension = i.getFileExtension();
                            if(extension.contains(keyword))
                                listMediaItemFind.add(i);
                        }
                    }
                }
                mediaItemAdapter.setData(listMediaItemFind);
                mediaItemAdapter.notifyDataSetChanged();
            }
        });
    }
}
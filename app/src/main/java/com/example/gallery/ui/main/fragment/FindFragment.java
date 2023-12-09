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

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class FindFragment extends Fragment implements View.OnClickListener {
    ViewGroup scrollViewGroup;
    View mView;
    TextInputEditText txtKey;
    RecyclerView rcvFindItem;

    Button btnReturn, btnSearch;
    TextView typeDes,typeDate,typeLocation,typeTag,typeNote;
    List<MediaItem> listMeidaItemFind;
    MediaItemAdapter mediaItemAdapter;

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
        typeNote = view.findViewById(R.id.typeNote);
        typeDate.setOnClickListener(this);
        typeNote.setOnClickListener(this);
        typeTag.setOnClickListener(this);
        typeDes.setOnClickListener(this);
        typeLocation.setOnClickListener(this);
        btnSearch = view.findViewById(R.id.btnSearch);
        txtKey = view.findViewById(R.id.txtFindContent);
        rcvFindItem = view.findViewById(R.id.rcv_find_item);
        btnSearch.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false);
        rcvFindItem.setLayoutManager(layoutManager);
        mediaItemAdapter = new MediaItemAdapter();
        rcvFindItem.setAdapter(mediaItemAdapter);
        MediaItemRepository.getInstance().getAllMediaItems().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                mediaItemAdapter.setData(mediaItems);
            }
        });






    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.typeDate){
            typeDate.setTextColor(Color.BLACK);
            typeLocation.setTextColor(Color.GRAY);
            typeNote.setTextColor(Color.GRAY);
            typeTag.setTextColor(Color.GRAY);
            typeDes.setTextColor(Color.GRAY);
            typeDate.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));
            typeLocation.setBackgroundColor(Color.WHITE);
            typeTag.setBackgroundColor(Color.WHITE);
            typeNote.setBackgroundColor(Color.WHITE);
            typeDes.setBackgroundColor(Color.WHITE);


        }
        if(v.getId() == R.id.typeDescription){
            typeDes.setTextColor(Color.BLACK);
            typeLocation.setTextColor(Color.GRAY);
            typeNote.setTextColor(Color.GRAY);
            typeTag.setTextColor(Color.GRAY);
            typeDate.setTextColor(Color.GRAY);
            typeDes.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));
            typeLocation.setBackgroundColor(Color.WHITE);
            typeTag.setBackgroundColor(Color.WHITE);
            typeNote.setBackgroundColor(Color.WHITE);
            typeDate.setBackgroundColor(Color.WHITE);


        }
        if(v.getId() == R.id.typeNote){
            typeNote.setTextColor(Color.BLACK);
            typeLocation.setTextColor(Color.GRAY);
            typeDate.setTextColor(Color.GRAY);
            typeTag.setTextColor(Color.GRAY);
            typeDes.setTextColor(Color.GRAY);
            typeNote.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));
            typeLocation.setBackgroundColor(Color.WHITE);
            typeTag.setBackgroundColor(Color.WHITE);
            typeDate.setBackgroundColor(Color.WHITE);
            typeDes.setBackgroundColor(Color.WHITE);


        }
        if(v.getId() == R.id.typeTag){
            typeTag.setTextColor(Color.BLACK);
            typeLocation.setTextColor(Color.GRAY);
            typeDate.setTextColor(Color.GRAY);
            typeNote.setTextColor(Color.GRAY);
            typeDes.setTextColor(Color.GRAY);
            typeTag.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));
            typeLocation.setBackgroundColor(Color.WHITE);
            typeDate.setBackgroundColor(Color.WHITE);
            typeNote.setBackgroundColor(Color.WHITE);
            typeDes.setBackgroundColor(Color.WHITE);

        }
        if(v.getId() == R.id.typeLocation){
            typeLocation.setTextColor(Color.BLACK);
            typeNote.setTextColor(Color.GRAY);
            typeDate.setTextColor(Color.GRAY);
            typeTag.setTextColor(Color.GRAY);
            typeDes.setTextColor(Color.GRAY);
            typeLocation.setBackground(getResources().getDrawable(R.drawable.find_type_choosen));
            typeDate.setBackgroundColor(Color.WHITE);
            typeTag.setBackgroundColor(Color.WHITE);
            typeNote.setBackgroundColor(Color.WHITE);
            typeDes.setBackgroundColor(Color.WHITE);

        }


    }
}
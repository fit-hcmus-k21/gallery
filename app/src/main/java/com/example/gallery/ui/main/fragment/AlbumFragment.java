package com.example.gallery.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.gallery.R;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.ui.main.adapter.AlbumAdapter;
import com.example.gallery.ui.main.doing.DeleteActivity;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment {


    View mView;
    private RecyclerView recyclerView;
    private  AlbumAdapter albumAdapter;
    private Menu mMenu;
    LinearLayout deleteAlbum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_album, container, false);

        // Actionbar
        setHasOptionsMenu(true);
        Toolbar toolbar = mView.findViewById(R.id.toolbar_album);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Gallery - Album");

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // Ánh xạ các biến
        recyclerView = view.findViewById(R.id.rcv_album_item);
        deleteAlbum = view.findViewById(R.id.albumDelete);

        // Xử lý RecyclerView GridManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);




        // Xử lý Adapter
        albumAdapter = new AlbumAdapter();
        recyclerView.setAdapter(albumAdapter);


        // Xử lý dữ liệu
        AlbumRepository.getInstance().getAlbums().observe(getViewLifecycleOwner(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                List<Album> data = new ArrayList<>();
                for(Album iterator : albums)
                    if(!iterator.getName().equals("Bin"))
                        data.add(iterator);
                albumAdapter.setData(data);
            }
        });

        //xử lý sự kiện chọn album delete
        deleteAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeleteActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_appbar_album_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            getActivity().onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
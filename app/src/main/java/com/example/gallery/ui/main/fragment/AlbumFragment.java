package com.example.gallery.ui.main.fragment;

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
import com.example.gallery.data.local.entities.Album;
import com.example.gallery.data.repositories.models.ViewModel.AlbumViewModel;
import com.example.gallery.ui.main.adapter.AlbumAdapter;

import java.util.List;

public class AlbumFragment extends Fragment {


    View mView;
    AlbumViewModel albumViewModel;
    private RecyclerView recyclerView;
    private  AlbumAdapter albumAdapter;
    private Menu mMenu;

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

        // Khởi tạo ViewModel
        albumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);



        // Ánh xạ các biến
        recyclerView = view.findViewById(R.id.rcv_album_item);

        // Xử lý RecyclerView GridManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);




        // Xử lý Adapter
        albumAdapter = new AlbumAdapter();
        recyclerView.setAdapter(albumAdapter);

//        // Thêm Decorations cho RecyclerView là những đường kẻ ngang và dọc
//        RecyclerView.ItemDecoration itemDecorationHorizontal = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
//        RecyclerView.ItemDecoration itemDecorationVertical = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(itemDecorationHorizontal);
//        recyclerView.addItemDecoration(itemDecorationVertical);

        // Xử lý dữ liệu
        albumViewModel.getAllAlbums().observe(getViewLifecycleOwner(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                albumAdapter.setData(albums);
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
package com.example.gallery.ui.main.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.ui.main.adapter.AlbumAdapter;
import com.example.gallery.ui.main.doing.DeleteActivity;
import com.example.gallery.ui.main.doing.InnerAlbumScreen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
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
        Toolbar toolbar = mView.findViewById(R.id.toolbar_fragment_album);
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

        albumAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Album album, int position) {
                MediaItemRepository.getInstance().getAllMediaItemsByAlbumName(album.getName()).observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
                    @Override
                    public void onChanged(List<MediaItem> mediaItems) {
                        for(MediaItem mediaItem : mediaItems){
                            mediaItem.setPreviousAlbum(mediaItem.getAlbumName());
                            MediaItemRepository.getInstance().updateMediaPreviousAlbum(mediaItem.getId(), mediaItem.getAlbumName());

                            mediaItem.setAlbumName("Bin");
                            MediaItemRepository.getInstance().updateMediaItemAlbum(mediaItem.getId(),"Bin");

                            Calendar calendar = Calendar.getInstance();
                            long currentDate = calendar.getTimeInMillis();
                            MediaItemRepository.getInstance().updateMediaItemDeleteTs(mediaItem.getId(),currentDate);
                            mediaItem.setDeletedTs(currentDate);
                            albumAdapter.notifyItemChanged(position);

                        }
                    }
                });
                AlbumRepository.getInstance().deleteAlbum(AppPreferencesHelper.getInstance().getCurrentUserId(), album.getName());

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

        if(id == R.id.media_create_album_item){
            dialogCreateAlbum();
        }
        else if(id == R.id.favorite_album_item){
            Intent intent = new Intent(getContext(), InnerAlbumScreen.class);
            Bundle bundle = new Bundle();
            bundle.putString("albumName", "Favorite");
            intent.putExtras(bundle);
            getContext().startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void dialogCreateAlbum() {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_new_album);

        Window window = dialog.getWindow();

        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setAttributes(window.getAttributes());

        dialog.setCancelable(false);

        // Ánh xạ các view
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_create_album);
        Button btnOke = dialog.findViewById(R.id.btn_ok_create_album);
        EditText editText = dialog.findViewById(R.id.edt_create_album);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String createAlbumName = editText.getText().toString();
                if(createAlbumName.isEmpty()){
                    Toast.makeText(AlbumFragment.this.getContext(), "Tên album không được để trống", Toast.LENGTH_SHORT).show();
                }
                else{
                    Album album = new Album(createAlbumName, "",
                            System.currentTimeMillis(), "",
                            AppPreferencesHelper.getInstance().getCurrentUserId(),
                            "",
                            0);
                    AlbumRepository.getInstance().insert(album);
                    dialog.dismiss();

                }
            }
        });

        dialog.show();
    }


}
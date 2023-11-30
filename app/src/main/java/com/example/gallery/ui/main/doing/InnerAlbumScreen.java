package com.example.gallery.ui.main.doing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.data.local.entities.MediaItem;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class InnerAlbumScreen extends AppCompatActivity {
    MediaItemViewModel mediaItemViewModel;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_album_layout);

        // Khởi tạo viewModel
        mediaItemViewModel = ViewModelProviders.of(this).get(MediaItemViewModel.class);

        // Ánh xạ các view
        recyclerView = findViewById(R.id.inner_album_recycler_view);

        // Xử lý LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Xử lý Adapter
        MediaItemAdapter mediaItemAdapter = new MediaItemAdapter();
        recyclerView.setAdapter(mediaItemAdapter);

        // Lấy dữ liệu từ intent
        Bundle bundle = getIntent().getExtras();
        String albumPath = bundle.getString("albumPath");

        mediaItemViewModel.getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {

                List<MediaItem> mediaItemList = getMediaItemsOfAlbum(mediaItems, albumPath);
//                Log.e("Mytag", "onChanged: " + mediaItemList.size());
                mediaItemAdapter.setData(mediaItemList);
            }
        });

        mediaItemAdapter.setOnItemClickListener(new MediaItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MediaItem mediaItem) {
                Intent intent = new Intent(InnerAlbumScreen.this, SingleMediaActivity.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable("mediaItem", mediaItem);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });


    }

    private List<MediaItem> getMediaItemsOfAlbum(List<MediaItem> mediaItems, String albumPath) {
        List<MediaItem> result = new ArrayList<>();

        if(albumPath.equals("favoritePath")){ // Không nên dùng == để so sánh chuỗi hãy dùng equals
            for(MediaItem mediaItem : mediaItems){
                if(mediaItem.isFavorite()){
                    result.add(mediaItem);
                    Log.e("Mytag", "getMediaItemsOfAlbum: " + mediaItem.getPath());
                }
            }
        }
        else{
            for(MediaItem mediaItem : mediaItems){
                if(mediaItem.getParentPath().equals(albumPath)){
                    result.add(mediaItem);
                }
            }
        }



        return result;
    }
}

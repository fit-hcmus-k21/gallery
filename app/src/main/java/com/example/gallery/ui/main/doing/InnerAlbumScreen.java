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
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class InnerAlbumScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("InnerAlbumScreen  27: onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_album_layout);

        // Khởi tạo viewModel

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
        String albumName = bundle.getString("albumName");

        MediaItemRepository.getInstance().getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {

                List<MediaItem> mediaItemList = getMediaItemsOfAlbum(mediaItems, albumName);
//                Log.e("Mytag", "onChanged: " + mediaItemList.size());
                System.out.println("InnerAlbumScreen  53: onChanged before set data: mediaItemList = " + mediaItems);
                mediaItemAdapter.setData(mediaItemList);
                System.out.println("InnerAlbumScreen  55: onChanged: after set data  " );
            }
        });

        mediaItemAdapter.setOnItemClickListener(new MediaItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MediaItem mediaItem) {
                System.out.println("On Item Click | Inner Album Screen before");

                Intent intent = new Intent(InnerAlbumScreen.this, SingleMediaActivity.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable("mediaItem", mediaItem);
                intent.putExtras(bundle);

                startActivity(intent);
                System.out.println("On Item Click | Inner Album Screen after");
            }
        });


    }

    private List<MediaItem> getMediaItemsOfAlbum(List<MediaItem> mediaItems, String albumName) {
        List<MediaItem> result = new ArrayList<>();

//        if(albumName.equals("favoritePath")){ // Không nên dùng == để so sánh chuỗi hãy dùng equals
//            for(MediaItem mediaItem : mediaItems){
//                if(mediaItem.isFavorite()){
//                    result.add(mediaItem);
//                    Log.e("Mytag", "getMediaItemsOfAlbum: " + mediaItem.getName());
//                }
//            }
//        }
        {
            for(MediaItem mediaItem : mediaItems){
                if(mediaItem.getAlbumName().equals(albumName)){
                    result.add(mediaItem);
                }
            }
        }



        return result;
    }
}

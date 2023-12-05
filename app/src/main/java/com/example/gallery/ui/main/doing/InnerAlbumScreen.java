package com.example.gallery.ui.main.doing;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;
import com.example.gallery.utils.BytesToStringConverter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class InnerAlbumScreen extends AppCompatActivity {
    MediaItemViewModel mediaItemViewModel;
    RecyclerView recyclerView;
    MediaItemAdapter mediaItemAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_album_layout);

        // Khởi tạo viewModel
        mediaItemViewModel = ViewModelProviders.of(this).get(MediaItemViewModel.class);

        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);

        // Ánh xạ các view
        recyclerView = findViewById(R.id.inner_album_recycler_view);

        // Xử lý LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Xử lý Adapter
        mediaItemAdapter = new MediaItemAdapter();
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
        materialToolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.statistic){
                showStatisticDialog();
            }
            return true;
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
    private void showStatisticDialog(){
        List<MediaItem> list = mediaItemAdapter.getData();
        long folderSize = 0;
        int imageCnt = 0;
        int videoCnt = 0;
        for(int i = 0 ; i < list.size();i++){
            folderSize += list.get(i).getFileSize();
            if(list.get(i).getFileExtension().equals("video/mp4"))
                videoCnt++;
            else imageCnt++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thống kê")
                .setMessage("Số ảnh và video: " + list.size() +"\n"
                        + "Số ảnh: " + imageCnt + "\n"
                        + "Số video: " + videoCnt + "\n"
                        + "Kích thước: " + BytesToStringConverter.longToString(folderSize))
                .setPositiveButton("OK", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

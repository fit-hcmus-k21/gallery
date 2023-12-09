package com.example.gallery.ui.main.doing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Delete;

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.main.adapter.DeleteAdapter;
import com.example.gallery.ui.main.adapter.ViewPagerSingleMediaAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeleteActivity extends AppCompatActivity {
    RecyclerView recyclerDelete;
    ViewPagerSingleMediaAdapter viewPagerSingleMediaAdapter;
    Button btnReturn;
    MediaItemViewModel mediaItemViewModel;
    List<MediaItem> listMediaItemToDisplay = new ArrayList<>();
    DeleteAdapter deleteAdapter;
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.delete_layout);
        recyclerDelete = findViewById(R.id.rcv_delete_item);
        btnReturn = findViewById(R.id.btnReturn);
        deleteAdapter = new DeleteAdapter();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        recyclerDelete.setLayoutManager(layoutManager);
        recyclerDelete.setAdapter(deleteAdapter);

        //lấy danh sách data trong thư mục bin
        MediaItemRepository.getInstance().getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                listMediaItemToDisplay = new ArrayList<>();
                for(MediaItem iterator : mediaItems)
                    if(iterator.getAlbumName() != null &&  iterator.getAlbumName().equals("Bin")){
                        // xóa những ảnh với DeleteTs trên 30 ngày
                        //lấy ngày hiện tại
                        Calendar calendar = Calendar.getInstance();
                        long currentDate = calendar.getTimeInMillis();
                        long deleteDate = iterator.getDeletedTs();
                        //chuyển về đinh dạng ngày
                        Date day1 = new Date(currentDate);
                        Date day2 = new Date(deleteDate);
                        // tính khoảng cách 2 ngày
                        long distance = day1.getTime() - day2.getTime();
                        long remain = TimeUnit.MILLISECONDS.toDays(distance);
                        if(remain >= 30){
                            MediaItemRepository.getInstance().deleteMediaItemFromPath(iterator.getPath());
                        }
                        else{
                            listMediaItemToDisplay.add(iterator);
                        }

                    }
                deleteAdapter.setData(listMediaItemToDisplay);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package com.example.gallery.ui.main.doing;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.ui.main.adapter.ViewPagerSingleMediaAdapter;
import com.example.gallery.utils.GetInDexOfHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SingleMediaDeleteActivity extends AppCompatActivity {
    MutableLiveData<MediaItem> mediaItemLiveData = new MutableLiveData<>();
    ViewPagerSingleMediaAdapter viewPagerSingleMediaAdapter;
    ViewPager2 deletePic;
    List<MediaItem> listMediaItem = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    Button btnReturn;
    int currentIndex = -1;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.delete_single_media_item);

        deletePic = findViewById(R.id.single_photo_delete);
        btnReturn = findViewById(R.id.btnReturn);
        bottomNavigationView = findViewById(R.id.deleteBottomNavigation);

        //get data
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        MediaItem mediaItemIntent = (MediaItem) data.getSerializable("mediaItem");

        viewPagerSingleMediaAdapter = new ViewPagerSingleMediaAdapter();
        deletePic.setAdapter(viewPagerSingleMediaAdapter);

        final boolean[] isMoveToCurrentItem = {false};
        MediaItemRepository.getInstance().getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                // lấy danh sách các item trong bin
                listMediaItem = new ArrayList<>();
                for(MediaItem iterator : mediaItems)
                    if(iterator.getAlbumName().equals("Bin")) {
                        listMediaItem.add(iterator);
                    }

                viewPagerSingleMediaAdapter.setData(listMediaItem);
                int index = GetInDexOfHelper.getIndexOf(listMediaItem,mediaItemIntent);

                if(!isMoveToCurrentItem[0] && index >=0  && index < listMediaItem.size()){
                    // Lấy dữ liệu của MediaItem đang được chọn tra về LiveData
                    mediaItemLiveData.setValue(listMediaItem.get(index));
                    if(!isMoveToCurrentItem[0]){
                        deletePic.setCurrentItem(index,false);
                        isMoveToCurrentItem[0] = true;
                    }
                }
            }
        });

        deletePic.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                if(listMediaItem.size() <= position)
                    return;
                mediaItemLiveData.setValue(listMediaItem.get(position));
                currentIndex = position;
                if(listMediaItem == null)
                    return;

                MediaItem selectedMediaItem = listMediaItem.get(position);
                // xử lý các thao tác bên trong
                bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        if(item.getItemId() == R.id.btnRestore){                // failed 
                            MediaItemRepository.getInstance().updateMediaItemAlbum(selectedMediaItem.getId(),selectedMediaItem.getPreviousAlbum());
                            MediaItemRepository.getInstance().updateMediaPreviousAlbum(selectedMediaItem.getId(),"");
                            MediaItemRepository.getInstance().updateMediaItemDeleteTs(selectedMediaItem.getId(),0);
                            listMediaItem.remove(position);
                            viewPagerSingleMediaAdapter.setData(listMediaItem);
                            viewPagerSingleMediaAdapter.notifyDataSetChanged();
                            finish();

                        }
                        if(item.getItemId() == R.id.btnDelete){
                            MediaItem itemDelete = listMediaItem.get(position);
                            listMediaItem.remove(position);
                            MediaItemRepository.getInstance().deleteMediaItemFromPath(itemDelete.getPath());
                            viewPagerSingleMediaAdapter.notifyDataSetChanged();

                            finish();

                        }
                    }
                });

            }
            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("SingleMediaActivity | OnCreate | onPageScrollStateChanged");
                super.onPageScrollStateChanged(state);
            }

        });


    }

}

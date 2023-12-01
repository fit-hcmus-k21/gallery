package com.example.gallery.ui.main.doing;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.main.adapter.ViewPagerSingleMediaAdapter;
import com.example.gallery.utils.GetInDexOfHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.Inflater;

public class SingleMediaActivity extends AppCompatActivity {

    public static int REQUEST_MAKE_FAVORITE_ITEM = 100;
    MediaItemViewModel mediaItemViewModel;

    ViewPager2 viewPager2;
    ViewPagerSingleMediaAdapter viewPagerSingleMediaAdapter;

    ImageView favoriteImageView;
    List<MediaItem> mediaItemsList; // Biến thành viên để lưu trữ danh sách các MediaItem

    LinearLayout customappbar;
    Menu mMenu;
    MutableLiveData<MediaItem> mediaItemLiveData = new MutableLiveData<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_photo_screen);

        // Create instance of ViewModel
        mediaItemViewModel = ViewModelProviders.of(this).get(MediaItemViewModel.class);

        // Ánh xạ các view
        viewPager2 = findViewById(R.id.single_photo_viewpager2);
        favoriteImageView = findViewById(R.id.media_favorite_icon);
        customappbar = findViewById(R.id.custom_bottom_appbar);


        // Gán Adapter
        viewPagerSingleMediaAdapter = new ViewPagerSingleMediaAdapter();
        viewPager2.setAdapter(viewPagerSingleMediaAdapter);


        // Thao tác voới actionbar và tạo nut back ở đây
        Toolbar toolbar = findViewById(R.id.toolbar_single_media_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        MediaItem mediaItemIntent = (MediaItem) bundle.getSerializable("mediaItem");

        // Đây là biến dạng boolen để đảm bảo rằng việc di chuyển đến item đang được chọn chỉ được thực hiện 1 lần
        // Nếu không có biến này thì khi chúng ta thực hiện thao tác thay đổi trạng thái Favorite của item đang được chọn
        // thì nó sẽ tự động di chuyển đến item mà ta đã truyền vào trước đó gây mất đồng bộ
        final boolean[] isMoveToCurrentItem = {false};
        mediaItemViewModel.getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {

                mediaItemsList = mediaItems;

                viewPagerSingleMediaAdapter.setData(mediaItems);
                int index = GetInDexOfHelper.getIndexOf(mediaItems, mediaItemIntent);

                // Lấy dữ liệu của MediaItem đang được chọn tra về LiveData
                mediaItemLiveData.setValue(mediaItems.get(index));

                if(!isMoveToCurrentItem[0]){
                    Log.e("MyTag", "onChanged: " + index)   ;
                    viewPager2.setCurrentItem(index, false);
                    isMoveToCurrentItem[0] = true;
                }

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

                MediaItem selectedMediaItem = mediaItemsList.get(position);
                Log.e("MyTag", "onPageSelected: " + position);

                if(selectedMediaItem.isFavorite()){
                    favoriteImageView.setImageResource(R.drawable.heart_svgrepo_com_color);
                }
                else{
                    favoriteImageView.setImageResource(R.drawable.baseline_heart_svgrepo_com);
                }


                favoriteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Đây chỉ mới là việc thay đổi dữ liêu của Favorite trong database tự định nghĩa
                        // Chúng ta phải tiêến hành cập nhật trong MediaStore.Images.Media/MediaStores.Video.Media trường Favorite nữa
                        selectedMediaItem.setFavorite(!selectedMediaItem.isFavorite());
                        mediaItemViewModel.updateFavorite(selectedMediaItem.getId(), selectedMediaItem.isFavorite());

                        if(selectedMediaItem.isFavorite()){
                            favoriteImageView.setImageResource(R.drawable.heart_svgrepo_com_color);
                        }
                        else{
                            favoriteImageView.setImageResource(R.drawable.baseline_heart_svgrepo_com);
                        }

                        // Cập nhật thông tin trong MediaStore.Images.Media/MediaStores.Video.Media bằng cách sử dụng Pending Intent với createFavoriteRequest
                        List<Uri> uriList = new ArrayList<>();
                        uriList.add(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(selectedMediaItem.getId())));

                        PendingIntent pendingIntent = null;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            pendingIntent = MediaStore.createFavoriteRequest(getContentResolver(), uriList, selectedMediaItem.isFavorite());

                            if(pendingIntent != null){
                                try {
                                    startIntentSenderForResult(pendingIntent.getIntentSender(), REQUEST_MAKE_FAVORITE_ITEM, null, 0, 0, 0, null);
                                } catch (IntentSender.SendIntentException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }

                    }
                });



                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    // Override lại hàm khởi tạo 1 menu, và gán menu đó cho biến mMenu. Có thể thao tác trong file xml cho menu
    // Nhưng không hiểu vì sao lại lỗi, không hiêển thị được icon nên dùng cách này
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_appbar_media_item_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    // Bắt các sự kiện khi các nút trên thanh Action bar được chọn


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home ){
            onBackPressed();
        }
        else if(id == R.id.media_information_item){
            mediaItemLiveData.observe(this, new Observer<MediaItem>() {
                @Override
                public void onChanged(MediaItem mediaItem) {
                    DisPlayInforMationAlerDialog(mediaItem);
                }
            });
        }
        else if(id == R.id.media_change_name_item){
            //TODO handle media_change_name_item
        }
        else if(id == R.id.media_copy_to_item){
            //TODO handle media_copy_to_item
        }
        else if(id == R.id.media_move_to_item){
            //TODO handle media_move_to_item
        }
        else if(id == R.id.media_set_wallpaper_item){
            //TODO handle media_set_wallpaper_item
        }


        return super.onOptionsItemSelected(item);
    }

    private void DisPlayInforMationAlerDialog(MediaItem mediaItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chi tiết ảnh")
                .setMessage("* Tên: " + mediaItem.getName() + "\n" +
                        "* Đường dẫn: " + mediaItem.getPath() + "\n" +
                        "* Kích thước: " + mediaItem.getFileSize() + "\n" +
                        "* Ngày tạo: " + mediaItem.getCreationDate() + "\n" +
                        "* Ngày sửa: " + mediaItem.getLastModified() + "\n" +
                        "* Định dạng: " + mediaItem.getFileExtension() + "\n" +
                        "* Độ phân giải: " + mediaItem.getWidth() + "x" + mediaItem.getHeight() + "\n" +
                        "* Độ yêu thích: " + mediaItem.isFavorite() + "\n")
                .setPositiveButton("OK", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
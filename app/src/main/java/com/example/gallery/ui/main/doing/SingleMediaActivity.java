package com.example.gallery.ui.main.doing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.data.repositories.models.ViewModel.QRCodeViewModel;
import com.example.gallery.ui.main.Slide35_QRCodeActivity;
import com.example.gallery.ui.main.adapter.ViewPagerSingleMediaAdapter;
import com.example.gallery.utils.ExoPlayerManagement;
import com.example.gallery.utils.GetInDexOfHelper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
    List<ExoPlayer> exoPlayerList = new ArrayList<>();
    int currentIndex = -1;

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

        // Call back
        viewPagerSingleMediaAdapter.setOnVideoPreparedListener(new ViewPagerSingleMediaAdapter.OnVideoPreparedListener() {
            @Override
            public void onVideoPrepared(ExoPlayer exoPlayer) {
                exoPlayerList.add(exoPlayer);
                Log.e("MyTag", "count: " + exoPlayerList.size());
            }
        });

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

                if(!isMoveToCurrentItem[0] && index >= 0 && index < mediaItems.size()){
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

                currentIndex = position;

                if(mediaItemsList == null){
                    return;
                }
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
                        if(selectedMediaItem.isFavorite()){
                            favoriteImageView.setImageResource(R.drawable.heart_svgrepo_com_color);
                        }
                        else{
                            favoriteImageView.setImageResource(R.drawable.baseline_heart_svgrepo_com);
                        }
                        mediaItemViewModel.updateFavorite(selectedMediaItem.getId(), selectedMediaItem.isFavorite());



//                        // Cập nhật thông tin trong MediaStore.Images.Media/MediaStores.Video.Media bằng cách sử dụng Pending Intent với createFavoriteRequest
//                        List<Uri> uriList = new ArrayList<>();
//                        uriList.add(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(selectedMediaItem.getId())));
//
//                        PendingIntent pendingIntent = null;
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                            pendingIntent = MediaStore.createFavoriteRequest(getContentResolver(), uriList, selectedMediaItem.isFavorite());
//
//                            if(pendingIntent != null){
//                                try {
//                                    startIntentSenderForResult(pendingIntent.getIntentSender(), REQUEST_MAKE_FAVORITE_ITEM, null, 0, 0, 0, null);
//                                } catch (IntentSender.SendIntentException e) {
//                                    throw new RuntimeException(e);
//                                }
//
//                            }
//                        }

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
            DisPlayInforMationAlerDialog(mediaItemsList.get(currentIndex));
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
        else if(id == R.id.media_set_lockpaper_item){
            setLockpaper();
        }
        else if(id == R.id.media_set_wallpaper_item){
            setWallpaper();
        }
        else if(id == R.id.media_qr_code_item){
            showQRcodeDialog(mediaItemsList.get(currentIndex).getPath());
        }



        return super.onOptionsItemSelected(item);
    }
    private void setLockpaper() {
        MediaItem mediaItemLiveData = mediaItemsList.get(currentIndex);
        Toast.makeText(this, "Set wallpaper" + mediaItemLiveData.getPath() , Toast.LENGTH_SHORT).show();

        if (mediaItemLiveData != null) {
            Bitmap mBitmap = BitmapFactory.decodeFile(mediaItemLiveData.getPath());

            if (mBitmap != null) {
                try {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager.setBitmap(mBitmap, null, true, WallpaperManager.FLAG_LOCK);
                    } else {
                        // For older devices without direct lockscreen wallpaper support
                        Toast.makeText(this, "Lockscreen wallpaper is not supported on this device.", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(this, "Lockscreen wallpaper set successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to set lockscreen wallpaper", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Bitmap is null. Unable to set lockscreen wallpaper.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void setWallpaper() {
        MediaItem mediaItemLiveData = mediaItemsList.get(currentIndex);
        Toast.makeText(this, "Set wallpaper" + mediaItemLiveData.getPath() , Toast.LENGTH_SHORT).show();

        if (mediaItemLiveData != null) {
            Bitmap mBitmap = BitmapFactory.decodeFile(mediaItemLiveData.getPath());

            if (mBitmap != null) {
                try {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                    wallpaperManager.setBitmap(mBitmap);

                    // Optionally, you can set the wallpaper to other options like CENTER_CROP, FIT_XY, etc.
                    // wallpaperManager.setWallpaperOffsetSteps(1, 1);
                    // wallpaperManager.suggestDesiredDimensions(width, height);

                    Toast.makeText(this, "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to set wallpaper", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Bitmap is null. Unable to set wallpaper.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showQRcodeDialog(String file_path) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_qr_code);


        Window window = dialog.getWindow();

        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setAttributes(window.getAttributes());

        // Vị trí show dialog


        dialog.setCancelable(true);


//         Ánh xạ các view
        ImageView imageView_qr_code = dialog.findViewById(R.id.qr_code_image_view);
        Button button_close = dialog.findViewById(R.id.save_qr_code_button);

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // ViewModel
        QRCodeViewModel qrCodeViewModel;
        qrCodeViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(QRCodeViewModel.class);
        qrCodeViewModel.generateQRCode(file_path);

        qrCodeViewModel.qrCodeLiveData.observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                Toast.makeText(SingleMediaActivity.this, "QR code generated", Toast.LENGTH_SHORT).show();
                imageView_qr_code.setImageBitmap(bitmap);
            }
        });

        dialog.show();

    }

    private void DisPlayInforMationAlerDialog(MediaItem mediaItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Convert creationDate và lastModified về dạng ngày tháng năm
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String creationDate = "";
        if(mediaItem.getCreationDate() != null){
            creationDate = simpleDateFormat.format(mediaItem.getCreationDate());

        }
        String lastModified = "";

        if(mediaItem.getLastModified() != null){
            lastModified = simpleDateFormat.format(mediaItem.getLastModified());
        }

        builder.setTitle("Chi tiết ảnh")
                .setMessage("* Tên: " + mediaItem.getName() + "\n" +
                        "* Đường dẫn: " + mediaItem.getPath() + "\n" +
                        "* Kích thước: " + formatFileSize(mediaItem.getFileSize()) + " MB" + "\n" +
                        "* Ngày tạo: " + creationDate + "\n" +
                        "* Ngày sửa: " + lastModified + "\n" +
                        "* Định dạng: " + mediaItem.getFileExtension() + "\n" +
                        "* Độ phân giải: " + mediaItem.getWidth() + "x" + mediaItem.getHeight() + "\n" +
                        "* Yêu thích: " + (mediaItem.isFavorite() ? "Có" : "Không" )+ "\n")
                .setPositiveButton("OK", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public String formatFileSize(Long fileSize){
        double size = (1.0*fileSize)/(1024*1024);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(size);
    }
    @Override
    protected void onStop() {
        super.onStop();
        for(ExoPlayer exoPlayer : exoPlayerList){
            if(exoPlayer != null){
                exoPlayer.clearMediaItems();
                exoPlayer.release();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(ExoPlayer exoPlayer : exoPlayerList){
            if(exoPlayer != null){
                exoPlayer.clearMediaItems();
                exoPlayer.release();

            }
        }
    }
}

package com.example.gallery.ui.main.doing;

import android.app.AlertDialog;
import android.app.Dialog;

import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gallery.App;
import com.example.gallery.R;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.data.repositories.models.ViewModel.QRCodeViewModel;
import com.example.gallery.ui.main.adapter.ViewPagerSingleMediaAdapter;
import com.example.gallery.ui.main.fragment.FindFragment;
import com.example.gallery.utils.GetInDexOfHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class SingleMediaActivity extends AppCompatActivity  {

    public static int REQUEST_MAKE_FAVORITE_ITEM = 100;

    ViewPager2 viewPager2;
    ViewPagerSingleMediaAdapter viewPagerSingleMediaAdapter;

    ImageView favoriteImageView, editImageView;
    ImageView shareImageView, deleteImageView;
    List<MediaItem> mediaItemsList; // Biến thành viên để lưu trữ danh sách các MediaItem
    MutableLiveData<MediaItem> mediaItemLiveData = new MutableLiveData<>();
    LinearLayout customappbar;
    Menu mMenu;
    List<ExoPlayer> exoPlayerList = new ArrayList<>();
    int currentIndex = -1;
    String key ="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //  System.out.println("SingleMediaActivity | OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_photo_screen);


        // Ánh xạ các view
        viewPager2 = findViewById(R.id.single_photo_viewpager2);
        favoriteImageView = findViewById(R.id.media_favorite_icon);
        shareImageView = findViewById(R.id.media_share_icon);
        customappbar = findViewById(R.id.custom_bottom_appbar);
        editImageView = findViewById(R.id.media_edit_icon);
        deleteImageView = findViewById(R.id.media_trash_icon);




        // Gán Adapter
        viewPagerSingleMediaAdapter = new ViewPagerSingleMediaAdapter();
        viewPager2.setAdapter(viewPagerSingleMediaAdapter);

        // Call back

        // Thao tác với actionbar và tạo nut back ở đây
        Toolbar toolbar = findViewById(R.id.toolbar_single_media_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String albumName = bundle.getString("albumName");
        MediaItem mediaItemIntent = (MediaItem) bundle.getSerializable("mediaItem");
        key = bundle.getString("key");



        // Đây là biến dạng boolen để đảm bảo rằng việc di chuyển đến item đang được chọn chỉ được thực hiện 1 lần
        // Nếu không có biến này thì khi chúng ta thực hiện thao tác thay đổi trạng thái Favorite của item đang được chọn
        // thì nó sẽ tự động di chuyển đến item mà ta đã truyền vào trước đó gây mất đồng bộ

        final boolean[] isMoveToCurrentItem = {false};

        if(albumName.equals("AllMedia")){
            MediaItemRepository.getInstance().getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
                @Override
                public void onChanged(List<MediaItem> mediaItems) {
                    //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items");

                    List<MediaItem> tempList = new ArrayList<>(mediaItems);

                    for(int i = 0; i < tempList.size() - 1; i++){
                        for(int j = i + 1; j < tempList.size(); j++){
                            if(tempList.get(i).getId() < tempList.get(j).getId()){
                                MediaItem temp = tempList.get(i);
                                tempList.set(i, tempList.get(j));
                                tempList.set(j, temp);
                            }
                        }
                    }

                    mediaItemsList = tempList;

                    viewPagerSingleMediaAdapter.setData(tempList);
                    int index = GetInDexOfHelper.getIndexOf(tempList, mediaItemIntent);

                    if (!isMoveToCurrentItem[0] && index >= 0 && index < tempList.size()) {
                        // Lấy dữ liệu của MediaItem đang được chọn tra về LiveData
                        mediaItemLiveData.setValue(tempList.get(index));
                        //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 101");
                        //  System.out.println("SingleMediaActivity | mediaItemLiveData = " + mediaItemLiveData.getValue());

                        if (!isMoveToCurrentItem[0]) {
                            Log.e("MyTag", "onChanged: " + index);
                            //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 105");

                            viewPager2.setCurrentItem(index, false);
                            //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 108");
                            isMoveToCurrentItem[0] = true;

                        }

                    }
                }
            });
        }
        else if(albumName.equals("Favorite")){
            MediaItemRepository.getInstance().getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
                @Override
                public void onChanged(List<MediaItem> mediaItems) {
                    //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items");


                    List<MediaItem> mediaItemsFavorite = getFavoriteMediaItems(mediaItems);


                    mediaItemsList = mediaItemsFavorite;

                    viewPagerSingleMediaAdapter.setData(mediaItemsFavorite);
                    int index = GetInDexOfHelper.getIndexOf(mediaItemsFavorite, mediaItemIntent);

                    if (!isMoveToCurrentItem[0] && index >= 0 && index < mediaItemsFavorite.size()) {
                        // Lấy dữ liệu của MediaItem đang được chọn tra về LiveData
                        mediaItemLiveData.setValue(mediaItemsFavorite.get(index));
                        //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 101");
                        //  System.out.println("SingleMediaActivity | mediaItemLiveData = " + mediaItemLiveData.getValue());

                        if (!isMoveToCurrentItem[0]) {
                            Log.e("MyTag", "onChanged: " + index);
                            //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 105");

                            viewPager2.setCurrentItem(index, false);
                            //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 108");
                            isMoveToCurrentItem[0] = true;

                        }

                    }
                }
            });
        }
        else{
            MediaItemRepository.getInstance().getAllMediaItemsByAlbumName(albumName).observe(this, new Observer<List<MediaItem>>() {
                @Override
                public void onChanged(List<MediaItem> mediaItems) {
                    //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items");

                    mediaItemsList = mediaItems;


                    viewPagerSingleMediaAdapter.setData(mediaItems);
                    int index = GetInDexOfHelper.getIndexOf(mediaItems, mediaItemIntent);

                    if (!isMoveToCurrentItem[0] && index >= 0 && index < mediaItems.size()) {
                        // Lấy dữ liệu của MediaItem đang được chọn tra về LiveData
                        mediaItemLiveData.setValue(mediaItems.get(index));
                        //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 101");
                        //  System.out.println("SingleMediaActivity | mediaItemLiveData = " + mediaItemLiveData.getValue());

                        if (!isMoveToCurrentItem[0]) {
                            Log.e("MyTag", "onChanged: " + index);
                            //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 105");

                            viewPager2.setCurrentItem(index, false);
                            //  System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 108");
                            isMoveToCurrentItem[0] = true;

                        }

                    }
                }
            });
        }


        viewPagerSingleMediaAdapter.setOnVideoPreparedListener(new ViewPagerSingleMediaAdapter.OnVideoPreparedListener() {
            @Override
            public void onVideoPrepared(ExoPlayer exoPlayer) {
                exoPlayerList.add(exoPlayer);
                Log.e("MyTag", "count: " + exoPlayerList.size());
            }
        });



        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //  System.out.println("Single media activity | on page scrolled 137 | position : " + position);
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                //  System.out.println("Single media activity | on page selected 143 | position : " + position);
                if(position < 0 || position >= mediaItemsList.size()){
                    return;
                }
                mediaItemLiveData.setValue(mediaItemsList.get(position));

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

                //  update và remove comment sau *********
                if(mediaItemsList.get(position).getFileExtension() != null && !mediaItemsList.get(position).getFileExtension().equals("mp4")){
                    //  System.out.println("SingleMediaActivity | File extension = " + mediaItemsList.get(position).getFileExtension() + " | position = " + position);


//                    editImageView.setVisibility(View.VISIBLE);
                    editImageView.setEnabled(true);
                    editImageView.setClickable(true);
                }
                else{
//                    editImageView.setVisibility(View.GONE);
                    editImageView.setEnabled(false);
                    editImageView.setClickable(false);
                }



                deleteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(SingleMediaActivity.this);
                        dialog.setContentView(R.layout.delete_dialog);

                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(true);

                        Button btnDelete = dialog.findViewById(R.id.btnDelete);
                        Button btnCancel = dialog.findViewById(R.id.btnCancel);
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Getting albumName before move to bin and file path
                                String albumName = mediaItemsList.get(position).getAlbumName();
                                String deletedMediaItemPath = mediaItemsList.get(position).getPath();
                                String newThumbnailPath = mediaItemsList.get(mediaItemsList.size() - 1).getPath();
                                if(position == mediaItemsList.size() - 1){
                                    if(mediaItemsList.size() > 1){
                                        newThumbnailPath = mediaItemsList.get(mediaItemsList.size() - 2).getPath();
                                    }
                                }

                                //move to bin
                                MediaItemRepository.getInstance().updateMediaPreviousAlbum(mediaItemsList.get(position).getId(),mediaItemsList.get(position).getAlbumName());
                                MediaItemRepository.getInstance().updateMediaItemAlbum(mediaItemsList.get(position).getId(),"Bin");
                                mediaItemsList.get(position).setPreviousAlbum(mediaItemsList.get(position).getAlbumName());
                                mediaItemsList.get(position).setAlbumName("Bin");
                                //get current day
                                Calendar calendar = Calendar.getInstance();
                                long currentDate = calendar.getTimeInMillis();
                                MediaItemRepository.getInstance().updateMediaItemDeleteTs(mediaItemsList.get(position).getId(),currentDate);
                                mediaItemsList.get(position).setDeletedTs(currentDate);
                                viewPagerSingleMediaAdapter.notifyDataSetChanged();
//                                InnerAlbumScreen.mediaItemAdapter.notifyDataSetChanged();




                                //checking whether the item is coverphotopath of album or not

                                String finalNewThumbnailPath = newThumbnailPath;
                                AlbumRepository.getInstance().getAlbumByAlbumName(AppPreferencesHelper.getInstance().getCurrentUserId(), albumName).observe(SingleMediaActivity.this, new Observer<Album>() {
                                    @Override
                                    public void onChanged(Album album) {
                                        if(album != null){
                                            if(mediaItemsList.size() == 0){
                                                //delete album
                                                AlbumRepository.getInstance().deleteAlbum(AppPreferencesHelper.getInstance().getCurrentUserId(), album.getName());
                                            }
                                            else{
//                                             //update album
                                                AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), album.getName(), finalNewThumbnailPath);
                                                album.setCoverPhotoPath(finalNewThumbnailPath);
                                            }
                                        }
                                    }
                                });
//                                if(!key.isEmpty() && key.equals("Find")){
//                                    Intent response = new Intent(SingleMediaActivity.this, FindFragment.class);
//                                    setResult(RESULT_OK);
//                                }

                                dialog.dismiss();
                                finish();

                            }
                        });
                        dialog.show();
                    }
                });

                editImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent editIntent = new Intent(SingleMediaActivity.this,EditActivity.class);
                        Bundle data = new Bundle();
                        data.putSerializable("meidaItem",mediaItemsList.get(position));
                        editIntent.putExtras(data);
                        startActivityForResult(editIntent,111111);

                    }
                });

                shareImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareImageToInternet(selectedMediaItem);
                    }
                });
                favoriteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  System.out.println("SingleMediaActivity | OnCreate | onClick | update favorite");

                        // Đây chỉ mới là việc thay đổi dữ liêu của Favorite trong database tự định nghĩa
                        // Chúng ta phải tiêến hành cập nhật trong MediaStore.Images.Media/MediaStores.Video.Media trường Favorite nữa
                        selectedMediaItem.setFavorite(!selectedMediaItem.isFavorite());

                        MediaItemRepository.getInstance().updateFavorite(selectedMediaItem.getId(), selectedMediaItem.isFavorite());

                        if(selectedMediaItem.isFavorite()){
                            favoriteImageView.setImageResource(R.drawable.heart_svgrepo_com_color);
                            Toast.makeText(SingleMediaActivity.this,"Added to favorites",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            favoriteImageView.setImageResource(R.drawable.baseline_heart_svgrepo_com);
                            Toast.makeText(SingleMediaActivity.this,"Removed from favorites",Toast.LENGTH_SHORT).show();

                        }
                        MediaItemRepository.getInstance().updateFavorite(selectedMediaItem.getId(), selectedMediaItem.isFavorite());

                    }
                });


                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //  System.out.println("SingleMediaActivity | OnCreate | onPageScrollStateChanged");
                super.onPageScrollStateChanged(state);
            }
        });

        //  System.out.println("SingleMediaActivity | OnCreate | End");

    }

    private List<MediaItem> getFavoriteMediaItems(List<MediaItem> mediaItems) {
        List<MediaItem> mediaItemsFavorite = new ArrayList<>();
        for(MediaItem mediaItem : mediaItems){
            if(mediaItem.isFavorite()){
                mediaItemsFavorite.add(mediaItem);
            }
        }
        return mediaItemsFavorite;
    }

    // Override lại hàm khởi tạo 1 menu, và gán menu đó cho biến mMenu. Có thể thao tác trong file xml cho menu
    // Nhưng không hiểu vì sao lại lỗi, không hiêển thị được icon nên dùng cách này
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  System.out.println("SingleMediaActivity | onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.top_appbar_media_item_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    // Bắt các sự kiện khi các nút trên thanh Action bar được chọn


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //  System.out.println("SingleMediaActivity | onOptionsItemSelected");
        int id = item.getItemId();

        if(id == android.R.id.home ){
            onBackPressed();
        }
        else if(id == R.id.media_information_item){
            DisPlayInforMationAlerDialog(mediaItemLiveData.getValue());


//            DisPlayInforMationAlerDialog(mediaItemsList.get(currentIndex));
//            mediaItemLiveData.observe(this, new Observer<MediaItem>() {
//                @Override
//                public void onChanged(MediaItem mediaItem) {
//                    //  System.out.println("Single Media Activity | On changed 239 " + mediaItem);
//                    DisPlayInforMationAlerDialog(mediaItem);
//                }
//            });
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
            showQRcodeDialog(mediaItemLiveData.getValue().getPath());

//            showQRcodeDialog(mediaItemsList.get(currentIndex).getPath());
        }
        else if(id == R.id.media_convert_text_item){
            textRecognition(mediaItemLiveData.getValue());
        }
        else if(id == R.id.media_note_media_item){
            showNoteDialog();
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
        //  System.out.println("SingleMediaActivity | DisPlayInforMationAlerDialog");

        // Định dạng bạn muốn chuyển đổi sang (đối với ngày/tháng/năm)
        String desiredFormat = "dd/MM/yyyy";

        // Giá trị kiểu long của creationDate
        long creationDateValue = mediaItem.getCreationDate();

        // Chuyển đổi giá trị long thành đối tượng Date
        Date creationDate = new Date(creationDateValue);

        // Chuyển đổi đối tượng Date thành chuỗi với định dạng mới
        String formattedDate = new SimpleDateFormat(desiredFormat).format(creationDate);




        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String lastModified = "";

        if(mediaItem.getLastModified() != null){
            lastModified = new SimpleDateFormat().format(mediaItem.getLastModified());
        }

        builder.setTitle("Chi tiết ảnh")
                .setMessage("* Tên: " + mediaItem.getName() + "\n" +
                        "* Đường dẫn: " + mediaItem.getPath() + "\n" +
                        "* Kích thước: " + formatFileSize(mediaItem.getFileSize()) + " MB" + "\n" +
                        "* Ngày sửa: " + lastModified + "\n" +
                        "* Định dạng: " + mediaItem.getFileExtension() + "\n" +
                        "* Độ phân giải: " + mediaItem.getWidth() + "x" + mediaItem.getHeight() + "\n" +
                        "* Yêu thích: " + (mediaItem.isFavorite() ? "Có" : "Không" )+ "\n" +
                        "* Ngày tạo: " + formattedDate + "\n" +
                        "* Album: " +mediaItem.getAlbumName() + "\n" +
                        "* Location: " + mediaItem.getLocation()+ "\n" +
                        "* Ghi chú: " + mediaItem.getDescription()+ "\n" +
                        "* TAG: " + mediaItem.getTag()+ "\n" +
                        "* Extension: " + mediaItem.getFileExtension() + "\n" )
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent receive ) {
        super.onActivityResult(requestCode, resultCode, receive);
        if(requestCode == 111111 && resultCode == RESULT_OK){
            Bundle data = receive.getExtras();
            String result = data.getString("result");
            String path ;
            Toast.makeText(SingleMediaActivity.this,result,Toast.LENGTH_SHORT).show();
            if(result.equals("SAVE")){
                //  System.out.println("SingleMediaActivity | onActivityResult | result = SAVE");
                path = receive.getStringExtra("afterEdit");         // đường dẫn chứa hình ảnh sau khi đã edit
//                MediaItem item = (MediaItem) data.getSerializable("received");
                //  System.out.println("Before: 555");
                MediaItem item = mediaItemsList.get(currentIndex);
                //  System.out.println("Before: 558");

                //lưu hình ảnh vào database
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path,options);
                //  System.out.println("Before: 563");


                item.setPath(path);
                item.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());

                //  System.out.println("Before: 570");

                // create album if not exist
                String albumName = "Edited";
                item.setAlbumName(albumName);

                //  System.out.println("album name: " + albumName + " | " + AlbumRepository.getInstance().isExistAlbum(albumName));
                if (AlbumRepository.getInstance().isExistAlbum(albumName) == false) {
                    //  System.out.println("SingleMediaActivity | onActivityResult | result = SAVE | album not exist");
                    Album alb = new Album();
                    alb.setName(albumName);
                    alb.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                    alb.setCoverPhotoPath(path);
                    AlbumRepository.getInstance().insert(alb);
                    Toast.makeText(App.getInstance(), "Album " + albumName + " created", Toast.LENGTH_SHORT).show();
                }
                AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), "Edited", path);



                int width = options.outWidth;
                int height = options.outHeight;
                String type = options.outMimeType;
                long size = new File(path).length();
                long date = new Date(System.currentTimeMillis()).getTime();

                item.setWidth(width);
                item.setHeight(height);
                item.setFileSize(size);
                item.setLastModified(date);

//                remove id of item
                item.setId(0);


                MediaItemRepository.getInstance().insert(item);

                //  System.out.println("Image saved ");
                Toast.makeText(SingleMediaActivity.this,"Image saved",Toast.LENGTH_SHORT).show();

            }
        }
    }


    private void textRecognition(MediaItem mediaItem){
        //  System.out.println("SingleMediaActivity | textRecognition | mediaitem: " + mediaItem);
        FirebaseVisionImage image;
        try{
//            image = FirebaseVisionImage.fromFilePath(this,Uri.parse(mediaItem.getPath()));
            image = FirebaseVisionImage.fromFilePath(this,Uri.fromFile(new File(mediaItem.getPath())));
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> textTask =
                    detector.processImage(image)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    String result = firebaseVisionText.getText();
                                    //  System.out.println("SingleMediaActivity | textRecognition | onSuccess | result = " + result);
                                    showOCRResultDialog(result);
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SingleMediaActivity.this,
                                                    "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void showOCRResultDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả")
                .setMessage(text)
                .setPositiveButton("OK", null)
                .setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("text", text);
                        clipboardManager.setPrimaryClip(clip);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void shareImageToInternet(MediaItem mediaItem){
        //  System.out.println("Share image : 337 | mediaitem: " + mediaItem + " | path: " + mediaItem.getPath());

//        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(mediaItem.getPath()));

        Uri uri = Uri.parse(mediaItem.getPath());
        //  System.out.print("Uri: "  + uri);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //  System.out.println("Share image : 342");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share this image with..."));
        //  System.out.println("Share image : 347");

    }
    private void showNoteDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_note_media_item);

        Window window = dialog.getWindow();

        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setAttributes(window.getAttributes());

        dialog.setCancelable(true);

        // Anh xa cac view
        EditText editText = dialog.findViewById(R.id.note_media_discription);
        Button button_save = dialog.findViewById(R.id.save_button);
        Button button_cancel = dialog.findViewById(R.id.cancel_button);

        // Set du lieu cho editText tu database voi truong description
        editText.setText(mediaItemsList.get(currentIndex).getDescription());

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = editText.getText().toString();
                MediaItemRepository.getInstance().updateMediaItemDescription(mediaItemsList.get(currentIndex).getId(), description);
                dialog.dismiss();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

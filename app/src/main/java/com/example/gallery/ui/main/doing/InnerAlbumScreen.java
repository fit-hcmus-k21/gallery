package com.example.gallery.ui.main.doing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Dialog;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.ui.custom.AddImageFromDevice;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;
import com.example.gallery.utils.BytesToStringConverter;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class InnerAlbumScreen extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1;

    RecyclerView recyclerView;
    public static MediaItemAdapter mediaItemAdapter;
    MaterialToolbar topAppBar;
    List<MediaItem> mediaItemList = new ArrayList<>();
    String albumName;
    // -----------
    private FloatingActionButton btnPickFiles;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //  System.out.println("InnerAlbumScreen  27: onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_album_layout);
        topAppBar = findViewById(R.id.topAppBar);
        btnPickFiles = findViewById(R.id.floating_pick_from_device_button_in_album);

        // Lấy dữ liệu từ intent
        Bundle bundle = getIntent().getExtras();
        albumName = bundle.getString("albumName");
//        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);
        //Toolbar here
        Toolbar materialToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Album: " + albumName);

        // Ánh xạ các view
        recyclerView = findViewById(R.id.inner_album_recycler_view);

        // Xử lý LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Xử lý Adapter
        mediaItemAdapter = new MediaItemAdapter();
        recyclerView.setAdapter(mediaItemAdapter);

        MediaItemRepository.getInstance().getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {

                mediaItemList = getMediaItemsOfAlbum(mediaItems, albumName);
//                Log.e("Mytag", "onChanged: " + mediaItemList.size());
                //  System.out.println("InnerAlbumScreen  53: onChanged before set data: mediaItemList = " + mediaItems);
                mediaItemAdapter.setData(mediaItemList);
                //  System.out.println("InnerAlbumScreen  55: onChanged: after set data  " );
            }
        });


        mediaItemAdapter.setOnItemClickListener(new MediaItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MediaItem mediaItem) {
                //  System.out.println("On Item Click | Inner Album Screen before");

                Intent intent = new Intent(InnerAlbumScreen.this, SingleMediaActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("albumName", albumName);
                bundle.putSerializable("mediaItem", mediaItem);

                intent.putExtras(bundle);

                startActivity(intent);
                //  System.out.println("On Item Click | Inner Album Screen after");
            }
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.sort){
                    Toast.makeText(InnerAlbumScreen.this,"SORT",Toast.LENGTH_SHORT).show();
                    //show dialog
                    Dialog dialog = new Dialog(InnerAlbumScreen.this);
                    dialog.setContentView(R.layout.sort_dialog);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);

                    Button btnSave,btnCancel;
                    btnSave = dialog.findViewById(R.id.save);
                    btnCancel = dialog.findViewById(R.id.cancel);

                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RadioGroup radioFactor, radioKind;
                            radioKind = dialog.findViewById(R.id.kind);
                            radioFactor = dialog.findViewById(R.id.factor);
                            int selectedFactor = radioFactor.getCheckedRadioButtonId();
                            String KindOption = null, FactorOption = null;
                            int selectedKind = radioKind.getCheckedRadioButtonId();

                            if(selectedKind != -1){
                                RadioButton selectRadioKind = dialog.findViewById(selectedKind);
                                KindOption = selectRadioKind.getText().toString();
                            }
                            if(selectedFactor != -1){
                                RadioButton selectedRadioFactor = dialog.findViewById(selectedFactor);
                                FactorOption = selectedRadioFactor.getText().toString();
                            }
                            // create Intent;
                            if(FactorOption == null || KindOption == null){
                                Toast.makeText(InnerAlbumScreen.this,"You need to choose all request",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //get album name
                                albumName = albumName.substring(albumName.lastIndexOf('/')+1);
                                Intent arrange = new Intent(InnerAlbumScreen.this, ArrangementAction.class);
                                Bundle data = new Bundle();
                                data.putString("Factor", FactorOption);
                                data.putString("Kind", KindOption);
                                data.putString("Album",albumName);
                                arrange.putExtras(data);
                                startActivity(arrange);
                                dialog.dismiss();
                            }

                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
                if(item.getItemId() == R.id.slideShow){
                    slideShowDialog();
                }
                if(item.getItemId() == R.id.change_name){
                    showRenameDialog(albumName);
                }
                else if(item.getItemId() == R.id.change_thumbnail){
                    chooseThumnailDialog(albumName);
                }
                else if(item.getItemId() == R.id.statistic){
                    showStatisticDialog();
                }
                else if(item.getItemId() == R.id.set_private_album){
                    changePrivateAlbumProperty(0, "Đặt mật khẩu");
                }
                else if(item.getItemId() == R.id.unset_private_album){
                    changePrivateAlbumProperty(1, "Nhập mật khẩu");
                }
                return true;
            }
        });

        // add event for button
        btnPickFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("openAddImageFromDeviceActivity");
                Intent intent = new Intent(InnerAlbumScreen.this, AddImageFromDevice.class);

                Bundle sendBundle = new Bundle();
                sendBundle.putString("albumName", albumName);
                intent.putExtras(sendBundle);

                startActivityForResult(intent, REQUEST_IMAGE_PICK);

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_appbar_inner_album_menu, menu);
        AlbumRepository.getInstance().getAlbumByAlbumName(AppPreferencesHelper.getInstance().getCurrentUserId(), albumName)
            .observe(this, new Observer<Album>() {
                @Override
                public void onChanged(Album album) {
                    if(album != null){
                        if (album.getName().equals("Favorite")) {
                            menu.findItem(R.id.set_private_album).setVisible(false);
                            menu.findItem(R.id.unset_private_album).setVisible(false);
                        }else {
                            menu.findItem(R.id.set_private_album).setVisible(!album.isPrivateAlb());
                            menu.findItem(R.id.unset_private_album).setVisible(album.isPrivateAlb());
                        }
                    }
                }
            });

        return super.onCreateOptionsMenu(menu);
    }


    private List<MediaItem> getMediaItemsOfAlbum(List<MediaItem> mediaItems, String albumName) {
        List<MediaItem> result = new ArrayList<>();
        if(albumName.equals("Favorite")) {
            for(MediaItem mediaItem : mediaItems){
                if(mediaItem.isFavorite()){
                    result.add(mediaItem);
                }
            }
        }
        else{
            for(MediaItem mediaItem : mediaItems){
                if(mediaItem.getAlbumName().equals(albumName)){
                    result.add(mediaItem);
                }
            }
        }
        return result;
    }
    private void showStatisticDialog(){
        long folderSize = 0;
        int imageCnt = 0;
        int videoCnt = 0;
        for(int i = 0 ; i < mediaItemList.size();i++){
            folderSize += mediaItemList.get(i).getFileSize();
            if(mediaItemList.get(i).getFileExtension().equals("mp4"))
                videoCnt++;
            else imageCnt++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thống kê")
                .setMessage("Số ảnh và video: " + mediaItemList.size() +"\n"
                        + "Số ảnh: " + imageCnt + "\n"
                        + "Số video: " + videoCnt + "\n"
                        + "Kích thước: " + BytesToStringConverter.longToString(folderSize))
                .setPositiveButton("OK", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void slideShowDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_slideshow);

        Window window = dialog.getWindow();

        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.TOP);
        window.setAttributes(window.getAttributes());

        dialog.setCancelable(true);

        // Ánh xạ các view
        ViewFlipper viewFlipper = dialog.findViewById(R.id.view_flipper);

        for(MediaItem mediaItem : mediaItemList){
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(mediaItem.getPath()).into(imageView);
            viewFlipper.addView(imageView);
            Log.e("Mytag2", "slideShow: " + mediaItem.getPath());
        }
        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void chooseThumnailDialog(String albumName){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_thumbnail);

        Window window = dialog.getWindow();

        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.TOP);
        window.setAttributes(window.getAttributes());

        dialog.setCancelable(true);

        // Ánh xạ các view
        RecyclerView rcvChooseThumbnail = dialog.findViewById(R.id.choose_thumbnail_recycler_view);

        // Layoutmanager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rcvChooseThumbnail.setLayoutManager(gridLayoutManager);

        // Xu ly adapter
        MediaItemAdapter mediaItemAdapter = new MediaItemAdapter();
        mediaItemAdapter.setData(mediaItemList);
        rcvChooseThumbnail.setAdapter(mediaItemAdapter);

        mediaItemAdapter.setOnItemClickListener(new MediaItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MediaItem mediaItem) {
                String newThumbnailPath = mediaItem.getPath();
                AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), albumName, newThumbnailPath);
                Toast.makeText(InnerAlbumScreen.this, "Đổi ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showRenameDialog(String oldAlbumName) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rename_album);

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
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_rename_album);
        Button btnOke = dialog.findViewById(R.id.btn_ok_rename_album);
        EditText editText = dialog.findViewById(R.id.edt_rename_album);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newAlbumName = editText.getText().toString();
                if(newAlbumName.isEmpty()){
                    Toast.makeText(InnerAlbumScreen.this, "Tên album không được để trống", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlbumRepository.getInstance().updateAlbumName(AppPreferencesHelper.getInstance().getCurrentUserId(), oldAlbumName, newAlbumName);
                    MediaItemRepository.getInstance().updateAlbumName(oldAlbumName, newAlbumName);
                    dialog.dismiss();
                    finish();

                }
            }
        });

        dialog.show();
    }

    // type: 0 -> set private album
    // type: 1 -> unset private album
    private void changePrivateAlbumProperty(int type, String title){
        View view = LayoutInflater.from(this).inflate(R.layout.private_album_password, null);
        TextInputEditText inputEditText = view.findViewById(R.id.password_album);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
            .setView(view)
            .setPositiveButton("OK", (dialog, which) -> {
                String uid = AppPreferencesHelper.getInstance().getCurrentUserId();
                if(type == 0){
                    AlbumRepository.getInstance().updateAlbumIsPrivate(uid,albumName, true,inputEditText.getText().toString());
                }else if(type == 1) {

                    LiveData<Album> liveData = AlbumRepository.getInstance().getAlbumByAlbumName(uid, albumName);
                    Observer<Album> observer = new Observer<Album>() {
                        @Override
                        public void onChanged(Album album) {
                            if (album != null) {
                                if (album.getPassword().equals(inputEditText.getText().toString()))
                                    AlbumRepository.getInstance().updateAlbumIsPrivate(uid, album.getName(), false, "");
                                else
                                    Toast.makeText(InnerAlbumScreen.this, "Mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                            }
                            liveData.removeObserver(this);
                        }
                    };
                    liveData.observe(InnerAlbumScreen.this, observer);
                }
            })
            .setNegativeButton("Hủy", null);
        builder.create().show();
    }
}

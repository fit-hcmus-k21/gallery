package com.example.gallery.ui.main.doing;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Dialog;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;
import com.example.gallery.utils.BytesToStringConverter;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class InnerAlbumScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    public static MediaItemAdapter mediaItemAdapter;
    MaterialToolbar topAppBar;
    List<MediaItem> mediaItemList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //  System.out.println("InnerAlbumScreen  27: onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_album_layout);
        topAppBar = findViewById(R.id.topAppBar);


        // List MediaItem in album


//        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);
        //Toolbar here
        Toolbar materialToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        String albumName = bundle.getString("albumName");
        String albumPath = bundle.getString("albumPath");
//        thumbnailPath = bundle.getString("thumbnailPath");

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

                bundle.putSerializable("mediaItem", mediaItem);
                intent.putExtras(bundle);

                startActivity(intent);
                //  System.out.println("On Item Click | Inner Album Screen after");
            }
        });
        materialToolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.statistic){
                showStatisticDialog();
            }
            return true;
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
                                String albumName = bundle.getString("albumName");
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
                    Toast.makeText(InnerAlbumScreen.this,"SETTING",Toast.LENGTH_SHORT).show();

                    //to do nothing
                }
                else if(item.getItemId() == R.id.change_thumbnail){
                    chooseThumnailDialog(albumPath);
                }
                return true;
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
        return super.onCreateOptionsMenu(menu);
    }


    private List<MediaItem> getMediaItemsOfAlbum(List<MediaItem> mediaItems, String albumName) {
        List<MediaItem> result = new ArrayList<>();

        {
            for(MediaItem mediaItem : mediaItems){
                if(mediaItem.getAlbumName().equals(albumName)){
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
            if(list.get(i).getFileExtension().equals("mp4"))
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
    public void chooseThumnailDialog(String albumPath){
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
                AlbumRepository.getInstance().updateAlbumCoverPhotoPath(albumPath, newThumbnailPath);
                Toast.makeText(InnerAlbumScreen.this, "Đổi ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

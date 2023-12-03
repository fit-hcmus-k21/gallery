package com.example.gallery.ui.main.doing;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class InnerAlbumScreen extends AppCompatActivity {
    MediaItemViewModel mediaItemViewModel;
    RecyclerView recyclerView;
    MaterialToolbar topAppBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_album_layout);
        topAppBar = findViewById(R.id.topAppBar);


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
                                String albumName = albumPath;
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
                    Toast.makeText(InnerAlbumScreen.this,"SLIDESHOW",Toast.LENGTH_SHORT).show();
                    //to do nothing
                }
                if(item.getItemId() == R.id.setting){
                    Toast.makeText(InnerAlbumScreen.this,"SETTING",Toast.LENGTH_SHORT).show();

                    //to do nothing
                }
                return true;
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

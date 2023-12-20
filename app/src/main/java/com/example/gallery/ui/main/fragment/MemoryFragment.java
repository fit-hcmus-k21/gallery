package com.example.gallery.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gallery.R;
import com.example.gallery.data.local.db.repositories.MediaItemRepository;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.ViewModel.AlbumViewModel;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.main.FullImageActivity;
import com.example.gallery.ui.main.adapter.AlbumAdapter;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MemoryFragment extends Fragment implements MediaItemAdapter.OnItemClickListener{

    View view;
    private MediaItemViewModel mediaItemViewModel;

    private RecyclerView recyclerView;

    private MediaItemAdapter mediaItemAdapter;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_memory, container, false);
        MediaItemRepository mediaItemRepository;

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Khởi tạo ViewModel
        mediaItemViewModel = ViewModelProviders.of(this).get(MediaItemViewModel.class);
        textView=view.findViewById(R.id.dateMemory);
        // Ánh xạ các biến
        recyclerView = view.findViewById(R.id.recycleMemoryImg);
        // Xử lý RecyclerView GridManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        // Xử lý Adapter
        mediaItemAdapter = new MediaItemAdapter();
        recyclerView.setAdapter(mediaItemAdapter);
        mediaItemAdapter.setOnItemClickListener((MediaItemAdapter.OnItemClickListener) this);

        // Xử lý dữ liệu
        mediaItemViewModel.getAllMediaItems().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> imgMemory) {
                List<MediaItem>temp= GetNecessaryDetail(imgMemory);
                mediaItemAdapter.setData(temp);
            }
        });

    }




    List<MediaItem> GetNecessaryDetail(List<MediaItem>imgMemory){
        List<MediaItem> temp = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        String yearFlag="F";
        String monthFlag="F";
        long year,month;
        year=month=0;
        long daysDifference = 0;
        for (int i = imgMemory.size() - 1; i >= 0; i--) {
            Instant instant = Instant.ofEpochMilli(imgMemory.get(i).getCreationDate());
            LocalDate localDate1 = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            daysDifference = ChronoUnit.DAYS.between(localDate1, localDate);
            if (monthFlag.equals("F")&&daysDifference%(365)==0){
                if(yearFlag.equals("F")){
                    year=daysDifference/365;
                    yearFlag="T";
                }
                if(daysDifference/365!=year)
                    break;
                temp.add(imgMemory.get(i));

            }
            if(yearFlag.equals("F")&&daysDifference%30==0){
                if(monthFlag.equals("F")){
                    month=daysDifference/30;
                    monthFlag="T";
                }
                if(daysDifference/30!=month)
                    break;
                temp.add(imgMemory.get(i));
            }
        }


        if(yearFlag.equals("T")&&year!=0){
            textView.setText(year+" years ago");
        }
        if(monthFlag.equals("T")&&month!=0){
            textView.setText(month+" months ago");
        }
        if(yearFlag.equals("F")&&monthFlag.equals("F"))
            textView.setText("NO MEMORY TODAY");

        return temp;
    }

    @Override
    public void onItemClick(MediaItem mediaItem) {
        Intent intent = new Intent(getContext(), FullImageActivity.class);
        intent.putExtra("image_path", mediaItem.getPath()); // Truyền đường dẫn hình ảnh đến Activity mới
        startActivity(intent);
    }
}
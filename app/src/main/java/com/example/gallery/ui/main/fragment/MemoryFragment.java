package com.example.gallery.ui.main.fragment;

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
import com.example.gallery.ui.main.adapter.AlbumAdapter;
import com.example.gallery.ui.main.adapter.MediaItemAdapter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MemoryFragment extends Fragment {

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

//        // Thêm Decorations cho RecyclerView là những đường kẻ ngang và dọc
//        RecyclerView.ItemDecoration itemDecorationHorizontal = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
//        RecyclerView.ItemDecoration itemDecorationVertical = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(itemDecorationHorizontal);
//        recyclerView.addItemDecoration(itemDecorationVertical);

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
        List<MediaItem>temp =new ArrayList<>();
        LocalDate localDate=LocalDate.now();
        String flag="blue";
        String flagMemory="false";
        String flagBlock="false";
        int tempi=0;
        for(int i=imgMemory.size()-1;i>=0;i--){
            Instant instant =Instant.ofEpochMilli(imgMemory.get(i).getCreationDate());
            LocalDate localDate1 = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            long daysDifference = ChronoUnit.DAYS.between(localDate1, localDate);
            if(daysDifference>=365&&daysDifference%365==0){
                flagMemory="true";
                if(!flag.equals("red"))
                    tempi= (int) (daysDifference/365);
                flag="red";
                if(tempi!=(daysDifference/365))
                    break;
                temp.add(imgMemory.get(i));

            }
            if(flag.equals("blue")&& daysDifference<365&&daysDifference%30==0){
                flagMemory="true";
                if(flagBlock.equals("false")){
                    tempi= (int) (daysDifference/30);
                    flagBlock="true";
                }

                if(tempi!=(int) (daysDifference/30))
                    break;
                if(tempi!=0)
                    temp.add(imgMemory.get(i));
            }
        }
        if(flagMemory.equals("false")&&tempi==0){
            textView.setText("NO MEMORY IN THIS DAY");
        }
        else{
            if(flag.equals("red"))
                textView.setText(tempi+" Year Ago");
            else
                textView.setText(tempi+" Month Ago");
        }
        return temp;
    }
}
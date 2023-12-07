package com.example.gallery.ui.main.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.ui.main.doing.SingleMediaActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainMediaItemAdapter extends RecyclerView.Adapter<MainMediaItemAdapter.MainMediaItemViewHolder> {
    private List<MediaItem> mediaItemListAll = new ArrayList<>();
    private HashMap<String, List<MediaItem>> groupMediaItemByDate = new HashMap<>();
    private List<String> dateListString = new ArrayList<>();
    public MediaItemAdapter mediaItemAdapter;
    private int currentType = MediaItem.TYPE_GRID;
    //
    public void setData(List<MediaItem> mediaItemListAll, HashMap<String, List<MediaItem>> groupMediaItemByDate, List<String> dateListString) {
        System.out.println("MainMediaItemAdapter 001: setData: mediaItemListAll  ");
       this.mediaItemListAll = mediaItemListAll;
       System.out.println("line 35");
       this.groupMediaItemByDate = groupMediaItemByDate;
        System.out.println("line 37");

       this.dateListString = dateListString;
        System.out.println("line 40");

       notifyDataSetChanged();

       System.out.println("MainMediaItemAdapter 001: after set data");
    }
    public void setCurrentType(int currentType){
        this.currentType = currentType;
        notifyDataSetChanged();
    }

    public HashMap<String, List<MediaItem>>  getData() {
        return groupMediaItemByDate;
    }
    @NonNull
    @Override
    public MainMediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("MainMediaItemAdapter 001: onCreateViewHolder: viewType = " + viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_layout, parent, false);

        return new MainMediaItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMediaItemViewHolder holder, int position) {
        System.out.println("MainMediaItemAdapter 001: onBindViewHolder: position = " + position);

        String date = dateListString.get(position);
        holder.headingTextView.setText(date);

        List<MediaItem> mediaItemList = groupMediaItemByDate.get(date);

        // Tao 2 Layout manager cho viec chuyen doi qua lai khi click vao menu chuyen doi
        GridLayoutManager gridLayoutManager = new GridLayoutManager(holder.itemView.getContext(), 3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);

        if(currentType == MediaItem.TYPE_GRID){
            holder.recyclerView.setLayoutManager(gridLayoutManager);
        }
        else if(currentType == MediaItem.TYPE_LIST) {
            holder.recyclerView.setLayoutManager(linearLayoutManager);
        }

        System.out.println("Before renew adapter line 83");
        mediaItemAdapter = new MediaItemAdapter(); // Phải tạo 1 adapter mới vì nếu dùng adapter của MediaItemAdapter thì sẽ bị lỗi
        // Nói chung nên tạo adapter trong đây để tránh lỗi

        System.out.println("Before set adapter line 87");


        holder.recyclerView.setAdapter(mediaItemAdapter);

        System.out.println("After set data line 92");


        mediaItemAdapter.setData(mediaItemList);

        System.out.println("After set data line 97");

        // Xử lý sự kiện click từ recyclerview con
        mediaItemAdapter.setOnItemClickListener(new MediaItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MediaItem mediaItem) {
                System.out.println("On Item Click | MainMediaItemAdapter before");
                int index = mediaItemListAll.indexOf(mediaItem);

                Intent intent = new Intent(holder.itemView.getContext(), SingleMediaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Bundle bundle = new Bundle();

                bundle.putSerializable("mediaItem", mediaItem);
                intent.putExtras(bundle);

                holder.itemView.getContext().startActivity(intent);
                System.out.println("On Item Click | MainMediaItemAdapter after");
            }
        });

        System.out.println("Finish line 116");


    }

    @Override
    public int getItemCount() {
        if(!groupMediaItemByDate.isEmpty()){
            return groupMediaItemByDate.size();
        }
        return 0;
    }

    public static class MainMediaItemViewHolder extends RecyclerView.ViewHolder {

        TextView headingTextView;
        RecyclerView recyclerView;

        public MainMediaItemViewHolder(@NonNull View itemView) {
            super(itemView);

            headingTextView = itemView.findViewById(R.id.tv_rcv_item);
            recyclerView = itemView.findViewById(R.id.rcv_item);
        }
    }
}

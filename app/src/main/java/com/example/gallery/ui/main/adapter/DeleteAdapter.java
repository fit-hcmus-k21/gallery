package com.example.gallery.ui.main.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.ui.main.doing.SingleMediaDeleteActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.MyViewHolder> {
    List<MediaItem> listDeleteItem;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.delete_photo);
            date = itemView.findViewById(R.id.delete_date);
        }
    }

    public void setData(List<MediaItem> data){
        this.listDeleteItem = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DeleteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteAdapter.MyViewHolder holder, int position) {
        if(listDeleteItem == null && listDeleteItem.isEmpty())
            return ;
        MediaItem mediaItem = this.listDeleteItem.get(position);
        if(mediaItem == null)
            return;
        Glide.with(holder.itemView.getContext()).load(mediaItem.getPath()).into(holder.image);
        //get current date
        Calendar calendar = Calendar.getInstance();
        long currentDate = calendar.getTimeInMillis();
        long deleteDate = mediaItem.getDeletedTs();
        Date day1 = new Date(currentDate);
        Date day2 = new Date(deleteDate);
        long distance = day1.getTime() - day2.getTime();
        long remain = TimeUnit.MILLISECONDS.toDays(distance);
        holder.date.setText((30-remain) + " days");
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), SingleMediaDeleteActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("mediaItem",mediaItem);
                intent.putExtras(data);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listDeleteItem == null)
            return 0;
        return listDeleteItem.size();
    }
}

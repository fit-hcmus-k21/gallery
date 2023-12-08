package com.example.gallery.ui.main.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.ui.main.doing.SingleMediaActivity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder> {
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_VIDEO = 2;
    private List<MediaItem> mediaItemList = new ArrayList<>();
    private OnItemClickListener listener;
    public void setData(List<MediaItem> mediaItemList){
        //  System.out.println("MediaItemAdapter 001: setData: mediaItemList = " + mediaItemList);
        this.mediaItemList = mediaItemList;
        notifyDataSetChanged();
    }
    public List<MediaItem> getData(){
        //  System.out.println("MediaItemAdapter 001: getData: mediaItemList = " + mediaItemList);

        return mediaItemList;
    }
    // In recyclerView that has a function that can get the type to display. watch below
    // This function will be return the type of item
    @Override
    public int getItemViewType(int position) {
        if(mediaItemList != null && mediaItemList.size() > 0) {
//            //  System.out.println("On get item view type 45 | MediaItem Adapter");

            if(mediaItemList.get(position).getFileExtension() != null && mediaItemList.get(position).getFileExtension().equals("video/mp4")){
//                //  System.out.println("On get item view type 47 | MediaItem Adapter");
                return TYPE_VIDEO;
            }
            else {
//                //  System.out.println("On get item view type 51 | MediaItem Adapter");
                return TYPE_PHOTO;
            }
        }
//        //  System.out.println("On get item view type 53 | MediaItem Adapter");
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        //  System.out.println("MediaItemAdapter 001: onCreateViewHolder: viewType = " + viewType);

        View view = null;
        // This switch case is used to check the viewType to inflate the layout for item correctly
        if(viewType == TYPE_PHOTO){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);

        }

        return new MediaItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaItemViewHolder holder, int position) {
        MediaItem mediaItem = mediaItemList.get(position);
        if(mediaItem == null){
            return;
        }
        //  System.out.println("MediaItemAdapter 001: onBindViewHolder: mediaItem = " + mediaItem);
        Glide.with(holder.itemView.getContext()).load(mediaItem.getPath()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                //  System.out.println("MediaItemAdapter | onBindViewHolder | onClick | mediaItem = " + mediaItem);
                listener.onItemClick(mediaItem);
//                //  System.out.println("MediaItemAdapter | onBindViewHolder | onClick | after");
            }
        });
    }

    @Override
    public int getItemCount() {
        if(!mediaItemList.isEmpty()){
            return mediaItemList.size();
        }
        return 0;
    }

    public static class MediaItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MediaItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            //  System.out.println("MediaItemAdapter 001: MediaItemViewHolder: imageView = " + imageView);

            imageView = itemView.findViewById(R.id.img_media_item);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        //  System.out.println("MediaItemAdapter | Set on Item Click Listener | before | listener = " + listener);

        this.listener = listener;

//        //  System.out.println("MediaItemAdapter | Set on Item Click Listener | after | listener = " + listener);

    }
    public interface OnItemClickListener {
        void onItemClick(MediaItem mediaItem);
    }
}

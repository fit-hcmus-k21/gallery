package com.example.gallery.ui.gridview;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.ui.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder> {

    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_VIDEO = 2;
    private List<MediaItem> mediaItemList = new ArrayList<>();
    private OnItemClickListener listener;
    public void setData(List<MediaItem> mediaItemList){
        this.mediaItemList = mediaItemList;
        notifyDataSetChanged();
    }
    public List<MediaItem> getData(){
        return mediaItemList;
    }
    // In recyclerView that has a function that can get the type to display. watch below
    // This function will be return the type of item
    @Override
    public int getItemViewType(int position) {
        if(mediaItemList != null && mediaItemList.size() > 0) {
            if(mediaItemList.get(position).getFileExtension().equals("video/mp4")){
                return TYPE_VIDEO;
            }
            else {
                return TYPE_PHOTO;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        // This switch case is used to check the viewType to inflate the layout for item correctly
//        switch (viewType)
//        {
//            case MediaItem.TYPE_GRID:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
//                break;
//            case MediaItem.TYPE_LIST:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
//                break;
//            case MediaItem.TYPE_STAGGERED:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staggered, parent, false);
//                break;
//            default:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
//        }
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
        Glide.with(holder.itemView.getContext()).load(mediaItem.getPath()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(mediaItem);
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

            imageView = itemView.findViewById(R.id.img_media_item);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(MediaItem mediaItem);
    }



}

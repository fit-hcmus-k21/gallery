package com.example.gallery.ui.main.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;

import java.util.ArrayList;
import java.util.List;

public class CreateStoryAdapter extends RecyclerView.Adapter<CreateStoryAdapter.StoryViewHolder>{

    private List<MediaItem> mediaItems = new ArrayList<>();
    private List<Boolean> checkstate = new ArrayList<>();

    public void setData(List<MediaItem> mediaItems){
        this.mediaItems = mediaItems;
        for(int i = 0; i < mediaItems.size(); i++){
            checkstate.add(false);
        }
        notifyDataSetChanged();
    }
    public List<MediaItem> getData(){
        return mediaItems;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        holder.bind(mediaItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        if(!mediaItems.isEmpty()){
            return mediaItems.size();
        }
        return 0;
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        CheckBox checkBox;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_story_item);
            checkBox = itemView.findViewById(R.id.img_story_checkbox_item);
        }

        public void bind(MediaItem mediaItem, int position){
            if(mediaItem == null){
                return;
            }
            Glide.with(itemView.getContext()).load(mediaItem.getPath()).into(imageView);
            checkBox.setChecked(checkstate.get(position));

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkBox.isChecked()){
                        checkstate.set(position, true);
                    }
                    else{
                        checkstate.set(position, false);
                    }
                }
            });
        }

    }
    public List<MediaItem> getCheckedItems(){
        List<MediaItem> checkedItems = new ArrayList<>();
        for(int i = 0; i < checkstate.size(); i++){
            if(checkstate.get(i)){
                checkedItems.add(mediaItems.get(i));
            }
        }
        return checkedItems;
    }
}

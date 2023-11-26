package com.example.gallery.data.repositories.models.Test;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;

import java.util.ArrayList;
import java.util.List;

public class MediaItemAdapter_Test extends RecyclerView.Adapter<MediaItemAdapter_Test.TestViewHolder>{
    private List<MediaItem> mediaItems;
    private onItemClickListener listener;

    public void setData(List<MediaItem> mediaItems){
        List<MediaItem> mediaItems1 = new ArrayList<>();
        for(MediaItem mediaItem : mediaItems){
            if(mediaItem.getDeletedTs() == 0){
                mediaItems1.add(mediaItem);
            }
        }
        this.mediaItems = mediaItems1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_layout_test, null);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        // Sử dụng Glide là thư viện bên thứ 3 để cải thiện hiệu suất của ứng dụng, có thể dùng Picasso
        Glide.with(holder.imageView.getContext()).load(mediaItems.get(position).getPath()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(mediaItems != null){
            return mediaItems.size();
        }
        return 0;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView cardView;
        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewTest);
            cardView = itemView.findViewById(R.id.cardViewTest);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(mediaItems.get(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener{
        void onItemClick(MediaItem mediaItem);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

}

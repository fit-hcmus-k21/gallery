package com.example.gallery.data.repositories.models;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder>{
    private List<com.example.gallery.data.local.entities.MediaItem> mediaItems;

    public void setData(List<com.example.gallery.data.local.entities.MediaItem> mediaItems){
        this.mediaItems = mediaItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_layout, null);
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
        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewTest);
        }
    }
}

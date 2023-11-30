package com.example.gallery.ui.main.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.ui.main.ImageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class ImageGroupAdapter extends RecyclerView.Adapter<ImageGroupAdapter.ImageGroupViewHolder> {
    private ArrayList<HashMap<String, ArrayList<Integer>>> groups;

    public ImageGroupAdapter(ArrayList<HashMap<String, ArrayList<Integer>>> groups) {
        this.groups = groups;
    }

    @Nonnull
    @Override
    public ImageGroupViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide07_images_group, parent, false);
        return new ImageGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@Nonnull ImageGroupViewHolder holder, int pos) {
        Map<String, ArrayList<Integer>> group = groups.get(pos);

        String heading = group.keySet().iterator().next();
        holder.headingTextView.setText(heading);

        ArrayList<Integer> imageList = group.get(heading);
        ImageAdapter imageAdapter = new ImageAdapter(imageList); // create another adapter for grid view

        holder.gridView.setAdapter(imageAdapter);  // update with imgs


    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ImageGroupViewHolder extends RecyclerView.ViewHolder {
        TextView headingTextView;
        GridView gridView;

        public ImageGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            headingTextView = itemView.findViewById(R.id.viewDateHolder);
            gridView = itemView.findViewById(R.id.gridview);
        }
    }

}

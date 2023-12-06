package com.example.gallery.ui.main.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.ui.main.doing.DuplicationActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DuplicateChildAdapter extends RecyclerView.Adapter<DuplicateChildAdapter.ChildViewHolder> {
    private List<MediaItem> mediaItemList = new ArrayList<>();
    private List<Boolean> checkstate = new ArrayList<>();
    Context mainContext;
    //    private MediaItemAdapter.OnItemClickListener listener;
    public void setData(List<MediaItem> mediaItemList, List<Boolean> check){
        this.mediaItemList = mediaItemList;
        this.checkstate = check;
        notifyDataSetChanged();
    }
    public List<MediaItem> getData(){return  this.mediaItemList;}

    public DuplicateChildAdapter(Context context){
        this.mainContext = context;
    }

    @NonNull
    @Override
    public DuplicateChildAdapter.ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duplicate_child_screen,parent,false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DuplicateChildAdapter.ChildViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MediaItem mediaItem = this.mediaItemList.get(position);
        if(mediaItem == null)
            return;

        Glide.with(holder.itemView.getContext()).load(mediaItem.getPath()).into(holder.similarItem);
        holder.similarCheckbox.setChecked(checkstate.get(position));
    }

    @Override
    public int getItemCount() {
        return mediaItemList.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder{

        CheckBox similarCheckbox;
        ImageView similarItem;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            similarItem = itemView.findViewById(R.id.SimilarItem);
            similarCheckbox = itemView.findViewById(R.id.SimilarCheckbox);
            similarCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked == checkstate.get(getAdapterPosition())) return;
                    checkstate.set(getAdapterPosition(),isChecked);
                    if(isChecked) {
                        DuplicationActivity.countDel++;
                        DuplicationActivity.sizeDel+= mediaItemList.get(getAdapterPosition()).getFileSize();
                        DuplicationActivity.btnDelete.setClickable(true);
                        DuplicationActivity.btnDelete.setEnabled(true);
                        DuplicationActivity.btnDelete.setBackgroundColor(mainContext.getResources().getColor(R.color.selected));
                        DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Định dạng với 2 chữ số thập phân
                        String roundedValue = decimalFormat.format((double)DuplicationActivity.sizeDel/(1024*1024));
                        DuplicationActivity.btnDelete.setText("DELETE(" + DuplicationActivity.countDel + " item, " + roundedValue + " MB)");
                    }
                    else {
                        DuplicationActivity.countDel--;
                        DuplicationActivity.sizeDel -= mediaItemList.get(getAdapterPosition()).getFileSize();
                        if(DuplicationActivity.countDel <= 0){
                            DuplicationActivity.btnDelete.setClickable(false);
                            DuplicationActivity.btnDelete.setEnabled(false);
                            DuplicationActivity.btnDelete.setBackgroundColor(mainContext.getResources().getColor(R.color.grey));
                            DuplicationActivity.btnDelete.setText("Delete");
                        }
                        else{
                            DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Định dạng với 2 chữ số thập phân
                            String roundedValue = decimalFormat.format((double)DuplicationActivity.sizeDel/(1024*1024));
                            DuplicationActivity.btnDelete.setText("DELETE(" + DuplicationActivity.countDel + " item, " + roundedValue + " MB)");
                        }
                    }
                }
            });

        }
    }
}

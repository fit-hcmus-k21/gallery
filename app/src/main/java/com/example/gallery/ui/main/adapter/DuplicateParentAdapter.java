package com.example.gallery.ui.main.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
//import com.facebook.bolts.BoltsExecutors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DuplicateParentAdapter extends RecyclerView.Adapter<DuplicateParentAdapter.ParentViewHolder> {
    private List<MediaItem> mediaItemListAll = new ArrayList<>();
    private HashMap<String, List<MediaItem>> groupMediaItemByDate = new HashMap<>();
    private List<String> dateListString = new ArrayList<>();
    public DuplicateChildAdapter duplicateChildAdapter;
    private HashMap<String,List<Boolean>> checkState = new HashMap<>();


    Context mainContext;

    public void setData(List<MediaItem> mediaItemListAll, HashMap<String, List<MediaItem>> groupMediaItemByDate, List<String> dateListString, HashMap<String,List<Boolean>> checkstate){
        this.mediaItemListAll = mediaItemListAll;
        this.groupMediaItemByDate = groupMediaItemByDate;
        this.dateListString = dateListString;
        this.checkState = checkstate;
        notifyDataSetChanged();
    }
    public DuplicateParentAdapter(Context context){
        this.mainContext = context;
    }
    public HashMap<String, List<MediaItem>> getData(){return this.groupMediaItemByDate;}

    @NonNull
    @Override
    public DuplicateParentAdapter.ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duplicate_parent_screen,parent,false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DuplicateParentAdapter.ParentViewHolder holder, int position) {
        String date = dateListString.get(position);
        //chuyển về format dd/mm/yyyy
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy/MM/dd") ;
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
        String display;
        try {
            Date oldDate = oldFormat.parse(date);
            display = newFormat.format(oldDate);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.dateTitle.setText(display);

        List<MediaItem> mediaItemList = groupMediaItemByDate.get(date);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(holder.similarRecyclerview.getContext(),3,GridLayoutManager.VERTICAL,false);
        holder.similarRecyclerview.setLayoutManager(layoutManager);

        duplicateChildAdapter = new DuplicateChildAdapter(mainContext);
        holder.similarRecyclerview.setAdapter(duplicateChildAdapter);

        duplicateChildAdapter.setData(mediaItemList,checkState.get(date));
    }

    @Override
    public int getItemCount() {
        if(!groupMediaItemByDate.isEmpty())
            return groupMediaItemByDate.size();
        else return 0;
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder{
        private TextView dateTitle;
        private RecyclerView similarRecyclerview;


        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTitle = itemView.findViewById(R.id.DateTitle);
            similarRecyclerview = itemView.findViewById(R.id.SimilarRecyclerview);

        }
    }
}

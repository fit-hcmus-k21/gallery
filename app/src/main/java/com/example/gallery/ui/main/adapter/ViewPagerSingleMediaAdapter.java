package com.example.gallery.ui.main.adapter;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerSingleMediaAdapter extends RecyclerView.Adapter<ViewPagerSingleMediaAdapter.ViewPagerSingleMediaViewHolder> {
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_VIDEO = 2;
    List<MediaItem> mediaItemList = new ArrayList<>();

    Context context;


    public void setData(List<MediaItem> mediaItemList){
        System.out.println("View Pager Single Media Adapter 001: setData: mediaItemList = " + mediaItemList);

        this.mediaItemList = mediaItemList;
        notifyDataSetChanged();
    }
    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(mediaItemList != null && mediaItemList.size() > 0) {
            System.out.println("On get item view type 47 | View Pager Single Media Adapter");
            if(mediaItemList.get(position).getFileExtension() != null  && mediaItemList.get(position).getFileExtension().equals("video/mp4")){
                return TYPE_VIDEO;
            }
            else {
                return TYPE_PHOTO;
            }
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewPagerSingleMediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("View Pager Single Media Adapter |Begin | onCreateViewHolder: viewType = " + viewType);
        View view = null;

        if(viewType == TYPE_PHOTO){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_media, parent, false);
        }
        else if(viewType == TYPE_VIDEO){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_video, parent, false);
        }
        assert view != null;
        System.out.println("View Pager Single Media Adapter | End | onCreateViewHolder: view = " + view);
        return new ViewPagerSingleMediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerSingleMediaViewHolder holder, int position) {
        System.out.println("View Pager Single Media Adapter start: onBindViewHolder: position = " + position);
        MediaItem mediaItem = mediaItemList.get(position);

        int viewType = getItemViewType(position);

        if(viewType == TYPE_PHOTO){
            Glide.with(holder.itemView.getContext()).load(mediaItem.getPath()).into(holder.imageView);

        }
        else{
            //TODO vòng đời của exoplayer chưa được quan lý tốt, khi thoát khỏi video thì video đó chưa được tắt hãy cố gắng tắt nó
            ExoPlayer exoPlayer = new ExoPlayer.Builder(holder.itemView.getContext()).build();
            holder.playerView.setPlayer(exoPlayer);

            androidx.media3.common.MediaItem mediaItemx = androidx.media3.common.MediaItem.fromUri(mediaItem.getPath());
            exoPlayer.setMediaItem(mediaItemx);

            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(true);


        }
        System.out.println("View Pager Single Media Adapter end: onBindViewHolder: position = " + position);

    }

    private void setFullScreen() {

        if(context instanceof AppCompatActivity){

            AppCompatActivity activity = (AppCompatActivity) context;

            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().hide();
            }
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public int getItemCount() {
        if(mediaItemList != null)
            return mediaItemList.size();
        return 0;
    }

    public class ViewPagerSingleMediaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        PlayerView playerView;
        public ViewPagerSingleMediaViewHolder(@NonNull View itemView) {

            super(itemView);
            int viewType = getItemViewType();

            System.out.println("View Pager Single Media Adapter | start | ViewPagerSingleMediaViewHolder: itemView = " + itemView);


            imageView = itemView.findViewById(R.id.single_media_view);
            playerView = itemView.findViewById(R.id.player_view);
        }
    }
}

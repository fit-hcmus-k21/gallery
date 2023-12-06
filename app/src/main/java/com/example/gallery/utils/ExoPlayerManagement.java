package com.example.gallery.utils;

import android.content.Context;

import androidx.media3.exoplayer.ExoPlayer;

public class ExoPlayerManagement {
    private ExoPlayer exoPlayer;

    public void initExoPlayer(Context context, String path){
        exoPlayer = new ExoPlayer.Builder(context).build();
        androidx.media3.common.MediaItem mediaItemx = androidx.media3.common.MediaItem.fromUri(path);
        exoPlayer.setMediaItem(mediaItemx);

        exoPlayer.prepare();
    }
    public void releaseExoPlayer(){
        if(exoPlayer != null)
            exoPlayer.release();
    }
    public ExoPlayer getExoPlayer(){
        return exoPlayer;
    }
}

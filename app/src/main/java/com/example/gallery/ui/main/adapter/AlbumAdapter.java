package com.example.gallery.ui.main.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.ui.main.doing.InnerAlbumScreen;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<Album> albums = new ArrayList<>();
    public void setData(List<Album> albums){
        this.albums = albums;
        notifyDataSetChanged();
    }
    public List<Album> getData(){
        return albums;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_album, null);

        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albums.get(position);
        if(album == null){
            return;
        }

        holder.textView.setText(album.getName());

        Glide.with(holder.imageView.getContext())
                .load(albums.get(position).getCoverPhotoPath())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  System.out.println("Album Adapter | onBindViewHolder: onClick: album = " + album);
                Intent intent = new Intent(holder.itemView.getContext(), InnerAlbumScreen.class);

                Bundle bundle = new Bundle();

                bundle.putString("albumName", album.getName());
                bundle.putString("albumPath", album.getPath());
//                bundle.putString("thumbnailPath", album.getCoverPhotoPath());
                //  System.out.println( "onClick album name: " + album.getName());
                intent.putExtras(bundle);

                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(albums != null) {
            return albums.size();
        }
        return 0;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public AlbumViewHolder(@NonNull View itemView) {

            super(itemView);

            //  System.out.println("Album Adapter | AlbumViewHolder: itemView = " + itemView);
            imageView = itemView.findViewById(R.id.img_album);
            textView = itemView.findViewById(R.id.txt_album_name);


        }
    }
}

package com.example.gallery.data.repositories.models.Test;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gallery.data.local.entities.Album;

import com.example.gallery.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter_Test extends BaseAdapter {
    private Context context;
    private List<Album> albums;
    private onItemClickListener listener;
    public AlbumAdapter_Test(Context context){
        this.context = context;
    }
    public void setData(List<Album> albums){
        List<Album> albums1 = new ArrayList<>();
        for(Album album : albums){
            if(album.getDeletedTs() == 0){
                albums1.add(album);
            }
        }
        this.albums = albums1;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(albums != null){
            return albums.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(position > 0 && position < albums.size()){
            return albums.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewName;
        TextView textViewPath;
        TextView textViewUserID;
        TextView textViewDate;
        ImageView imageViewCoverPhoto;
        LinearLayout albumItemLayout;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.album_layout_test, null);
        }
        textViewName = (TextView) convertView.findViewById(R.id.albumTextViewName);
        textViewPath = (TextView) convertView.findViewById(R.id.albumTextViewFolderPath);
        textViewUserID = (TextView) convertView.findViewById(R.id.albumTextViewUserID);
        textViewDate = (TextView) convertView.findViewById(R.id.albumTextViewLastModified);
        imageViewCoverPhoto = (ImageView) convertView.findViewById(R.id.albumImageView);
        albumItemLayout = (LinearLayout) convertView.findViewById(R.id.albumItem_layout);

        Glide.with(context)
                .load(albums.get(position).getCoverPhotoPath())
                .into(imageViewCoverPhoto);
        textViewDate.setText(String.valueOf(albums.get(position).getCreationDate()));
        textViewName.setText(albums.get(position).getName());
        textViewPath.setText(albums.get(position).getPath());
        textViewUserID.setText(albums.get(position).getUserID() + " ");
        albumItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Mytask", "AlbumAdapter_Test: " + albums.get(position).getName());
                if(listener != null){
                    listener.onItemClick(albums.get(position));
                }
            }
        });

        return convertView;
    }

    public interface onItemClickListener{
        void onItemClick(Album album);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}

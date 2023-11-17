package com.example.gallery.data.repositories.models;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gallery.data.local.entities.Album;

import com.example.gallery.R;

import java.util.List;

public class TestAlbumAdapter extends BaseAdapter {
    private Context context;
    private List<Album> albums;

    public TestAlbumAdapter(Context context, List<Album> albums){
        this.context = context;
        this.albums = albums;
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
        if(convertView == null){
            convertView = View.inflate(context, R.layout.album_layout, null);
        }
        textViewName = (TextView) convertView.findViewById(R.id.albumTextViewName);
        textViewPath = (TextView) convertView.findViewById(R.id.albumTextViewFolderPath);
        textViewUserID = (TextView) convertView.findViewById(R.id.albumTextViewUserID);
        textViewDate = (TextView) convertView.findViewById(R.id.albumTextViewLastModified);
        imageViewCoverPhoto = (ImageView) convertView.findViewById(R.id.albumImageView);

        Glide.with(context)
                .load(albums.get(position).getCoverPhotoPath())
                .into(imageViewCoverPhoto);
        textViewDate.setText(String.valueOf(albums.get(position).getCreationDate()));
        textViewName.setText(albums.get(position).getName());
        textViewPath.setText(albums.get(position).getPath());
        textViewUserID.setText(albums.get(position).getUserID() + " ");

        return convertView;
    }
}

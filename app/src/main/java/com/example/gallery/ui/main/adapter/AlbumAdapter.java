package com.example.gallery.ui.main.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gallery.R;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.ui.main.doing.InnerAlbumScreen;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
                if(album.isPrivateAlb()){
                    View view = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.private_album_password, null);
                    TextInputEditText inputEditText = view.findViewById(R.id.password_album);
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Nhập mật khẩu")
                        .setView(view)
                        .setPositiveButton("OK", (dialog, which) -> {
                            if(album.getPassword().equals(inputEditText.getText().toString()))
                                navigateToInnerAlbumScreen(holder.itemView.getContext(),album);
                            else Toast.makeText(holder.itemView.getContext(), "Mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                        })
                            .setNegativeButton("Hủy", null);
                    builder.create().show();
                }
                else {
                    navigateToInnerAlbumScreen(holder.itemView.getContext(),album);
                }
            }
        });
    }
    private void navigateToInnerAlbumScreen(Context context, Album album) {
        Intent intent = new Intent(context, InnerAlbumScreen.class);
        Bundle bundle = new Bundle();
        bundle.putString("albumName", album.getName());
        intent.putExtras(bundle);
        context.startActivity(intent);
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

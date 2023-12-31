package com.example.gallery.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gallery.R;
import com.example.gallery.ui.main.Slide24_PhotoDetailViewScreenActivity;

public class Slide24_ListAdapter extends BaseAdapter{
    public Integer[] mainPic;
    public Context context;
    public String[] infoPic;
    public Slide24_ListAdapter(Context main, int layout, String[] info,Integer[] pic){
        context = main;
        mainPic = pic;
        infoPic = info;
    }


    @Override
    public int getCount() {
        return mainPic.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Slide24_PhotoDetailViewScreenActivity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.slide08_image_list,null);
        ImageView mainPicture =(view).findViewById(R.id.mainPicture);
        TextView information = (view).findViewById(R.id.infoPicture);
        mainPicture.setImageResource(mainPic[position]);
        information.setText((infoPic[position]));
        return view;
    }
}
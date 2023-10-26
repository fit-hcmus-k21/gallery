package com.example.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findlayout.R;

public class myAdapter extends BaseAdapter{
    public Integer[] mainPic;
    public Context context;
    public String[] infoPic;

    public myAdapter(Context main, int layout, String[] info, Integer[] pic){
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
        LayoutInflater inflater = ((mainActivity)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.item,null);
        ImageView mainPicture = (row).findViewById(R.id.mainPicture);
        TextView information = (row).findViewById(R.id.infoPicture);
        mainPicture.setImageResource(mainPic[position]);
        information.setText(infoPic[position]);
        return row;
    }
}
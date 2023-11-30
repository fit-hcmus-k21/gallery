package com.example.gallery.ui.main.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gallery.ui.main.fragment.AlbumFragment;
import com.example.gallery.ui.main.fragment.FindFragment;
import com.example.gallery.ui.main.fragment.MediaItemFragment;
import com.example.gallery.ui.main.fragment.MemoryFragment;

public class MyViewPager2Adapter extends FragmentStateAdapter {

    public MyViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MediaItemFragment();
            case 1:
                return new AlbumFragment();
            case 2:
                return new FindFragment();
            case 3:
                return new MemoryFragment();
            default:
                return new MediaItemFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

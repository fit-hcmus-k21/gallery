package com.example.gallery.ui.main.doing;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gallery.R;

import com.example.gallery.data.repositories.models.HelperFunction.RequestPermissionHelper;

import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.ui.main.adapter.MyViewPager2Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doing_main_activity);
        //  System.out.println("in oncreate main: 42");


        fetchData();

    }
    private void fetchData() {

        // Ánh xạ các Widget
        viewPager2 = findViewById(R.id.viewPager2_main);
        bottomNavigationView = findViewById(R.id.bottomNavigation_main);

        // Khởi tạo 1 Viewpager2 adapter
        MyViewPager2Adapter myViewPager2Adapter = new MyViewPager2Adapter(this);
        viewPager2.setAdapter(myViewPager2Adapter);

        // Xử lý sự kiện khi click vào 1 item dưới bottom navigation sẽ chuyển fragment tương ứng
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Lấy ra id của phần từ được click và so sánh => chuyển đổi Viewpager tương ứng
                // 0: Photos; 1: Albums; 2: Finding; 3: Memory
                int id = item.getItemId();
                if(id == R.id.nav_photos){
                    viewPager2.setCurrentItem(0);
                }
                else if (id == R.id.nav_albums){
                    viewPager2.setCurrentItem(1);
                }
                else if (id == R.id.nav_finding){
                    viewPager2.setCurrentItem(2);
                }
                else if (id == R.id.nav_memories){
                    viewPager2.setCurrentItem(3);
                } else if (id == R.id.nav_profile){
                    viewPager2.setCurrentItem(4);
                }

                return true;
            }
        });

        // Sử lý sự kiện lướt Viewpager2 sang trái hoặc phải
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            // Override phương thức thứ 2, hàm callback sẽ trả về cho ta 1 postion
            // Sử lý postion với từng fragment cụ thể và đồng bộ cho bottomnavigation item
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position)
                {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.nav_photos).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.nav_albums).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.nav_finding).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.nav_memories).setChecked(true);
                        break;

                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              System.out.println("in onRequestPermissionsResult main: " + requestCode);
            // Sử dụng Executor
            Executor executor = Executors.newSingleThreadExecutor();

            executor.execute(() -> {
                  System.out.println("in executor main: " + requestCode);

                new Handler(Looper.getMainLooper()).post(() -> MediaItemRepository.getInstance().fetchData());


            });
        } else {
            System.out.println("else on result permission: " + requestCode);
            fetchData();
        }

    }

}

package com.example.gallery.ui.main.doing;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gallery.R;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.User;
import com.example.gallery.data.repositories.models.HelperFunction.RequestPermissionHelper;
import com.example.gallery.data.repositories.models.ViewModel.AlbumViewModel;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;
import com.example.gallery.ui.main.adapter.MyViewPager2Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    UserViewModel userViewModel;
    AlbumViewModel albumViewModel;
    MediaItemViewModel mediaItemViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doing_main_activity);

        // Khoi tao cac viewmodel
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        albumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);
        mediaItemViewModel = ViewModelProviders.of(this).get(MediaItemViewModel.class);

        //TODO kiêểm tra lại phần quyền truy cập
        if(RequestPermissionHelper.checkAndRequestPermission(this, 101)){
            fetchData();
        }
//        else{
//            Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show();
//        }

        // ****************************** End code tạm thời ******************************

//        // Ánh xạ các Widget
//        viewPager2 = findViewById(R.id.viewPager2_main);
//        bottomNavigationView = findViewById(R.id.bottomNavigation_main);
//
//        // Khởi tạo 1 Viewpager2 adapter
//        MyViewPager2Adapter myViewPager2Adapter = new MyViewPager2Adapter(this);
//        viewPager2.setAdapter(myViewPager2Adapter);
//
//        // Xử lý sự kiện khi click vào 1 item dưới bottom navigation sẽ chuyển fragment tương ứng
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                // Lấy ra id của phần từ được click và so sánh => chuyển đổi Viewpager tương ứng
//                // 0: Photos; 1: Albums; 2: Finding; 3: Memory
//                int id = item.getItemId();
//                if(id == R.id.nav_photos){
//                    viewPager2.setCurrentItem(0);
//                }
//                else if (id == R.id.nav_albums){
//                    viewPager2.setCurrentItem(1);
//                }
//                else if (id == R.id.nav_finding){
//                    viewPager2.setCurrentItem(2);
//                }
//                else if (id == R.id.nav_memories){
//                    viewPager2.setCurrentItem(3);
//                }
//
//                return true;
//            }
//        });
//
//        // Sử lý sự kiện lướt Viewpager2 sang trái hoặc phải
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            // Override phương thức thứ 2, hàm callback sẽ trả về cho ta 1 postion
//            // Sử lý postion với từng fragment cụ thể và đồng bộ cho bottomnavigation item
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//
//                switch (position)
//                {
//                    case 0:
//                        bottomNavigationView.getMenu().findItem(R.id.nav_photos).setChecked(true);
//                        break;
//                    case 1:
//                        bottomNavigationView.getMenu().findItem(R.id.nav_albums).setChecked(true);
//                        break;
//                    case 2:
//                        bottomNavigationView.getMenu().findItem(R.id.nav_finding).setChecked(true);
//                        break;
//                    case 3:
//                        bottomNavigationView.getMenu().findItem(R.id.nav_memories).setChecked(true);
//                        break;
//                }
//
//            }
//        });

    }

    private void fetchData() {

//         ******************************  Lấy dữ liệu từ external - Code tam thời ******************************
        userViewModel.insertUser(new User(1, "User1", "", "user1", "123",
                "user1@example.com", "", "", "", ""));
        userViewModel.insertUser(new User(10, "User2", "", "user2", "123",
                "user2@example.com", "", "", "", ""));
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if(users != null && !users.isEmpty()){
                    albumViewModel.fetchData();
                }
            }
        });
        albumViewModel.getAllAlbums().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                if(albums != null && !albums.isEmpty()){
                    mediaItemViewModel.fetchData();
                }
            }
        });
// ******************************  Lấy dữ liệu từ external - Code tam thời ******************************

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
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            fetchData();

        }
    }
}

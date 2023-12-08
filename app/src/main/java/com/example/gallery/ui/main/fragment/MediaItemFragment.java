package com.example.gallery.ui.main.fragment;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gallery.App;
import com.example.gallery.R;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.MediaItem;

import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.ui.main.adapter.MainMediaItemAdapter;

import com.example.gallery.utils.BytesToStringConverter;
import com.example.gallery.ui.main.doing.DuplicationActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MediaItemFragment extends Fragment {

    public static final int REQUEST_TAKE_PHOTO = 256;
    public static final int REQUEST_SIMILAR_PHOTO = 123;

    View mView;
    private RecyclerView recyclerView;
    private MainMediaItemAdapter mainMediaItemAdapter;
    private int mCurrentType = MediaItem.TYPE_GRID;
    private Menu mMenu; // use this to change icon of menu

    private Toolbar toolbar;

    // 3 Layout management
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    List<String> dateListString;
    HashMap<String, List<MediaItem>> mediaItemGroupByDate;
    List<MediaItem> mediaItemsLíst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_media_item, container, false);

        setHasOptionsMenu(true); // Cho phép fragment có thể tạo menu

        toolbar = mView.findViewById(R.id.toolbar_media_item);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Gallery - Media");


        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo viewModel
        dateListString = new ArrayList<>();

        // Ánh xạ các biến
        recyclerView = view.findViewById(R.id.rcv_media_item);
        toolbar = view.findViewById(R.id.toolbar_media_item);



        // Xử lý các layoutmanager
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        linearLayoutManager = new LinearLayoutManager(getContext());

        // Default layout manager
        recyclerView.setLayoutManager(linearLayoutManager);

        // Adapter for recycler view
        mainMediaItemAdapter = new MainMediaItemAdapter();
        recyclerView.setAdapter(mainMediaItemAdapter);
//        mainMediaItemAdapter.mediaItemAdapter.setOnItemClickListener(new MediaItemAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(MediaItem mediaItem) {
//                Log.e("Mytag", "onItemClick: " + mediaItem.getPath());
//
////                Intent intent = new Intent(holder.imageView.getContext(), SingleMediaActivity.class);
////
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("listmediaitem", (ArrayList<MediaItem>)mediaItemList);
////                bundle.putSerializable("mediaitem", mediaItem);
////                intent.putExtras(bundle);
////
////                holder.imageView.getContext().startActivity(intent);
//            }
//        });

        // check if mediaItemViewModel is null or mediaItemViewModel.getAllMediaItems() is null

        // Data from viewModel
        MediaItemRepository.getInstance().getAllMediaItems().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {

            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                if(mediaItems == null) {
                    return;
                }

                for(MediaItem mediaItem : mediaItems){
                    System.out.println("MediaItemFragment 001: onViewCreated: getAllMediaItems: onChanged: in loop");

                    mediaItem.setTypeDisplay(mCurrentType);
                }

                mediaItemGroupByDate = setMediaItemGroupByDate(mediaItems);

                mediaItemsLíst = mediaItems;


                mainMediaItemAdapter.setData(mediaItemsLíst, mediaItemGroupByDate, dateListString); // trong adapter có hàm setData và có notifydatasetchanged
                System.out.println("on observe : " + mediaItems.size() + " before set hash map");

                HashMap<String, List<MediaItem>> mediaItemGroupByDate = setMediaItemGroupByDate(mediaItems);

                System.out.println("on observe : after set hash map , before set data");


                mainMediaItemAdapter.setData(mediaItems, mediaItemGroupByDate, dateListString); // trong adapter có hàm setData và có notifydatasetchanged

                System.out.println("on observe : after set hash map , after set data");



            }
        });

    }

    // Hàm này sẽ nhận vào 1 list mediaItem và trả về 1 hashmap với key là ngày tháng năm, value là 1 list mediaItem
    private  HashMap<String, List<MediaItem>> setMediaItemGroupByDate(List<MediaItem> mediaItems) {
        HashMap<String, List<MediaItem>> result = new HashMap<>();

        for(MediaItem mediaItem : mediaItems){
            String date = new SimpleDateFormat("dd/MM/yyyy").format(mediaItem.getCreationDate());
            if(result.containsKey(date)){
                result.get(date).add(mediaItem);
            }else{
                // Theem vao dateListString
                dateListString.add(date);

                List<MediaItem> mediaItemList = new ArrayList<>();
                mediaItemList.add(mediaItem);
                result.put(date, mediaItemList);
            }
        }

        // sort dateListString desc
//        for(int i = 0 ; i < dateListString.size() - 1; i++){
//            for(int j = i + 1; j < dateListString.size(); j++){
//                String date1 = dateListString.get(i);
//                String date2 = dateListString.get(j);
//                if(date1.compareTo(date2) < 0){
//                    String temp = date1;
//                    dateListString.set(i, date2);
//                    dateListString.set(j, temp);
//                }
//            }
//        }

        return result;
    }

//     Khởi tạo 1 menu onCreateOptionsMenu

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.topbar_media_item_menu, menu);
        mMenu = menu;
    }

    // Bắt sự kiện chọn item trên menu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.modeDisplayView){
            Toast.makeText(getContext(), "Change type display", Toast.LENGTH_SHORT).show();
            onClickChangeTypeDisplay();
        }
        else if(id == R.id.camera_item){
            takeAPicture();
        }else if(id == R.id.similarPhoto){
            Intent intent = new Intent(getContext(), DuplicationActivity.class);
            startActivityForResult(intent,REQUEST_SIMILAR_PHOTO);
        }

        else if(id == R.id.statistic){
            showStatisticDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void _takeAPicture() {
        // Gọi intent để chụp ảnh
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Kiểm tra có đủ điều kiện để chụp ảnh hay không về mặt phần cứng
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            // Gọi startActivityForResult để nhận kết quả trả về
            System.out.println("MediaItemFragment 244: takeAPicture: before");
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            System.out.println("MediaItemFragment 244: takeAPicture: after");

            // lấy dữ liệu ảnh mới được chụp
            Bundle bundle = takePictureIntent.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");

            // lấy đường dẫn của ảnh
            String uri = "";

            System.out.println("MediaItemFragment 244: takeAPicture: uri: " + uri);



        }

    }

    private String currentPhotoPath;

    // Gọi hàm này để chụp ảnh
    private void takeAPicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                Uri uri = FileProvider.getUriForFile(App.getInstance(), App.getProcessName() + ".provider", new File(photoFile.getPath()));


                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // Hàm này tạo một file để lưu ảnh
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    // Hàm này xử lý kết quả trả về từ intent chụp ảnh
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            // Ảnh đã được chụp thành công
            Toast.makeText(getContext(), "Chụp ảnh thành công", Toast.LENGTH_SHORT).show();

            // Sử dụng currentPhotoPath để truy cập đường dẫn của ảnh
            if (currentPhotoPath != null) {
                // In đường dẫn ra Log (hoặc thực hiện các thao tác khác)
                Log.d("YourFragment", "Đường dẫn ảnh đã chụp: " + currentPhotoPath);
                MediaItem item = new MediaItem();
                item.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                item.setPath(currentPhotoPath);
                item.setAlbumName("Camera");
                item.setCreationDate(new Date().getTime());

                MediaItemRepository.getInstance().insert(item);
            }
        }
    }




    public void onClickChangeTypeDisplay(){ // Bao gồm việc thây đổi type hiển thị cho item, và đổi icon của menu, đổi thêm layoutmanager
        if(mCurrentType == MediaItem.TYPE_GRID){
//            setTypeDisplayRecyclerView(MediaItem.TYPE_LIST);
            mainMediaItemAdapter.setCurrentType(MediaItem.TYPE_LIST);
            mCurrentType = MediaItem.TYPE_LIST;
        }else if(mCurrentType == MediaItem.TYPE_LIST){
//            setTypeDisplayRecyclerView(MediaItem.TYPE_STAGGERED);
            mainMediaItemAdapter.setCurrentType(MediaItem.TYPE_GRID);
            mCurrentType = MediaItem.TYPE_GRID;
        }
        setIconMenu();
    }

    private void setIconMenu() {
        switch (mCurrentType)
        {
            case MediaItem.TYPE_GRID:
                mMenu.getItem(1).setIcon(R.drawable.baseline_grid_view_24);

                break;
            case MediaItem.TYPE_LIST:
                mMenu.getItem(1).setIcon(R.drawable.baseline_list_24);

                break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK){
//            Toast.makeText(getContext(), "Take a picture success", Toast.LENGTH_SHORT).show();
//            // Lấy dữ liệu trả về từ intent cho việc take a picture
//            Bundle bundle = data.getExtras();
//            // Lấy ảnh từ bundle có thể dưới dạng bitmap hoặc uri nhưng sẽ lấy uri để tiện cho việc lưu xuống
//            Uri uri = data.getData();
//
//            List<Uri> uriList = new ArrayList<>();
//            uriList.add(Uri.withAppendedPath(Uri, ));
//
//            // Lưu ảnh vào MediaStore.Images.Media
//
//    }

    private void showStatisticDialog(){
        List<MediaItem> list =  MediaItemRepository.getInstance().getAllMediaItems().getValue();
        long folderSize = 0;
        int imageCnt = 0;
        int videoCnt = 0;

        for(int i = 0 ; i < list.size();i++){
            folderSize += list.get(i).getFileSize();
            if(list.get(i).getFileExtension().equals("video/mp4"))
                videoCnt++;
            else imageCnt++;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thống kê")
                .setMessage("Số ảnh và video: " + list.size()  +"\n"
                        + "Số ảnh: " + imageCnt + "\n"
                        + "Số video: " + videoCnt + "\n"
                        + "Kích thước: " + BytesToStringConverter.longToString(folderSize))
                .setPositiveButton("OK", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
package com.example.gallery.ui.main.fragment;


import static com.example.gallery.utils.Utils.addRandomNumberToFileName;
import static com.example.gallery.utils.Utils.getFileExtension;
import static com.example.gallery.utils.Utils.getFileNameFromUrl;
import static com.example.gallery.utils.Utils.isImageContentType;
import static com.example.gallery.utils.Utils.isValidUrl;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gallery.App;
import com.example.gallery.R;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;

import com.example.gallery.data.repositories.models.HelperFunction.RequestPermissionHelper;
import com.example.gallery.data.repositories.models.Repository.AlbumRepository;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.custom.AddImageFromDevice;
import com.example.gallery.ui.custom.AnimatedGIFWriter;
import com.example.gallery.ui.main.adapter.CreateStoryAdapter;
import com.example.gallery.ui.main.adapter.MainMediaItemAdapter;

import com.example.gallery.ui.main.doing.MainActivity;
import com.example.gallery.utils.BytesToStringConverter;
import com.example.gallery.ui.main.doing.DuplicationActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MediaItemFragment extends Fragment {

    public static final int REQUEST_TAKE_PHOTO = 256;
    public static final int REQUEST_SIMILAR_PHOTO = 123;
    private static final int MY_CAMERA_PERMISSION_CODE = 10001;

    private static final int REQUEST_IMAGE_PICK = 1;

    View mView;
    private RecyclerView recyclerView;
    private MainMediaItemAdapter mainMediaItemAdapter;
    private int mCurrentType = MediaItem.TYPE_GRID;
    private Menu mMenu; // use this to change icon of menu

    private Toolbar toolbar;

    // 3 Layout management
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    List<String> dateListString;
    HashMap<String, List<MediaItem>> mediaItemGroupByDate;
    List<MediaItem> filterData;
    List<MediaItem> mediaItemsLíst;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    private FloatingActionButton btnPickFiles;


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
        btnPickFiles = view.findViewById(R.id.floating_pick_from_device_button);

        // add event for button
        btnPickFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("openAddImageFromDeviceActivity");
                showOptionsDialog();

            }
        });

        // add event scroll for recyclerview
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @org.jetbrains.annotations.NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    btnPickFiles.show();
                }else{
                    btnPickFiles.hide();
                }
            }
        });



        // Xử lý các layoutmanager
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        linearLayoutManager = new LinearLayoutManager(getContext());

        // Default layout manager
        recyclerView.setLayoutManager(linearLayoutManager);

        // Adapter for recycler view
        mainMediaItemAdapter = new MainMediaItemAdapter();
        recyclerView.setAdapter(mainMediaItemAdapter);

        // Data from viewModel
        MediaItemRepository.getInstance().getAllPublicMediaItem(AppPreferencesHelper.getInstance().getCurrentUserId()).observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {

            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                if(mediaItems == null) {
                    return;
                }
                filterData = new ArrayList<>();
                for(MediaItem iterator : mediaItems)
                    if(!iterator.getAlbumName().equals("Bin"))
                        filterData.add(iterator);

                for(MediaItem mediaItem : filterData){
//                    System.out.println("MediaItemFragment 001: onViewCreated: getAllMediaItems: onChanged: in loop");
                    mediaItem.setTypeDisplay(mCurrentType);
                }

                mediaItemGroupByDate = setMediaItemGroupByDate(filterData);

                mainMediaItemAdapter.setData(filterData, mediaItemGroupByDate, dateListString); // trong adapter có hàm setData và có notifydatasetchanged
//                System.out.println("on observe : " + mediaItems.size() + " before set hash map");
                HashMap<String, List<MediaItem>> mediaItemGroupByDate = setMediaItemGroupByDate(filterData);

                mainMediaItemAdapter.setData(filterData, mediaItemGroupByDate, dateListString); // trong adapter có hàm setData và có notifydatasetchanged
            }
        });

        // Khởi tạo ActivityResultLauncher cho yêu cầu quyền
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
            // Xử lý khi quyền được cấp hoặc không được cấp
            if (isGranted.getOrDefault(Manifest.permission.CAMERA, false)) {
                // Quyền CAMERA được cấp, thực hiện các thao tác liên quan
                getPictureFromCamera();
            } else {
                // Quyền CAMERA không được cấp, hiển thị thông báo hoặc thực hiện các xử lý khác
                Toast.makeText(getContext(), "Quyền CAMERA không được cấp", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Hàm này sẽ nhận vào 1 list mediaItem và trả về 1 hashmap với key là ngày tháng năm, value là 1 list mediaItem
    private  HashMap<String, List<MediaItem>> setMediaItemGroupByDate(List<MediaItem> mediaItems) {
        HashMap<String, List<MediaItem>> result = new HashMap<>();

        for(int i = mediaItems.size() - 1; i >= 0; i--){
            MediaItem mediaItem = mediaItems.get(i);
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
            requestCameraPermissionAndTakePhoto();

        }else if(id == R.id.similarPhoto){
            Intent intent = new Intent(getContext(), DuplicationActivity.class);
            startActivityForResult(intent,REQUEST_SIMILAR_PHOTO);
        }
        else if(id == R.id.statistic){
            showStatisticDialog();
        }
        else if(id == R.id.createStoryItem){
            showCreateStoryDialog();
        }
        else if(id == R.id.app_information){
            showAppInfoDialog();
//            Toast.makeText(getContext(),"get app info",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCreateStoryDialog() {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.create_story_dialog);

        Window window = dialog.getWindow();

        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setAttributes(window.getAttributes());

        dialog.setCancelable(true);

        // Anh xa cac view
        RecyclerView recyclerView = dialog.findViewById(R.id.rcv_story_item);
        Button btnCreateStory = dialog.findViewById(R.id.btn_create_story);
//        Button btnCancelCreateStory = dialog.findViewById(R.id.btn_cancel_create_story);

        // Layout manager
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager1);

        // Adapter
        CreateStoryAdapter createStoryAdapter = new CreateStoryAdapter();
        recyclerView.setAdapter(createStoryAdapter);

        // Data
        MediaItemRepository.getInstance().getAllMediaItems().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                if(mediaItems == null) {
                    return;
                }
                List<MediaItem> data = new ArrayList<>();
                for(MediaItem iterator : mediaItems)
                    if(!iterator.getAlbumName().equals("Bin"))
                        data.add(iterator);
                createStoryAdapter.setData(data);
            }
        });

        // Event
//        btnCancelCreateStory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        btnCreateStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MediaItem> list = createStoryAdapter.getCheckedItems();
                if(list.size() > 0){
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/GIF_Story/");
                    if(!file.exists()){
                        file.mkdir();
//                        Album album = new Album();
//                        album.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
//                        album.setAlbumName("GIF_Story");
//                        album.setCoverPhotoPath(list.get(0).getPath());
//                        AlbumRepository.getInstance().insert(album);
                    }
                    String outputFilePath = file.getPath() + "/" + System.currentTimeMillis() + "story.gif";

                    try {
                        File fileDestination = new File(outputFilePath);
                        OutputStream outputStream = new FileOutputStream(fileDestination);

                        AnimatedGIFWriter animatedGIFWriter = new AnimatedGIFWriter(true);
                        animatedGIFWriter.prepareForWrite(outputStream, -1,-1);
                        for(MediaItem item : list){
                            Bitmap bitmap = BitmapFactory.decodeFile(item.getPath());
                            animatedGIFWriter.writeFrame(outputStream, bitmap, 1000);
                        }
                        AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), "GIF_Story", list.get(0).getPath());

                        animatedGIFWriter.finishWrite(outputStream);
                        MediaItem mediaItem = new MediaItem();
                        mediaItem.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                        mediaItem.setPath(outputFilePath);
                        mediaItem.setAlbumName("GIF_Story");
                        mediaItem.setCreationDate(new Date().getTime());
                        mediaItem.setFileSize(fileDestination.length());
                        mediaItem.setFileExtension("gif");
                        MediaItemRepository.getInstance().insert(mediaItem);

                        if(AlbumRepository.getInstance().isExistAlbum("GIF_Story") == false) {
                            Album alb = new Album();
                            alb.setName("GIF_Story");
                            alb.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                            alb.setCoverPhotoPath(list.get(0).getPath());
                            AlbumRepository.getInstance().insert(alb);
                        }
                        AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), "Edited", list.get(0).getPath());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getContext(), "Create story succeed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private String currentPhotoPath;

    private void requestCameraPermissionAndTakePhoto() {

        if (ContextCompat.checkSelfPermission(App.getInstance(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền CAMERA nếu chưa có
            requestPermissionLauncher.launch(new String[]{Manifest.permission.CAMERA});
        } else {
            // Quyền đã được cấp, thực hiện các thao tác liên quan
            getPictureFromCamera();
        }
    }


    protected void getPictureFromCamera() {


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

                System.out.println("MediaItemFragment : takeAPicture: uri: " + uri);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                System.out.println("MediaItemFragment : before startActivityForResult: ");

                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);



//                System.out.println("MediaItemFragment : after startActivityForResult: ");

            }
        }
    }






    // Hàm này tạo một file để lưu ảnh
    private File createImageFile() throws IOException {
        System.out.println("MediaItemFragment : createImageFile: ");
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = imageFile.getAbsolutePath();
        System.out.println("Current Photo Path: " + currentPhotoPath);
        return imageFile;
    }

    // Hàm này xử lý kết quả trả về từ intent chụp ảnh
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("MediaItemFragment : onActivityResult: ");

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {


            // Sử dụng currentPhotoPath để truy cập đường dẫn của ảnh
            if (currentPhotoPath != null) {
                // In đường dẫn ra Log (hoặc thực hiện các thao tác khác)
               System.out.println( "Đường dẫn ảnh đã chụp: " + currentPhotoPath);
                MediaItem item = new MediaItem();
                item.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                item.setPath(currentPhotoPath);
                item.setAlbumName("Camera");
                item.setCreationDate(new Date().getTime());

                // Update thumbnail for "All" Album
                AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), "Camera", currentPhotoPath);

                MediaItemRepository.getInstance().insert(item);
            }
            // Ảnh đã được chụp thành công
            Toast.makeText(getContext(), "Chụp ảnh thành công", Toast.LENGTH_SHORT).show();
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


    private void showStatisticDialog(){
        List<MediaItem> list =  MediaItemRepository.getInstance().getAllMediaItems().getValue();
        long folderSize = 0;
        int imageCnt = 0;
        int videoCnt = 0;

        for(int i = 0 ; i < list.size();i++){
            folderSize += list.get(i).getFileSize();
            if(list.get(i).getFileExtension().equals("mp4"))
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


    // Hàm để hiển thị dialog thêm ảnh
    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_img_dialog, null);
        builder.setView(dialogView);

        // Lấy các button từ dialog layout
        Button btnLoadAllImages = dialogView.findViewById(R.id.btnLoadAllImages);
        Button btnAddSomeImages = dialogView.findViewById(R.id.btnAddSomeImages);
        Button btnLoadImagesFromUrl = dialogView.findViewById(R.id.btnLoadImagesFromUrl);

        // Thiết lập sự kiện click cho các button

        // Tạo dialog hiện tại
        final AlertDialog currentDialog = builder.create();
        btnLoadAllImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng chọn tùy chọn 1
                // ...
                currentDialog.dismiss();
                RequestPermissionHelper.checkAndRequestPermission(getActivity(), 225);

            }
        });

        btnAddSomeImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng chọn tùy chọn 2
                // ...
                currentDialog.dismiss();

                Intent intent = new Intent(getActivity(), AddImageFromDevice.class);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);


            }
        });

        btnLoadImagesFromUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng chọn tùy chọn 3
                // ...
                currentDialog.dismiss();
                showCustomDialog();


            }
        });

        // Tạo và hiển thị AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Hàm để hiển thị dialog
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.download_img_url, null);
        builder.setView(dialogView);

        // Lấy các view từ dialog layout
        TextView tvStatus = dialogView.findViewById(R.id.tvStatus);
        EditText etUrl = dialogView.findViewById(R.id.etUrl);
        Button btnDownload = dialogView.findViewById(R.id.btnDownload);
        Button btnClear = dialogView.findViewById(R.id.btnClear);
        ImageView imageView = dialogView.findViewById(R.id.imgVDownloaded);

        // Thiết lập sự kiện click cho nút "Download"
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng nhấn nút "Download"
                addImageFromLink(tvStatus, etUrl, imageView, btnDownload, btnClear);

            }
        });

        // Thiết lập sự kiện click cho nút "Clear"
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng nhấn nút "Clear"
                etUrl.setText("");
                tvStatus.setText("Download Status:");
                imageView.setVisibility(View.GONE);
            }
        });

        // Tạo và hiển thị AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // tải ảnh qua url
    private MutableLiveData<Boolean> isGif = new MutableLiveData<>(false);
    public  void addImageFromLink(TextView tvStatus, EditText etUrl, ImageView imageView, Button btnDownload, Button btnClear) {

        tvStatus.setText("Status: đang tải ảnh ...");
        tvStatus.setTextColor(Color.parseColor("#808080"));

        String imageUrl = etUrl.getText().toString();

        //  System.out.println("Image Url: " + imageUrl);
        if (!isValidUrl(imageUrl)) {
            tvStatus.setText("Error: Invalid URL");
            tvStatus.setTextColor(Color.parseColor("#FF0000"));

            return;
        }


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(imageUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle OkHttp failure...
                //  System.out.println("Have something wrong ");
                tvStatus.setText("Downloaded Status: failed");
                tvStatus.setTextColor(Color.parseColor("#FF0000"));


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Get the file extension from the Content-Type header
                    String contentType = response.header("Content-Type");
                    String fileExtension = getFileExtension(contentType);

                    if (fileExtension.equals(".gif")) {
                        isGif.postValue(true);
                    }

                    if (!isImageContentType(contentType)) {
                        tvStatus.setText("Error: Invalid image URL");
                        tvStatus.setTextColor(Color.parseColor("#FF0000"));

                        return;
                    }

                    //  System.out.println("Content Type: " + contentType);

                    // Get the file name from the URL
                    String fileName = addRandomNumberToFileName(getFileNameFromUrl(imageUrl)) + fileExtension;
                    //  System.out.println("File name from url: " + fileName);

                    // Save the image to a file
                    File appDirectory = new File(App.getInstance().getExternalFilesDir(null), "Images/FromUrls");
                    if (!appDirectory.exists()) {
                        appDirectory.mkdirs();
                    }

                    File imageFile = new File(appDirectory, fileName);

                    try (InputStream inputStream = response.body().byteStream();
                         OutputStream outputStream = new FileOutputStream(imageFile)) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Get image path
                    String imgPath = imageFile.getAbsolutePath();
                    //  System.out.println("Image path: " + imgPath);


                    // Save media item info to database
                    MediaItem item = new MediaItem();
                    item.setPath(imgPath);
                    item.setOrigin(imageUrl);
                    item.setCreationDate(Date.parse(new Date().toString()));
//                    item.setId(new Random().nextInt(10000));
                    item.setUserID(AppPreferencesHelper.getInstance().getCurrentUserId());
                    item.setAlbumName("Tải xuống");

                    // check if album not exist in table albums

                    MediaItemViewModel.getInstance().insert(item);

                    // Update thumbnail for "All" Album
                    AlbumRepository.getInstance().updateAlbumCoverPhotoPath(AppPreferencesHelper.getInstance().getCurrentUserId(), "Tải xuống", imgPath);

//                    Toast.makeText(App.getInstance(), "Insert successfully", Toast.LENGTH_SHORT).show();
                    //  System.out.println("Insert media item success from profile view model");



                    // Get the number of rows in the database
//                    //  System.out.println("Number of media items after download image: " + MediaItemViewModel.getInstance().getNumberOfMediaItems().getValue());

                    tvStatus.setText("Status: Tải ảnh " + fileExtension +" thành công !");
                    tvStatus.setTextColor(Color.parseColor("#008000"));
                }
            }
        });

        isGif.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Glide.with(App.getInstance())
                            .asGif()
                            .load(imageUrl)
                            .into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                }else{
                    Glide.with(App.getInstance())
                            .asBitmap()
                            .load(imageUrl)
                            .into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void showAppInfoDialog(){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.app_info_screen);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(window.getAttributes());
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);
        AppCompatImageButton btnReturn = dialog.findViewById(R.id.btnBack);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
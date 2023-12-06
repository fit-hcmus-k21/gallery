package com.example.gallery.ui.main.doing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gallery.R;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.main.adapter.ViewPagerSingleMediaAdapter;
import com.example.gallery.utils.GetInDexOfHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.Inflater;

public class SingleMediaActivity extends AppCompatActivity  {

    public static int REQUEST_MAKE_FAVORITE_ITEM = 100;

    ViewPager2 viewPager2;
    ViewPagerSingleMediaAdapter viewPagerSingleMediaAdapter;

    ImageView favoriteImageView, editImageView;
    ImageView shareImageView;
    List<MediaItem> mediaItemsList; // Biến thành viên để lưu trữ danh sách các MediaItem

    LinearLayout customappbar;
    Menu mMenu;
    MutableLiveData<MediaItem> mediaItemLiveData = new MutableLiveData<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("SingleMediaActivity | OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_photo_screen);


        // Ánh xạ các view
        viewPager2 = findViewById(R.id.single_photo_viewpager2);
        favoriteImageView = findViewById(R.id.media_favorite_icon);
        shareImageView = findViewById(R.id.media_share_icon);
        customappbar = findViewById(R.id.custom_bottom_appbar);
        editImageView = findViewById(R.id.media_edit_icon);




        // Gán Adapter
        viewPagerSingleMediaAdapter = new ViewPagerSingleMediaAdapter();
        viewPager2.setAdapter(viewPagerSingleMediaAdapter);


        // Thao tác với actionbar và tạo nut back ở đây
        Toolbar toolbar = findViewById(R.id.toolbar_single_media_item);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        MediaItem mediaItemIntent = (MediaItem) bundle.getSerializable("mediaItem");


//        editImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentEdit = new Intent(SingleMediaActivity.this,)
//            }
//        });

        // Đây là biến dạng boolen để đảm bảo rằng việc di chuyển đến item đang được chọn chỉ được thực hiện 1 lần
        // Nếu không có biến này thì khi chúng ta thực hiện thao tác thay đổi trạng thái Favorite của item đang được chọn
        // thì nó sẽ tự động di chuyển đến item mà ta đã truyền vào trước đó gây mất đồng bộ
        final boolean[] isMoveToCurrentItem = {false};
        MediaItemRepository.getInstance().getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                System.out.println("SingleMediaActivity | OnCreate | onChanged all media items");

                mediaItemsList = mediaItems;

                viewPagerSingleMediaAdapter.setData(mediaItems);
                int index = GetInDexOfHelper.getIndexOf(mediaItems, mediaItemIntent);

                // Lấy dữ liệu của MediaItem đang được chọn tra về LiveData
                mediaItemLiveData.setValue(mediaItems.get(index));
                System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 101");
                System.out.println("SingleMediaActivity | mediaItemLiveData = " + mediaItemLiveData.getValue());

                if(!isMoveToCurrentItem[0]){
                    Log.e("MyTag", "onChanged: " + index)   ;
                    System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 105");

                    viewPager2.setCurrentItem(index, false);
                    System.out.println("SingleMediaActivity | OnCreate | onChanged all media items | 108");
                    isMoveToCurrentItem[0] = true;

                }

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("Single media activity | on page scrolled 137 | position : " + position);
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("Single media activity | on page selected 143 | position : " + position);
                mediaItemLiveData.setValue(mediaItemsList.get(position));

                MediaItem selectedMediaItem = mediaItemsList.get(position);
                Log.e("MyTag", "onPageSelected: " + position);

                if(selectedMediaItem.isFavorite()){
                    favoriteImageView.setImageResource(R.drawable.heart_svgrepo_com_color);
                }
                else{
                    favoriteImageView.setImageResource(R.drawable.baseline_heart_svgrepo_com);
                }

                // hiện tại có nhiều ảnh chưa set file extension nên sẽ update và remove comment sau *********
//                if(mediaItemsList.get(position).getFileExtension() != null && mediaItemsList.get(position).getFileExtension().equals("image/jpeg")){
//                    editImageView.setEnabled(true);
//                    editImageView.setClickable(true);
//                }
//                else{
//                    editImageView.setEnabled(false);
//                    editImageView.setClickable(false);
//                }


                editImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent editIntent = new Intent(SingleMediaActivity.this,EditActivity.class);
                        Bundle data = new Bundle();
                        data.putSerializable("meidaItem",mediaItemsList.get(position));
                        editIntent.putExtras(data);
                        startActivityForResult(editIntent,111111);

                    }
                });

                shareImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareImageToInternet(selectedMediaItem);
                    }
                });
                favoriteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("SingleMediaActivity | OnCreate | onClick | update favorite");

                        // Đây chỉ mới là việc thay đổi dữ liêu của Favorite trong database tự định nghĩa
                        // Chúng ta phải tiêến hành cập nhật trong MediaStore.Images.Media/MediaStores.Video.Media trường Favorite nữa
                        selectedMediaItem.setFavorite(!selectedMediaItem.isFavorite());
                        MediaItemRepository.getInstance().updateFavorite(selectedMediaItem.getId(), selectedMediaItem.isFavorite());

                        if(selectedMediaItem.isFavorite()){
                            favoriteImageView.setImageResource(R.drawable.heart_svgrepo_com_color);
                        }
                        else{
                            favoriteImageView.setImageResource(R.drawable.baseline_heart_svgrepo_com);
                        }

                        // Cập nhật thông tin trong MediaStore.Images.Media/MediaStores.Video.Media bằng cách sử dụng Pending Intent với createFavoriteRequest
                        List<Uri> uriList = new ArrayList<>();
                        uriList.add(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(selectedMediaItem.getId())));

                        PendingIntent pendingIntent = null;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            pendingIntent = MediaStore.createFavoriteRequest(getContentResolver(), uriList, selectedMediaItem.isFavorite());

                            if(pendingIntent != null){
                                try {
                                    startIntentSenderForResult(pendingIntent.getIntentSender(), REQUEST_MAKE_FAVORITE_ITEM, null, 0, 0, 0, null);
                                } catch (IntentSender.SendIntentException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }

                    }
                });



                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("SingleMediaActivity | OnCreate | onPageScrollStateChanged");
                super.onPageScrollStateChanged(state);
            }
        });

        System.out.println("SingleMediaActivity | OnCreate | End");

    }

    // Override lại hàm khởi tạo 1 menu, và gán menu đó cho biến mMenu. Có thể thao tác trong file xml cho menu
    // Nhưng không hiểu vì sao lại lỗi, không hiêển thị được icon nên dùng cách này
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("SingleMediaActivity | onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.top_appbar_media_item_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    // Bắt các sự kiện khi các nút trên thanh Action bar được chọn


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println("SingleMediaActivity | onOptionsItemSelected");
        int id = item.getItemId();

        if(id == android.R.id.home ){
            onBackPressed();
        }
        else if(id == R.id.media_information_item){
            mediaItemLiveData.observe(this, new Observer<MediaItem>() {
                @Override
                public void onChanged(MediaItem mediaItem) {
                    System.out.println("Single Media Activity | On changed 239 " + mediaItem);
                    DisPlayInforMationAlerDialog(mediaItem);
                }
            });
        }
        else if(id == R.id.media_change_name_item){
            //TODO handle media_change_name_item
        }
        else if(id == R.id.media_copy_to_item){
            //TODO handle media_copy_to_item
        }
        else if(id == R.id.media_move_to_item){
            //TODO handle media_move_to_item
        }
        else if(id == R.id.media_set_wallpaper_item){
            //TODO handle media_set_wallpaper_item
        }
        else if(id == R.id.media_convert_text_item){
            textRecognition(mediaItemLiveData.getValue());
//            mediaItemLiveData.observe(this, new Observer<MediaItem>() {
//                @Override
//                public void onChanged(MediaItem mediaItem) {
////                    showOCRResultDialog(mediaItem.getPath());
//                    System.out.println("Single Media Activity | On changed 260 " + mediaItem );
//                    textRecognition(mediaItem);
//
//                }
//            });

        }

        return super.onOptionsItemSelected(item);
    }

    private void DisPlayInforMationAlerDialog(MediaItem mediaItem) {
        System.out.println("SingleMediaActivity | DisPlayInforMationAlerDialog");

        // Định dạng bạn muốn chuyển đổi sang (đối với ngày/tháng/năm)
        String desiredFormat = "dd/MM/yyyy";

// Giá trị kiểu long của creationDate
        long creationDateValue = mediaItem.getCreationDate();

// Chuyển đổi giá trị long thành đối tượng Date
        Date creationDate = new Date(creationDateValue);

// Chuyển đổi đối tượng Date thành chuỗi với định dạng mới
        String formattedDate = new SimpleDateFormat(desiredFormat).format(creationDate);




        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chi tiết ảnh")
                .setMessage("* Tên: " + mediaItem.getName() + "\n" +
                        "* Đường dẫn: " + mediaItem.getPath() + "\n\n" +
                        "* Kích thước: " + mediaItem.getFileSize() + "\n" +
                        "* Ngày tạo: " + formattedDate + "\n" +
                        "* Ngày sửa: " + mediaItem.getLastModified() + "\n" +
                        "* Định dạng: " + mediaItem.getFileExtension() + "\n" +
                        "* Độ phân giải: " + mediaItem.getWidth() + "x" + mediaItem.getHeight() + "\n" +
                        "* Độ yêu thích: " + mediaItem.isFavorite() + "\n" +
                        "* Album: " +mediaItem.getAlbumName() + "\n" +
                        "* Location: " + mediaItem.getLocation()+ "\n" +
                        "* Ghi chú: " + mediaItem.getDescription()+ "\n" +
                        "* TAG: " + mediaItem.getTag()+ "\n" +
                        "* Extension: " + mediaItem.getFileExtension() + "\n" )
                .setPositiveButton("OK", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent receive ) {
        super.onActivityResult(requestCode, resultCode, receive);
        if(requestCode == 111111 && resultCode == RESULT_OK){
            Bundle data = receive.getExtras();
            String result = data.getString("result");
            String path ;
            Toast.makeText(SingleMediaActivity.this,result,Toast.LENGTH_SHORT).show();
            if(result.equals("SAVE")){
                path = receive.getStringExtra("afterEdit");         // đường dẫn chứa hình ảnh sau khi đã edit
                MediaItem mediaItem = (MediaItem) data.getSerializable("received");
                //lưu hình ảnh vào database
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path,options);

                int width = options.outWidth;
                int height = options.outHeight;
                String type = options.outMimeType;
                Toast.makeText(SingleMediaActivity.this,type,Toast.LENGTH_SHORT).show();
                long size = new File(path).length();
                long date = 29382;
                MediaItemRepository.getInstance().insert(new MediaItem(1991, AppPreferencesHelper.getInstance().getCurrentUserId(), mediaItem.getName() + "edit","editTag1","editImage",path,width,height,size,type,mediaItem.getCreationDate(),mediaItem.getLocation(),mediaItem.getAlbumName(),mediaItem.getUrl(),false,mediaItem.getParentPath(),mediaItem.getLastModified(),mediaItem.getDeletedTs()));
            }
        }
    }
    private void textRecognition(MediaItem mediaItem){
        System.out.println("SingleMediaActivity | textRecognition | mediaitem: " + mediaItem);
        FirebaseVisionImage image;
        try{
//            image = FirebaseVisionImage.fromFilePath(this,Uri.parse(mediaItem.getPath()));
            image = FirebaseVisionImage.fromFilePath(this,Uri.fromFile(new File(mediaItem.getPath())));
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> textTask =
                    detector.processImage(image)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    String result = firebaseVisionText.getText();
                                    System.out.println("SingleMediaActivity | textRecognition | onSuccess | result = " + result);
                                    showOCRResultDialog(result);
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SingleMediaActivity.this,
                                                    "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void showOCRResultDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả")
                .setMessage(text)
                .setPositiveButton("OK", null)
                .setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("text", text);
                        clipboardManager.setPrimaryClip(clip);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void shareImageToInternet(MediaItem mediaItem){
        System.out.println("Share image : 337 | mediaitem: " + mediaItem + " | path: " + mediaItem.getPath());

        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(mediaItem.getPath()));

//        Uri uri = Uri.parse(mediaItem.getPath());
        System.out.print("Uri: "  + uri);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        System.out.println("Share image : 342");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share this image with..."));
        System.out.println("Share image : 347");

    }
}

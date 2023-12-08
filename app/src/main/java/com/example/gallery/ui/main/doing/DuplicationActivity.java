package com.example.gallery.ui.main.doing;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gallery.R;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.data.repositories.models.ViewModel.MediaItemViewModel;
import com.example.gallery.ui.main.adapter.DuplicateParentAdapter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DuplicationActivity extends AppCompatActivity implements View.OnClickListener {
    public static TextView overalInfo;
    public static Button  btnDelete;
    Button btnReturn;
    RecyclerView duplicateRecyclerview;
    MediaItemViewModel mediaItemViewModel;
    DuplicateParentAdapter duplicateParentAdapter;
    public static List<String> dateListString = new ArrayList<>();
    public static HashMap<String,List<MediaItem>> mediaItemGroupToSort = new HashMap<>() ;
    public static HashMap<String,List<Boolean>> checkstate = new HashMap<>();
    public static List<MediaItem> firstData = new ArrayList<>();
    public static List<List<MediaItem>> newList = new ArrayList<>();

    public static int countDel = 0;
    public static int sizeDel = 0;
    public int count = 0;
    public int size = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle saveInstanceState){
        //  System.out.println("DuplicationActivity 001: onCreate");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.duplicate_screen);

        overalInfo = findViewById(R.id.overalInfo);
        btnDelete = findViewById(R.id.btnDeleteSimilar);
        btnReturn = findViewById(R.id.btnReturn);
        duplicateRecyclerview = findViewById(R.id.mainItem);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);


        btnDelete.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        MediaItemRepository.getInstance().getAllMediaItems().observe(this, new Observer<List<MediaItem>>() {
            @Override
            public void onChanged(List<MediaItem> mediaItems) {
                mediaItemGroupToSort = new HashMap<>();
                dateListString = new ArrayList<>();
                firstData = new ArrayList<>();
                checkstate = new HashMap<>();
                newList = new ArrayList<>();
                HashSet<String> date = new HashSet<>();

                // get Image in album Camera
                for(MediaItem iterator : mediaItems){
                    if(!iterator.getFileExtension().equals("video/mp4") && !iterator.getAlbumName().equals("Edited") && !iterator.getAlbumName().equals("Bin")) {
                        firstData.add(iterator);
                        date.add(new SimpleDateFormat("yyyy/MM/dd").format(iterator.getCreationDate()));
                    }
                }

                //get date list and sort
                dateListString = new ArrayList<>(date);
                Collections.sort(dateListString,Collections.reverseOrder());
                // lấy danh sách HashMap<String, List<MediaItem>>
                for(String iterator : dateListString){
                    mediaItemGroupToSort.put(iterator, new ArrayList<>());
                    checkstate.put(iterator,new ArrayList<>());
                }

                for(MediaItem iterator : firstData){
                    mediaItemGroupToSort.get(new SimpleDateFormat("yyyy/MM/dd").format(iterator.getCreationDate())).add(iterator);
                }


                //get similar photos
                for(int i = 0 ; i < mediaItemGroupToSort.size(); ++i){
                    //  System.out.println("Bitmap 112 | Duplication | " + i);
                    newList.add(find(mediaItemGroupToSort.get(dateListString.get(i))));
                    //  System.out.println("Bitmap 114 | Duplication | " + i);

                }


                firstData = new ArrayList<>();
                //after get all similar photo
                int run = 0;
                for(int i = 0; i < newList.size(); ++i,++run){
                    //  System.out.println(run +"---" + i);

                    if(newList.get(i).isEmpty()){
                        mediaItemGroupToSort.remove(dateListString.get(run));
                        checkstate.remove(dateListString.get(run));
                        dateListString.remove(run);
                        --run;

                    }
                    else{
                        List<Boolean> check = new ArrayList<>();
                        for(MediaItem j : newList.get(i)) {
                            count++;
                            size += j.getFileSize();
                            firstData.add(j);
                            check.add(false);
                        }
                        mediaItemGroupToSort.replace(dateListString.get(run),newList.get(i));
                        checkstate.replace(dateListString.get(run),check);
                    }
                }

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String roundedValue = decimalFormat.format((double)size/(1024*1024));
                overalInfo.setText(count + " similar photo(s) in total, using " + roundedValue + "MB");
                duplicateParentAdapter.setData(firstData,mediaItemGroupToSort,dateListString,checkstate);
                duplicateParentAdapter.notifyDataSetChanged();
            }
        });




        duplicateRecyclerview.setLayoutManager(layoutManager);
        duplicateParentAdapter = new DuplicateParentAdapter(this);
        duplicateRecyclerview.setAdapter(duplicateParentAdapter);

        if(countDel > 0) {
            btnDelete.setEnabled(true);
            btnDelete.setClickable(true);
            btnDelete.setBackgroundColor(getResources().getColor(R.color.selected));
        }
        else{
            btnDelete.setClickable(false);
            btnDelete.setEnabled(false);
            btnDelete.setBackgroundColor(getResources().getColor(R.color.grey));
        }

    }




    // ----------------------------------------
    public List<MediaItem> find(List<MediaItem> photos){

        List<MediaItem> temp = new ArrayList<>(photos);
        List<Long> fingerValues = CalculateFingerValue(photos);
        List<MediaItem> output = new ArrayList<>();                 // lưa danh sách ảnh trùng sau khi check
        for(int i = 0; i < temp.size()-1; ++i){
            boolean check = false;
            int j = i + 1;
            while(j < temp.size()){
                int dist = 0;
                try {
                    dist = hamDist(fingerValues.get(i),fingerValues.get(j));
                }catch (Exception e){
                    System.out.println("Bitmap 203 | Duplication | " + e);

                }
                if(dist < 10){
                    if(check == false){
                        output.add(temp.get(i));
                        check = true;
                    }
                    output.add(temp.get(j));
                    System.out.println("Bitmap 196 | Duplication | " + temp.get(j));
                    temp.remove(j);
                    System.out.println("Bitmap 196 | Duplication | " + temp.size());
                    fingerValues.remove(j);
                    System.out.println("Bitmap 197 | Duplication | " + temp.size());
                    --j;
                }
                ++j;
            }
        }
        return output;
//        --------------------------------------------

    }
    public List<Long> CalculateFingerValue(List<MediaItem> photos){

        List<Long> finger = new ArrayList<>();
        float scaleW, scaleH;
        //  System.out.println("Bitmap 203 | Duplication | " + photos.size());
        for(int i = 0 ; i < photos.size(); ++i){
            MediaItem photo = photos.get(i);
//            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(DuplicationActivity.this.getContentResolver(),photo.getId(),MediaStore.Images.Thumbnails.MICRO_KIND,null);

            // Đường dẫn đến tập tin hình ảnh
            String imagePath = photo.getPath();

            // Sử dụng BitmapFactory để đọc ảnh từ đường dẫn và tạo Bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            // Kiểm tra xem Bitmap có được tạo thành công không
            if (bitmap != null) {
                //  System.out.println("Bitmap 204 | Duplication | " + bitmap);

                scaleW = 8.0f/bitmap.getWidth();
                scaleH = 8.0f/bitmap.getHeight();
                Matrix matrix = new Matrix();
                matrix.postScale(scaleW,scaleH);
                Bitmap scaleBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
                finger.add(getFingerPrint(scaleBitmap));
                bitmap.recycle();;
                scaleBitmap.recycle();
            } else {
                // Xử lý trường hợp không thể tạo Bitmap từ đường dẫn
                Log.e("Error", "Unable to create Bitmap from the specified path");
                //  System.out.println("Bitmap 219 | Duplication | " + bitmap);
            }




        }
        return finger;
    }

    public static long getFingerPrint(Bitmap bitmap){
        double[][] grayPixels = getGrayPixels(bitmap);
        double avgGray = getAvgGray(grayPixels);
        return getFingerPrint(grayPixels,avgGray);
    }

    public static double[][] getGrayPixels(Bitmap bitmap){
        int width = 8;
        int height = 8;
        double[][] pixels = new double[height][width];
        for(int i = 0 ; i < width; ++i)
            for(int j = 0; j < height ; ++j)
                pixels[i][j] = getGrayValue(bitmap.getPixel(i,j));
        return pixels;
    }
    public static double getAvgGray(double[][] grayPixels){
        int width = grayPixels[0].length;
        int height = grayPixels.length;
        int count = 0;
        for(int i = 0;  i < width; ++i)
            for(int j = 0; j < height; ++j)
                count += grayPixels[i][j];

        return (double) count/(width*height);
    }

    public static long getFingerPrint(double[][] grayPixels, double avgGray){
        int width = grayPixels[0].length;
        int height = grayPixels.length;
        Byte[] gray = new Byte[width * height];
        for(int i = 0; i < width; ++i)
            for(int j = 0; j < height; ++j)
                if(grayPixels[i][j] >= avgGray)
                    gray[i * height + j]  = 1;
                else
                    gray[i * height + j] = 0;
        long printfinger1 = 0;
        long printfinger2 = 0;
        for(int i = 0; i < 8*8; ++i){
            if(i < 32 )
                printfinger1 += (gray[63-i] << i);
            else
                printfinger2 += (gray[63-i] << (i-31));
        }

        return (printfinger2 << 32) + printfinger1;
    }

    public static double getGrayValue(int pixel){
        int red = Color.red(pixel);
        int blue = Color.blue(pixel);
        int green = Color.green(pixel);
        return 0.3*red + 0.59*green + 0.11*blue;

    }
    private static int hamDist(long finger1, long finger2) {
        int dist = 0;
        long result = finger1 ^ finger2;
        while (result != 0) {
            ++dist;
            result &= result - 1;
        }
        return dist;
    }

    @Override
    public void onPause(){
        super.onPause();
        mediaItemGroupToSort.clear();
        dateListString.clear();
        checkstate.clear();
        firstData.clear();
        dateListString = null;
        mediaItemGroupToSort= null;
        checkstate = null;
        firstData = null;
        btnDelete.setEnabled(false);
        btnDelete.setClickable(false);
        count = 0;
        countDel = 0;
        size = 0;
        sizeDel = 0;
        duplicateParentAdapter.notifyDataSetChanged();
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnReturn){
            btnDelete.setEnabled(false);
            btnDelete.setClickable(false);
            count = 0;
            countDel = 0;
            size = 0;
            sizeDel = 0;
            duplicateParentAdapter.notifyDataSetChanged();
            finish();
        }
        if(v.getId() == R.id.btnDeleteSimilar){
            int tempCount = 0;
            for(String iterator : dateListString){
                for(int j = 0; j < checkstate.get(iterator).size(); ++j){
                    if(checkstate.get(iterator).get(j) == true){
                        //delete
                        MediaItem deleteItem = mediaItemGroupToSort.get(iterator).get(j);
                        mediaItemGroupToSort.get(iterator).remove(j);
                        checkstate.get(iterator).remove(j);
                        //trường hợp sau khi xóa nếu chỉ còn <= 1 ảnh thì xóa luôn item đó trong checkstate, mediaItemGroupToSort
                        //remove trong firstdata
                        int pos = firstData.lastIndexOf(deleteItem);
                        firstData.remove(pos);
                        //xóa trong database(đưa vào thùng rác) ****
                        deleteItem.setAlbumName("Bin");
                        //set deletetime  ****
                        Calendar calendar = Calendar.getInstance();
                        long currentDate = calendar.getTimeInMillis();
                        deleteItem.setDeletedTs(currentDate);
                        --j;
                    }
                }
            }
            count -= countDel;
            size -= sizeDel;
            Toast.makeText(DuplicationActivity.this,String.valueOf(tempCount),Toast.LENGTH_SHORT).show();
            duplicateParentAdapter.notifyDataSetChanged();

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String roundedValue = decimalFormat.format((double)size/(1024*1024));
            overalInfo.setText(count + " similar photo(s) in total, using " + roundedValue + "MB");
            countDel = 0;
            sizeDel = 0;
            btnDelete.setClickable(false);
            btnDelete.setEnabled(false);
            btnDelete.setText("Delete");
            btnDelete.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }
}



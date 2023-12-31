//package com.example.gallery.ui.gridview;
//
//import android.content.Intent;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.lifecycle.ViewModelProviders;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.StaggeredGridLayoutManager;
//
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.example.gallery.BR;
//import com.example.gallery.R;
//import com.example.gallery.data.models.db.MediaItem;
//import com.example.gallery.databinding.FragmentMediaItemBinding;
//import com.example.gallery.ui.base.BaseFragment;
//
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class MediaItemFragment extends BaseFragment<FragmentMediaItemBinding, MediaItemViewModel> implements MediaItemNavigator {
//
////    ----------------------------
//    FragmentMediaItemBinding mFragmentMediaItemBinding;
//    @Override
//    public int getBindingVariable() {
//        return BR.viewModel;
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.profile;
//    }
//
//    public FragmentMediaItemBinding getmFragmentMediaItemBinding() {
//        return mFragmentMediaItemBinding;
//    }
//
//    public MediaItemViewModel getmProfileViewModel() {
//        return mViewModel;
//    }
//
//
////    ----------------------------
//
//
//    public static final int REQUEST_TAKE_PHOTO = 256;
//
//    private MainMediaItemAdapter mainMediaItemAdapter;
//    private int mCurrentType = MediaItem.TYPE_GRID;
//    private Menu mMenu; // use this to change icon of menu
//
//
//    // 3 Layout management
//    GridLayoutManager gridLayoutManager;
//    LinearLayoutManager linearLayoutManager;
//    StaggeredGridLayoutManager staggeredGridLayoutManager;
//    List<String> dateListString;
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
////        ------------------
//
//        super.onCreateView(inflater, container, savedInstanceState);
//        mFragmentMediaItemBinding = getViewDataBinding();
//        //  System.out.println("Fragment Media Item  108: " + mFragmentMediaItemBinding);
//
//
//
//        // Inside ProfileActivity
//        mViewModel = new ViewModelProvider(this).get(MediaItemViewModel.class);
//
//        if (mViewModel != null) {
//            //  System.out.println("Fragment Media Item 115: " +  "mViewModel is not null: " + mViewModel);
//            mViewModel.setNavigator(this);
//
//        } else {
//            // print to log
//            //  System.out.println("Fragment Media Item:" + " mViewModel is null");
//
//        }
//
//        //  System.out.println("Fragment Media Item: " + mFragmentMediaItemBinding);
//
//        mFragmentMediaItemBinding.setViewModel(mViewModel);
//
//        setHasOptionsMenu(true); // Cho phép fragment có thể tạo menu
//
//
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(mFragmentMediaItemBinding.toolbarMediaItem);
//        activity.getSupportActionBar().setTitle("Gallery - Media");
//
//
//
//
//        // Inflate the layout for this fragment
//        return mFragmentMediaItemBinding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//
//        super.onViewCreated(view, savedInstanceState);
//        //  System.out.println("On created view : " + mFragmentMediaItemBinding);
//
//        //  System.out.println("mViewModel: " + mViewModel);
//
//
//
////        ------------------
//        dateListString = new ArrayList<>();
//
//
//
//        // Xử lý các layoutmanager
//        gridLayoutManager = new GridLayoutManager(getContext(), 3);
//        linearLayoutManager = new LinearLayoutManager(getContext());
//
//        // Default layout manager
//        mFragmentMediaItemBinding.rcvMediaItem.setLayoutManager(linearLayoutManager);
//
//        // Adapter for recycler view
//        mainMediaItemAdapter = new MainMediaItemAdapter();
//        mFragmentMediaItemBinding.rcvMediaItem.setAdapter(mainMediaItemAdapter);
////        mainMediaItemAdapter.mediaItemAdapter.setOnItemClickListener(new MediaItemAdapter.OnItemClickListener() {
////            @Override
////            public void onItemClick(MediaItem mediaItem) {
////                Log.e("Mytag", "onItemClick: " + mediaItem.getPath());
//
////                Intent intent = new Intent(holder.imageView.getContext(), SingleMediaActivity.class);
////
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("listmediaitem", (ArrayList<MediaItem>)mediaItemList);
////                bundle.putSerializable("mediaitem", mediaItem);
////                intent.putExtras(bundle);
////
////                holder.imageView.getContext().startActivity(intent);
////            }
////        });
//
//
//        // Data from viewModel
//        mViewModel.getAllMediaItems().observe(getViewLifecycleOwner(), new Observer<List<MediaItem>>() {
//
//            @Override
//            public void onChanged(List<MediaItem> mediaItems) {
//                if(mediaItems == null) {
//                    return;
//                }
//
//                for(MediaItem mediaItem : mediaItems){
//                    mediaItem.setTypeDisplay(mCurrentType);
//                }
//
//                HashMap<String, List<MediaItem>> mediaItemGroupByDate = setMediaItemGroupByDate(mediaItems);
//
//                mainMediaItemAdapter.setData(mediaItems, mediaItemGroupByDate, dateListString); // trong adapter có hàm setData và có notifydatasetchanged
//
//            }
//        });
//
//    }
//
//    // Hàm này sẽ nhận vào 1 list mediaItem và trả về 1 hashmap với key là ngày tháng năm, value là 1 list mediaItem
//    private  HashMap<String, List<MediaItem>> setMediaItemGroupByDate(List<MediaItem> mediaItems) {
//        HashMap<String, List<MediaItem>> result = new HashMap<>();
//
//        for(MediaItem mediaItem : mediaItems){
//            String date = new SimpleDateFormat("dd/MM/yyyy").format(mediaItem.getCreationDate());
//            if(result.containsKey(date)){
//                result.get(date).add(mediaItem);
//            }else{
//                // Theem vao dateListString
//                dateListString.add(date);
//
//                List<MediaItem> mediaItemList = new ArrayList<>();
//                mediaItemList.add(mediaItem);
//                result.put(date, mediaItemList);
//            }
//        }
//
//        return result;
//    }
//
////     Khởi tạo 1 menu onCreateOptionsMenu
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.topbar_media_item_menu, menu);
//        mMenu = menu;
//    }
//
//    // Bắt sự kiện chọn item trên menu
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if(id == R.id.modeDisplayView){
//            Toast.makeText(getContext(), "Change type display", Toast.LENGTH_SHORT).show();
//            onClickChangeTypeDisplay();
//        }
//        else if(id == R.id.camera_item){
//            takeAPickture();
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void takeAPickture() {
//        // Gọi intent để chụp ảnh
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Kiểm tra có đủ điều kiện để chụp ảnh hay không về mặt phần cứng
//        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
//            // Gọi startActivityForResult để nhận kết quả trả về
//            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//        }
//
//    }
//
//    // Hàm chuyển đôi type hiển thị cho các data, mà không cần phải chờ livedata, lấy trực tiếp dữ liệu từ recyclerview
//    public void setTypeDisplayRecyclerView(int typeDisplay){
//       mCurrentType = typeDisplay; // Cập nhật lại biến type của hiện tại
//
//       if(mainMediaItemAdapter != null){
//           HashMap<String, List<MediaItem>> listHashMapMediaItem= mainMediaItemAdapter.getData(); // Dữ liệu của recyclerview
//
//           for(String date : listHashMapMediaItem.keySet()){
//               List<MediaItem> mediaItemList = listHashMapMediaItem.get(date);
//               for(MediaItem mediaItem : mediaItemList){
//                   mediaItem.setTypeDisplay(mCurrentType);
//               }
//           }
//           mainMediaItemAdapter.notifyDataSetChanged(); // Thông báo cho recyclerview là dữ liệu đã thay đổi
//       }
//    }
//    public void onClickChangeTypeDisplay(){ // Bao gồm việc thây đổi type hiển thị cho item, và đổi icon của menu, đổi thêm layoutmanager
//        if(mCurrentType == MediaItem.TYPE_GRID){
////            setTypeDisplayRecyclerView(MediaItem.TYPE_LIST);
//            mainMediaItemAdapter.setCurrentType(MediaItem.TYPE_LIST);
//            mCurrentType = MediaItem.TYPE_LIST;
//        }else if(mCurrentType == MediaItem.TYPE_LIST){
////            setTypeDisplayRecyclerView(MediaItem.TYPE_STAGGERED);
//            mainMediaItemAdapter.setCurrentType(MediaItem.TYPE_GRID);
//            mCurrentType = MediaItem.TYPE_GRID;
//        }
//        setIconMenu();
//    }
//
//    private void setIconMenu() {
//        switch (mCurrentType)
//        {
//            case MediaItem.TYPE_GRID:
//                mMenu.getItem(1).setIcon(R.drawable.baseline_grid_view_24);
//
//                break;
//            case MediaItem.TYPE_LIST:
//                mMenu.getItem(1).setIcon(R.drawable.baseline_list_24);
//
//                break;
//        }
//    }
//
////    @Override
////    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {
////            Toast.makeText(getContext(), "Take a picture success", Toast.LENGTH_SHORT).show();
////            // Lấy dữ liệu trả về từ intent cho việc take a picture
////            Bundle bundle = data.getExtras();
////            // Lấy ảnh từ bundle có thể dưới dạng bitmap hoặc uri nhưng sẽ lấy uri để tiện cho việc lưu xuống
////            Uri uri = data.getData();
////
////            List<Uri> uriList = new ArrayList<>();
//////            uriList.add(Uri.withAppendedPath(Uri, ));
////
////            // Lưu ảnh vào MediaStore.Images.Media
////
////        }
////    }
//}
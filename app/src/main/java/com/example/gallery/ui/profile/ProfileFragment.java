package com.example.gallery.ui.profile;


import android.content.Intent;

import androidx.annotation.NonNull;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.gallery.BR;
import com.example.gallery.R;

import com.example.gallery.data.DataManager;
import com.example.gallery.databinding.ProfileBinding;
import com.example.gallery.ui.base.BaseFragment;
import com.example.gallery.ui.custom.AddImageFromDevice;
import com.example.gallery.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


import javax.annotation.Nullable;

/**
 * Created on 15/11/2023
 */


public class ProfileFragment extends BaseFragment<ProfileBinding, ProfileViewModel> implements ProfileNavigator {

    private ProfileBinding mProfileBinding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.profile;
    }

    public ProfileBinding getmProfileBinding() {
        return mProfileBinding;
    }

    public ProfileViewModel getmProfileViewModel() {
        return mViewModel;
    }

    // ---------------------------







//    ------------------------------



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        ------------------------------

        mViewModel.getNumberOfImages().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mProfileBinding.txtNumImg.setText("Tổng số ảnh: " + integer);
            }
        });

        mViewModel.getNumberOfAlbums().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mProfileBinding.txtNumAlbum.setText("Số bộ sưu tập: " + integer);
            }
        });

        mViewModel.getCurrentTask().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mProfileBinding.seekBarSync.setProgress(integer);
                int max = mViewModel.getTotalTask();
                mProfileBinding.seekBarSync.setMax(max);
                System.out.println("Max: " + max);
                int percent = (int) (integer * 100.0 / max);
                mProfileBinding.textViewSyncProgress.setText(  percent + "%" );
                mProfileBinding.textViewSyncProgress.setVisibility(View.VISIBLE);
            }
        });


        mViewModel.getUser().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<com.example.gallery.data.models.db.User>() {
            @Override
            public void onChanged(com.example.gallery.data.models.db.User user) {
                System.out.println("User : " + user);
            }
        });

//        ------------------------------

        mProfileBinding.txtName.setText("" + mViewModel.getDataManager().getCurrentUserName());
        int typeLogin = mViewModel.getDataManager().getCurrentUserLoggedInMode();
        String txtAccountType = "";
        switch (typeLogin) {
            case 1:
                txtAccountType = "Tài khoản Google";
                break;
            case 2:
                txtAccountType = "Tài khoản Facebook";
                break;
            case 3:
                txtAccountType = "Tài khoản Gallery";
                break;
        }
        mProfileBinding.txtTypeAccount.setText(txtAccountType);

        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {

                Glide.with(this)
                        .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(mProfileBinding.imgAvatar);

        }







//        -----------------------

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mProfileBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);

        super.onCreateView(inflater, container, savedInstanceState);
        mProfileBinding = getViewDataBinding();
        //  System.out.println("ProfileBinding 57: " + mProfileBinding);


        // Inside ProfileActivity
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        if (mViewModel != null) {
            //  System.out.println("ProfileActivity: " +  "mViewModel is not null: " + mViewModel);
            mViewModel.setNavigator(this);

        } else {
            // print to log
            //  System.out.println("ProfileActivity:" + " mViewModel is null");

        }

        //  System.out.println("ProfileBinding: " + mProfileBinding);

        mProfileBinding.setViewModel(mViewModel);




        // Inflate the layout for this fragment
        return mProfileBinding.getRoot();
    }


    //---------------------------

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


//---------------------------

    private static final int REQUEST_IMAGE_PICK = 1;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Xử lý kết quả từ AddImageFromDevice ở đây
    }


}


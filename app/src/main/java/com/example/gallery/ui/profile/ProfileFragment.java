package com.example.gallery.ui.profile;

import android.content.Intent;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.gallery.App;
import com.example.gallery.BR;
import com.example.gallery.R;
import com.example.gallery.data.local.prefs.AppPreferencesHelper;
import com.example.gallery.data.models.db.Album;
import com.example.gallery.data.models.db.MediaItem;
import com.example.gallery.data.repositories.models.Repository.MediaItemRepository;
import com.example.gallery.data.repositories.models.Repository.UserRepository;
import com.example.gallery.data.repositories.models.ViewModel.AlbumViewModel;
import com.example.gallery.data.repositories.models.ViewModel.UserViewModel;
import com.example.gallery.databinding.ProfileBinding;
import com.example.gallery.ui.base.BaseFragment;
import com.example.gallery.ui.login.LoginActivity;

import java.util.List;

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
        System.out.println("On created view : " + mProfileBinding);

//        ------------------------------
        System.out.println("mViewModel: " + mViewModel);
        mViewModel.getNumberOfImages().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                System.out.println("ProfileFragment 86: " + integer);
                mProfileBinding.txtNumImg.setText("Number of images: " + integer);
            }
        });

        mViewModel.getNumberOfAlbums().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                System.out.println("ProfileFragment 86: " + integer);
                mProfileBinding.txtNumAlbum.setText("Number of albums: " + integer);
            }
        });

//        ------------------------------

        mProfileBinding.txtUserID.setText("Your userId: " + mViewModel.getDataManager().getCurrentUserId() + " ");
        mProfileBinding.txtEmail.setText("Your email: " + mViewModel.getDataManager().getCurrentUserEmail());
        mProfileBinding.txtName.setText("Hello, " + mViewModel.getDataManager().getCurrentUserName());
//        mProfileBinding.txtNumImg.setText("Number of images: " + UserViewModel.getInstance().getNumberOfItems().getValue()); // loi failed to open file '/data/data/com.example.gallery/code_cache

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mProfileBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);

        super.onCreateView(inflater, container, savedInstanceState);
        mProfileBinding = getViewDataBinding();
        System.out.println("ProfileBinding 57: " + mProfileBinding);


        // Inside ProfileActivity
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        if (mViewModel != null) {
            System.out.println("ProfileActivity: " +  "mViewModel is not null: " + mViewModel);
            mViewModel.setNavigator(this);

        } else {
            // print to log
            System.out.println("ProfileActivity:" + " mViewModel is null");

        }

        System.out.println("ProfileBinding: " + mProfileBinding);

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



}

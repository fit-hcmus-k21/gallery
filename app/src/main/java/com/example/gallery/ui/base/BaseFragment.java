package com.example.gallery.ui.base;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.view.View;

public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment {
    private BaseActivity mActivity;
    private View mRootView;
    private T mViewDataBinding;

//      -------------------


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }


    //    -----------------
    public interface Callback {
        void onFragmentAttached();

        void onFragmentDetached(String tag);
}
}

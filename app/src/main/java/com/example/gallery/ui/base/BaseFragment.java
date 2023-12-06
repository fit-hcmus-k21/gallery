package com.example.gallery.ui.base;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment {
    private BaseActivity mActivity;
    private View mRootView;
    private T mViewDataBinding;

    protected V mViewModel;

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

    public abstract
    @LayoutRes
    int getLayoutId();



//-----------------------------





    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("BaseFragment: onCreateView");

        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);


        System.out.println("BaseFragment: onCreateView: mViewDataBinding: " + mViewDataBinding);
        mRootView = mViewDataBinding.getRoot();
        return mRootView;
    }



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        System.out.println("BaseFragment: onViewCreated");

        super.onViewCreated(view, savedInstanceState);
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.setLifecycleOwner(this);
        mViewDataBinding.executePendingBindings();

        System.out.println("BaseFragment: onViewCreated: mViewDataBinding: " + mViewDataBinding);
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }



}

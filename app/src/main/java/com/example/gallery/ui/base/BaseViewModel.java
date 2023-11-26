package com.example.gallery.ui.base;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gallery.App;
import com.example.gallery.data.DataManager;

import java.lang.ref.WeakReference;

public abstract class BaseViewModel<N> extends ViewModel {
    private final DataManager mDataManager;
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private WeakReference<N> mNavigator;

    public BaseViewModel() {
        this.mDataManager = App.getDataManager();
        mIsLoading.setValue(false);
    }

    public LiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
//        Toast.makeText(App.getInstance(), "setIsLoading: " + isLoading, Toast.LENGTH_SHORT).show();

        if (isLoading) {
            mIsLoading.postValue(true);
        } else {
            mIsLoading.postValue(false);
        }

    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }
}

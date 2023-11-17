package com.example.gallery.data.repositories.models;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QRCodeViewModel extends ViewModel {
    private QRCodeGenerator qrCodeGenerator;
    public MutableLiveData<Bitmap> qrCodeLiveData = new MutableLiveData<>();
    public void generateQRCode(String image_path){
        qrCodeGenerator = new QRCodeGenerator();
        qrCodeGenerator.callApi(image_path, new QRCodeGenerator.QRCodeCallback() {
            @Override
            public void onQRCodeGenerated(Bitmap bitmap) {
                qrCodeLiveData.postValue(bitmap);
            }
        });
    }
}

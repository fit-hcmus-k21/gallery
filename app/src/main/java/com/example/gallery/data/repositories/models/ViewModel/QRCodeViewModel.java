package com.example.gallery.data.repositories.models.ViewModel;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gallery.data.models.others.QRCodeGenerator;


public class QRCodeViewModel extends ViewModel {
    private QRCodeGenerator qrCodeGenerator;
    public MutableLiveData<Bitmap> qrCodeLiveData = new MutableLiveData<>();
    public void generateQRCode(String image_path){
        Log.e("Mytag", "checkAndGenerateQRCode" + image_path);

        qrCodeGenerator = new QRCodeGenerator();
        qrCodeGenerator.callApi(image_path, new QRCodeGenerator.QRCodeCallback() {
            @Override
            public void onQRCodeGenerated(Bitmap bitmap) {
                qrCodeLiveData.postValue(bitmap);
            }
        });
    }
}

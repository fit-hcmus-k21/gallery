package com.example.gallery.data.models.others;


import android.graphics.Bitmap;
import android.util.Log;

import com.example.gallery.api.json_holder.Imgur;
import com.example.gallery.api.ImgurApi;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 28/10/2023
 */

public class QRCodeGenerator {
    // attributes
    private String url;
    private String filePath; // This file is a photo that is selected from the gallery
    private int photoIdentifier;
    private QRCodeCallback qrCodeCallback; // Hàm này dùng để cho Slide35 có thể lấy đảm bảo lấy được dữ liệu từ API


    // methods
    public void callApi(String image_path, QRCodeCallback qrCodeCallback) {
        File file = new File(image_path);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        ImgurApi.imgurApi.uploadImage(multipartBody, "Bearer 2c3779246c9fc0626ea90b101d3d84f427e48d03").enqueue(new Callback<Imgur>() {
            @Override
            public void onResponse(Call<Imgur> call, Response<Imgur> response) {
                if (response.isSuccessful()) {

                    Imgur imgur = response.body();

                    if (imgur.isSuccess()) {

                        String link = imgur.getData().getLink();
                        Bitmap bitmap = generatorQRCode(link);

//                          destQRCodeImg = WritePhotoToExternalStorage.writePhotoToExternalStorage(this, bitmap);

                        qrCodeCallback.onQRCodeGenerated(bitmap);

                    }

                } else {
                    Log.e("QRCodeGenerator", "response is not successful");
                }
            }

            @Override
            public void onFailure(Call<Imgur> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });

    }

    public Bitmap generatorQRCode(String url) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(url, BarcodeFormat.QR_CODE, 800, 800);

            Bitmap bitmap = new BarcodeEncoder().createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    public interface QRCodeCallback {
        void onQRCodeGenerated(Bitmap bitmap);
    }
}


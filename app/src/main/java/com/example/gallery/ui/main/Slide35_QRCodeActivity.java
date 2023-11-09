package com.example.gallery.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gallery.R;
import com.example.gallery.utils.QRCodeCallback;
import com.example.gallery.data.repositories.models.QRCodeGenerator;

public class Slide35_QRCodeActivity extends Activity implements QRCodeCallback {
    public static final String MEDIA_ITEM_FILE_PATH = "MEDIA_ITEM_FILE_PATH";
    public static final int REQUEST_PERMISSION_CODE = 101;
    ImageView imageView_qr_code;
    ProgressDialog progressDialog;

//    Intent intent = getIntent();
//    String filePath = intent.getStringExtra(MEDIA_ITEM_FILE_PATH);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_35_qr_code);

        /*RequestPermission*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_PERMISSION_CODE);
            } else {
                generatorQRCode("filePath from intent");
                Log.e("QRCodeActivity", "Permission is granted");
            }
        }

        imageView_qr_code = (ImageView) findViewById(R.id.imageView_qr_code);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                generatorQRCode("filePath from intent");
            }
        }
    }

    // The filePath will be received when the user choose image from the gallery. And pass to this activity through intent.
    private void generatorQRCode(String filePath) {
        progressDialog.show();

        String filePath_forTest = "/storage/emulated/0/DCIM/Camera/20231018_172011.jpg";

        QRCodeGenerator qrCodeGenerator = new QRCodeGenerator(filePath_forTest, this);
        qrCodeGenerator.callApi();

    }

    @Override
    public void onQRCodeGenerated(Bitmap bitmap) {
        progressDialog.dismiss();

        imageView_qr_code.setImageBitmap(bitmap);
    }
}

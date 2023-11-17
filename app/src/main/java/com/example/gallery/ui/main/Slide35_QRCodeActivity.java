package com.example.gallery.ui.main;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gallery.R;
import com.example.gallery.data.repositories.models.QRCodeViewModel;

public class Slide35_QRCodeActivity extends AppCompatActivity {
    public static final String MEDIA_ITEM_FILE_PATH = "MEDIA_ITEM_FILE_PATH";
    public static final int REQUEST_PERMISSION_CODE = 101;
    QRCodeViewModel qrCodeViewModel;
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
                checkAndGenerateQRCode("file path from the intent");
                Log.e("QRCodeActivity", "Permission is granted");
            }
        }
        imageView_qr_code = (ImageView) findViewById(R.id.imageView_qr_code);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        // Lỗi ở đây là không thể cast được this thành ViewModelStoreOwner
        // Cách Sửa lại Slide35_QRCodeActivity extends Activity thành Slide35_QRCodeActivity extends AppCompatActivity
        qrCodeViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(QRCodeViewModel.class);
        qrCodeViewModel.qrCodeLiveData.observe((LifecycleOwner) this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                progressDialog.dismiss();
                imageView_qr_code.setImageBitmap(bitmap);
            }
        });
    }

    private void checkAndGenerateQRCode(String file_path) {
        progressDialog.show();

        String filePath_forTest = "/storage/emulated/0/DCIM/Camera/20231110_071650.jpg";
        qrCodeViewModel.generateQRCode(filePath_forTest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                checkAndGenerateQRCode("the file path from the intent");
            }
        }
    }


}

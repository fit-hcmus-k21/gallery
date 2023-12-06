package com.example.gallery.ui.main.doing;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gallery.R;
import com.example.gallery.ui.main.adapter.MyCustomView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.slider.Slider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawAction extends AppCompatActivity implements View.OnClickListener {
    MyCustomView drawPhoto;
    Bitmap input=null;
    BottomNavigationView drawNavigation;
    Bitmap savedrew;
    Button btnSave, btnNotSave;
    String kind = null;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.draw_layout);

        input = EditActivity.saveImage;
        drawNavigation = (BottomNavigationView) findViewById(R.id.DrawNavigation);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnNotSave = (Button) findViewById(R.id.btnNotSave);

        drawPhoto = new MyCustomView(this,input);
        drawPhoto.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mainLayout.addView(drawPhoto);

        drawNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.btnText){
                    Dialog dialog = new Dialog(DrawAction.this);
                    dialog.setContentView(R.layout.draw_text_dialog);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);
                    Button btnOk = dialog.findViewById(R.id.btnOk);
                    Button btnColor = dialog.findViewById(R.id.btnColour);
                    Button btnSize = dialog.findViewById(R.id.btnSize);
                    Slider sldColor = dialog.findViewById(R.id.SliderColor);
                    Slider sldSize = dialog.findViewById(R.id.SliderSize);
                    View pickColor = dialog.findViewById(R.id.choosenColor);
                    EditText text = dialog.findViewById(R.id.textChoose);

                    Integer[] colorValue = {Color.BLACK, Color.WHITE,Color.YELLOW,Color.RED,Color.GREEN,Color.CYAN,Color.MAGENTA,Color.GRAY,Color.BLUE,Color.LTGRAY};

                    btnColor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sldColor.setVisibility(View.VISIBLE);
                            sldSize.setVisibility(View.INVISIBLE);
                            pickColor.setVisibility(View.VISIBLE);
                        }
                    });
                    btnSize.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sldColor.setVisibility(View.INVISIBLE);
                            pickColor.setVisibility(View.INVISIBLE);
                            sldSize.setVisibility(View.VISIBLE);
                        }
                    });
                    sldColor.addOnChangeListener(new Slider.OnChangeListener() {
                        @Override
                        public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                            pickColor.setBackgroundColor(colorValue[(int)value]);
                        }
                    });
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (text.getText().toString().isEmpty()){
                                Toast.makeText(DrawAction.this,"Please enter your text to write on picture",Toast.LENGTH_SHORT).show();
                                text.setText("");
                            }
                            else{
                                int textSize = (int)sldSize.getValue();
                                int textColor = (int)sldColor.getValue();
                                String inputText = text.getText().toString();
                                dialog.dismiss();
                                drawPhoto.onUpdateDrawFunction(false,inputText,textSize,colorValue[textColor]);

                            }
                        }
                    });
                    dialog.show();
                }
                if(item.getItemId() == R.id.btnPen){
//                    drawPhoto.onUpdateDrawFunction(true,"",1,1);
                    //draw dialog
                    Dialog dialog = new Dialog(DrawAction.this);
                    dialog.setContentView(R.layout.draw_line_dialog);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);

                    Button btnColour = dialog.findViewById(R.id.btnColour);
                    Button btnSize = dialog.findViewById(R.id.btnSize);
                    Button btnOk = dialog.findViewById(R.id.btnOK);
                    ImageButton btnLine = dialog.findViewById(R.id.drawLine);
                    ImageButton btnCircle = dialog.findViewById(R.id.drawCircle);
                    ImageButton btnSquare = dialog.findViewById(R.id.drawSquare);
                    ImageButton btnCurve = dialog.findViewById(R.id.drawCurve);

                    btnLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnLine.setBackgroundColor(getResources().getColor(R.color.selected));
                            btnCircle.setBackground(null);
                            btnSquare.setBackground(null);
                            btnCurve.setBackground(null);
                            kind = "Line";
                        }
                    });
                    btnCurve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnCurve.setBackgroundColor(getResources().getColor(R.color.selected));
                            btnCircle.setBackground(null);
                            btnSquare.setBackground(null);
                            btnLine.setBackground(null);
                            kind = "Curve";
                        }
                    });
                    btnCircle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnCircle.setBackgroundColor(getResources().getColor(R.color.selected));
                            btnSquare.setBackground(null);
                            btnCurve.setBackground(null);
                            btnLine.setBackground(null);
                            kind = "Circle";
                        }
                    });
                    btnSquare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnSquare.setBackgroundColor(getResources().getColor(R.color.selected));
                            btnCircle.setBackground(null);
                            btnCurve.setBackground(null);
                            btnLine.setBackground(null);
                            kind = "Square";
                        }
                    });

                    View colorPicker = dialog.findViewById(R.id.choosenColor);
                    Slider sldColour = dialog.findViewById(R.id.SliderColor);
                    Slider sldSize = dialog.findViewById(R.id.size);

                    Integer[] colorValue = {Color.BLACK, Color.WHITE,Color.YELLOW,Color.RED,Color.GREEN,Color.CYAN,Color.MAGENTA,Color.GRAY,Color.BLUE,Color.LTGRAY};

                    sldColour.addOnChangeListener(new Slider.OnChangeListener() {
                        @Override
                        public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                            colorPicker.setBackgroundColor(colorValue[(int)value]);
                        }
                    });
                    btnColour.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sldColour.setVisibility(View.VISIBLE);
                            colorPicker.setVisibility(View.VISIBLE);
                            sldSize.setVisibility(View.INVISIBLE);
                        }
                    });
                    btnSize.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sldColour.setVisibility(View.INVISIBLE);
                            colorPicker.setVisibility(View.INVISIBLE);
                            sldSize.setVisibility(View.VISIBLE);
                        }
                    });
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int color = colorValue[(int)sldColour.getValue()];
                            int size = (int) sldSize.getValue();
                            drawPhoto.onUpdateDrawFunction(true,kind,size,color);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                return true;
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedrew = drawPhoto.saveDrawingToBitmap();
                String path = saveBitmapToStorage(savedrew);
                Intent save = new Intent(DrawAction.this,EditActivity.class);
                save.putExtra("path",path);
                setResult(RESULT_OK,save);
                EditActivity.time+=1;
                finish();
            }
        });
        btnNotSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedrew = input;
                String path = saveBitmapToStorage(savedrew);
                Intent save = new Intent(DrawAction.this,EditActivity.class);
                save.putExtra("path",path);
                setResult(RESULT_OK,save);

                finish();
            }
        });

    }
    private String saveBitmapToStorage(Bitmap bitmap) {
        String fileName = "image.jpg"; // Tên tệp hình ảnh
        File storageDir = getExternalCacheDir(); // Lấy thư mục tạm ngoài
        File imageFile = new File(storageDir, fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            // Xảy ra lỗi khi lưu tệp hình ảnh
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
    @Override
    public void onClick(View v) {

    }
}

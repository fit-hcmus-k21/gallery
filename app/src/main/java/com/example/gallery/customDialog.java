package com.example.gallery;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.findlayout.R;
import com.google.android.material.textfield.TextInputLayout;

public class customDialog extends Dialog{

    public Context context;
    private TextInputLayout edtName;
    AutoCompleteTextView edtSource;
    Button btnOk,btnCancel;
    String[] source = {"Facebook","Zalo","Camera"};

    public customDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }
    @NonNull
    protected void onCreate(Bundle saveStateInstance){
        super.onCreate(saveStateInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_button_dialog);
        this.getWindow().setLayout(700,700);        // set dialog size in main window

        edtName = (TextInputLayout) findViewById(R.id.edtNamePic);
        edtSource = (AutoCompleteTextView) findViewById(R.id.outlined_exposed_dropdown_editable);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,source);
        edtSource.setAdapter(adapter);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOk = (Button) findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOkClick();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCancelClick();
            }
        });

    }
    private void buttonOkClick(){this.dismiss();}
    private void buttonCancelClick(){this.dismiss();}
}
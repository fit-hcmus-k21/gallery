package com.example.gallery.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.gallery.BR;
import com.example.gallery.R;
import com.example.gallery.databinding.Slide03RegisterScreenBinding;
import com.example.gallery.ui.base.BaseActivity;
import com.example.gallery.ui.login.LoginActivity;
import com.example.gallery.ui.main.MainActivity;

/**
 * Created on 16/11/2023
 */


public class RegisterActivity extends BaseActivity<Slide03RegisterScreenBinding, RegisterViewModel> implements RegisterNavigator {

    private Slide03RegisterScreenBinding mRegisterBinding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.slide03_register_screen;
    }

    @Override
    public void handleError(Object throwable) {
        // handle error
    }

    @Override
    public void register() {
        String username = mRegisterBinding.textUsername.getText().toString();
        String password = mRegisterBinding.textPassword.getText().toString();

        // toast to show username and password
        Toast.makeText(this, "Username: " + username + "\nPassword: " + password, Toast.LENGTH_SHORT).show();


        if (mViewModel.isUsernameAndPasswordValid(username, password)) {
//            View view = this.getCurrentFocus();
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            mViewModel.register(username, password);
        } else {
            Toast.makeText(this, "Invalid username/ password !" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "RegisterActivity", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        mRegisterBinding = getViewDataBinding();


        // Inside RegisterActivity
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        if (mViewModel != null) {
            Log.e("RegisterActivity", "mViewModel is not null");
            mViewModel.setNavigator(this);

        } else {
            // print to log
            Log.e("RegisterActivity", "mViewModel is null");

        }

        mRegisterBinding.setViewModel(mViewModel);


    }

    @Override
    public void openLoginActivity() {
        // open login activity
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}

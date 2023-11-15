package com.example.gallery.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.example.gallery.BR;
import com.example.gallery.R;
import com.example.gallery.databinding.Slide02LoginScreenBinding;
import com.example.gallery.ui.base.BaseActivity;
import com.example.gallery.ui.main.MainActivity;

/**
 * Created on 15/11/2023
 */


public class LoginActivity extends BaseActivity<Slide02LoginScreenBinding, LoginViewModel> implements LoginNavigator {

    private Slide02LoginScreenBinding mActivityLoginBinding;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.slide02_login_screen;
    }

    @Override
    public void handleError(Object throwable) {
        // handle error
    }

    @Override
    public void login() {
        String username = mActivityLoginBinding.textUsername.getText().toString();
        String password = mActivityLoginBinding.textPassword.getText().toString();
        if (mViewModel.isUsernameAndPasswordValid(username, password)) {
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            mViewModel.login(username, password);
        } else {
            Toast.makeText(this, "Invalid username/ password !" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = getViewDataBinding();
        mViewModel.setNavigator(this);
    }

    @Override
    public void openRegisterActivity() {
        // open register activity
    }

}


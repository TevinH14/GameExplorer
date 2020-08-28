package com.example.gameexplorer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gameexplorer.R;
import com.example.gameexplorer.fragment.ForgetPasswordFragment;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_forgetPasswordContainer, ForgetPasswordFragment.newInstance())
                .commit();
    }
}

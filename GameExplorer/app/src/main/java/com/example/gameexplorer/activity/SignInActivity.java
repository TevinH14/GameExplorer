package com.example.gameexplorer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gameexplorer.R;
import com.example.gameexplorer.fragment.SignInFragment;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_signInContainer, SignInFragment.newInstance())
                .commit();
    }
}

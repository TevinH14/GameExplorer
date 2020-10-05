package com.example.gameexplorer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.gameexplorer.R;
import com.example.gameexplorer.fragment.gamesFragment.GameDetailFragment;
import com.example.gameexplorer.fragment.settingFragment.ImageFragment;
import com.example.gameexplorer.fragment.userAuthFragments.SignUpFragment;

public class UserImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean comingFromSignUp = false;
        if(getIntent() != null){
            comingFromSignUp =getIntent().getBooleanExtra(
                    SignUpFragment.COMING_FROM_SIGN_UP,false);
        }
        int containerId;
        if(comingFromSignUp){
            setContentView(R.layout.activity_user_image);
            containerId = R.id.fl_imageSet_su;
        }
        else {
            setContentView(R.layout.activity_image);
            containerId = R.id.fl_image_s;
            Toolbar toolbar = findViewById(R.id.toolbar_gd);
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, ImageFragment.newInstance(comingFromSignUp))
                .commit();
    }
}

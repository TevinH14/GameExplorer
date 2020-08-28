package com.example.gameexplorer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.gameexplorer.R;
import com.example.gameexplorer.firebaseHelper.UserAuthenticationHelper;
import com.example.gameexplorer.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!UserAuthenticationHelper.checkUserStatus()){
            setUpView();
        }else {
            Intent homeIntent = new Intent(this,HomeActivity.class);
            startActivity(homeIntent);
        }
    }

    private void setUpView(){
        setContentView(R.layout.activity_main);
        //connect fragment MainFragment to this activity
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_mainContainer, MainFragment.newInstance())
                .commit();
    }
}

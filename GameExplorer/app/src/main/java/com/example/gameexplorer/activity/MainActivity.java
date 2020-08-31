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
        setContentView(R.layout.activity_main);
        if(!UserAuthenticationHelper.checkUserStatus()){
            setUpView();
        }else {
            Intent homeIntent = new Intent(this,HomeActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
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

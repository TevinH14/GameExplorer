package com.example.gameexplorer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gameexplorer.R;
import com.example.gameexplorer.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: check if user is signed in

        //connect fragment MainFragment to this activity
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_mainContainer, MainFragment.newInstance())
                .commit();
    }
}

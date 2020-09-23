package com.example.gameexplorer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceControl;

import com.example.gameexplorer.R;
import com.example.gameexplorer.firebaseHelper.RealTimeDatabaseHelper;
import com.example.gameexplorer.fragment.GameDetailFragment;
import com.example.gameexplorer.fragment.GamesFragment;
import com.example.gameexplorer.model.Games;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class GameDetailActivity extends AppCompatActivity {
    public static final String GAME_DETAIL_EXTRA = "GAME_DETAIL_EXTRA";

    private String mGame = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        if(getIntent() != null){
            mGame = getIntent().getStringExtra(GAME_DETAIL_EXTRA);
        }
        Toolbar toolbar = findViewById(R.id.toolbar_gd);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_gameDetail, GameDetailFragment.newInstance(mGame))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return false;

    }
}

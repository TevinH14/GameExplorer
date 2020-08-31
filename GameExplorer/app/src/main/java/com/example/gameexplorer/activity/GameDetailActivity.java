package com.example.gameexplorer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gameexplorer.R;
import com.example.gameexplorer.fragment.GameDetailFragment;
import com.example.gameexplorer.fragment.GamesFragment;
import com.example.gameexplorer.model.Games;

public class GameDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        Games game = null;
        if(getIntent() != null){
             game = (Games)getIntent().getSerializableExtra(GamesFragment.GAME_DETAIL_EXTRA);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_gameDetail, GameDetailFragment.newInstance(game))
                .commit();
    }
}

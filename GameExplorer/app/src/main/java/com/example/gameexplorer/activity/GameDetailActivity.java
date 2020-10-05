package com.example.gameexplorer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.gameexplorer.R;
import com.example.gameexplorer.firebaseHelper.RealTimeDatabaseHelper;
import com.example.gameexplorer.fragment.gamesFragment.GameDetailFragment;
import com.example.gameexplorer.model.GameDetail;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.networkHelper.gameTasks.GameDetailTask;

public class GameDetailActivity extends AppCompatActivity implements GameDetailTask.OnGamesFinished,
RealTimeDatabaseHelper.GameExistFinished{
    public static final String GAME_DETAIL_EXTRA = "GAME_DETAIL_EXTRA";

       private GameDetail mGameDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        String slug = "";
        if(getIntent() != null){
            slug = getIntent().getStringExtra(GAME_DETAIL_EXTRA);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_gd);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        GameDetailTask gdt = new GameDetailTask(this);
        gdt.execute("https://api.rawg.io/api/games/"+slug);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_gameDetail, GameDetailFragment.newInstance(null,false))
                .commit();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onGameDetailPost(GameDetail games) {
        mGameDetail = games;
        new RealTimeDatabaseHelper(this);
        RealTimeDatabaseHelper.checkIfGameExist(mGameDetail.getGameId());
    }

    @Override
    public void onExistPost(boolean hasGame) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_gameDetail, GameDetailFragment.newInstance(mGameDetail,hasGame))
                .commit();
    }

    public void replaceFragments() {
        RealTimeDatabaseHelper.checkIfGameExist(mGameDetail.getGameId());
    }
}

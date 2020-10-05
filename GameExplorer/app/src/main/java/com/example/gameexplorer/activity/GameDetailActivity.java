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
import com.example.gameexplorer.networkHelper.gameTasks.GameDetailTask;

public class GameDetailActivity extends AppCompatActivity implements GameDetailTask.OnGamesFinished,
RealTimeDatabaseHelper.GameExistFinished{
    public static final String GAME_DETAIL_EXTRA = "GAME_DETAIL_EXTRA";

    private String mSlug = null;
    private GameDetail mGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        if(getIntent() != null){
            mSlug = getIntent().getStringExtra(GAME_DETAIL_EXTRA);
        }
        Toolbar toolbar = findViewById(R.id.toolbar_gd);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        GameDetailTask gdt = new GameDetailTask(this);
        gdt.execute("https://api.rawg.io/api/games/"+mSlug);

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
        mGame = games;
        new RealTimeDatabaseHelper(this);
        RealTimeDatabaseHelper.checkIfGameExist(mGame.getGameId());
    }

    @Override
    public void onExistPost(boolean hasGame) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_gameDetail, GameDetailFragment.newInstance(mGame,hasGame))
                .commit();
    }

    public void replaceFragments() {
        RealTimeDatabaseHelper.checkIfGameExist(mGame.getGameId());
    }
}

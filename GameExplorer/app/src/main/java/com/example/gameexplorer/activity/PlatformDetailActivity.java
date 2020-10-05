package com.example.gameexplorer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.gameexplorer.R;
import com.example.gameexplorer.fragment.gamesFragment.GamesFragment;
import com.example.gameexplorer.fragment.platformFragment.PlatformDetailFragment;
import com.example.gameexplorer.model.GamePlatform;
import com.example.gameexplorer.networkHelper.platformTasks.PlatformDetailTask;

public class PlatformDetailActivity extends AppCompatActivity
        implements PlatformDetailTask.OnPlatformFinished {
    public static final String PLATFORM_DETAIL_EXTRA = "PLATFORM_DETAIL_EXTRA";

    private GamePlatform mPlatform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_detail);

        if(getIntent() != null){
            mPlatform  = (GamePlatform) getIntent().getSerializableExtra(PLATFORM_DETAIL_EXTRA);
        }
        Toolbar toolbar = findViewById(R.id.toolbar_gd);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        PlatformDetailTask pdt = new PlatformDetailTask(this);
        pdt.execute(mPlatform);
    }

    @Override
    public void onPlatformDetailPost(GamePlatform platform) {
        mPlatform = platform;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_platformDetail, PlatformDetailFragment.newInstance(mPlatform))
                        .commit();
    }

    public void replaceFragments(String url) {
        Fragment fragment = null;
        try {
            fragment =  GamesFragment.newInstance(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.fl_platformDetail,
                    fragment).addToBackStack(null)
                    .commit();
        }
    }
}

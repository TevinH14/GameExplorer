package com.example.gameexplorer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.gameexplorer.R;
import com.example.gameexplorer.fragment.developerFragments.DeveloperDetailFragment;
import com.example.gameexplorer.fragment.gamesFragment.GamesFragment;
import com.example.gameexplorer.model.Developer;
import com.example.gameexplorer.networkHelper.developerTasks.DeveloperDetailTask;

public class DeveloperDetailActivity extends AppCompatActivity
        implements DeveloperDetailTask.OnDeveloperFinishedFinished {
    public static final String DEVELOPER_DETAIL_EXTRA = "DEVELOPER_DETAIL_EXTRA";

    private Developer mDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_detail);


        if(getIntent() != null){
            mDeveloper  = (Developer) getIntent().getSerializableExtra(DEVELOPER_DETAIL_EXTRA);
        }
        Toolbar toolbar = findViewById(R.id.toolbar_gd);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        DeveloperDetailTask ddt = new DeveloperDetailTask(this);
        ddt.execute(mDeveloper);
    }

    @Override
    public void onDeveloperDetailPost(Developer developer) {
        mDeveloper = developer;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_developerDetail, DeveloperDetailFragment.newInstance(mDeveloper))
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
            fragmentManager.beginTransaction().replace(R.id.fl_developerDetail,
                    fragment).addToBackStack(null)
                    .commit();
        }
    }
}

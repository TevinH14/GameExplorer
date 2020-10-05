package com.example.gameexplorer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.gameexplorer.R;
import com.example.gameexplorer.fragment.creatorFragments.CreatorDetailFragment;
import com.example.gameexplorer.fragment.gamesFragment.GamesFragment;
import com.example.gameexplorer.model.Creator;
import com.example.gameexplorer.networkHelper.creatorTask.CreatorDetailTask;

public class CreatorDetailActivity extends AppCompatActivity implements CreatorDetailTask.OnCreatorFinished {
    public static final String CREATOR_DETAIL_EXTRA = "CREATOR_DETAIL_EXTRA";

    private Creator mCreator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_detail);
        if(getIntent() != null){
            mCreator  = (Creator) getIntent().getSerializableExtra(CREATOR_DETAIL_EXTRA);
        }
        Toolbar toolbar = findViewById(R.id.toolbar_gd);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        CreatorDetailTask cdt = new CreatorDetailTask(this);
        cdt.execute(mCreator);
    }

    @Override
    public void onCreatorDetailPost(Creator creator) {
        mCreator = creator;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_creatorDetail, CreatorDetailFragment.newInstance(mCreator))
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
            fragmentManager.beginTransaction().replace(R.id.fl_creatorDetail,
                    fragment).addToBackStack(null)
                    .commit();
        }
    }
}

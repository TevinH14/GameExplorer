package com.example.gameexplorer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.gameexplorer.R;
import com.example.gameexplorer.fragment.gamesFragment.GamesFragment;
import com.example.gameexplorer.fragment.publisherFragments.PublisherDetailFragment;
import com.example.gameexplorer.model.Publisher;
import com.example.gameexplorer.networkHelper.publisherTasks.PublisherDetailTask;

public class PublishersDetailActivity extends AppCompatActivity
        implements PublisherDetailTask.OnPublisherFinishedFinished {

    //constant
    public static final String PUBLISHERS_DETAIL_EXTRA = "PUBLISHERS_DETAIL_EXTRA";

    private Publisher mPublisher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishers_detail);

        //check if there is a publisher intent
        //assign publisher object
        if(getIntent() != null){
            mPublisher  = (Publisher) getIntent().getSerializableExtra(PUBLISHERS_DETAIL_EXTRA);
        }

        //set tool bar in activity
        Toolbar toolbar = findViewById(R.id.toolbar_gd);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        PublisherDetailTask pdt = new PublisherDetailTask(this);
        pdt.execute(mPublisher);
    }

    // on post bring publisher detail fragment to view
    @Override
    public void onPublisherDetailPost(Publisher publisher) {
        mPublisher = publisher;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_publisher_detail, PublisherDetailFragment.newInstance(mPublisher))
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
            fragmentManager.beginTransaction().replace(R.id.fl_publisher_detail,
                    fragment).addToBackStack(null)
                    .commit();
        }
    }
}

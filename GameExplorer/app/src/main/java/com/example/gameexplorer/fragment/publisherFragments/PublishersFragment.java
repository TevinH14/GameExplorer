package com.example.gameexplorer.fragment.publisherFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.PublishersDetailActivity;
import com.example.gameexplorer.adapter.PublisherAdapter;
import com.example.gameexplorer.model.Publisher;
import com.example.gameexplorer.networkHelper.NetworkUtils;
import com.example.gameexplorer.networkHelper.publisherTasks.PublisherTask;

import java.util.ArrayList;

public class PublishersFragment extends Fragment implements PublisherTask.OnPublisherFinished,
        AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener{

    private static String mUrlStart;
    private String mUrlEnd;
    private String mNextUrl;
    private ArrayList<Publisher> mPublisherList;
    private PublisherAdapter mPublishersAdapter;
    private PublisherTask mPublisherTask;
    private boolean mIsLoaded = false;
    private boolean mSorted = false;
    private int preLast;
    private ProgressBar mProgressBar;

    public static PublishersFragment newInstance() {

        Bundle args = new Bundle();

        PublishersFragment fragment = new PublishersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null && getContext() != null){
            mUrlStart = NetworkUtils.getPublishersUrl();
            mPublisherList = new ArrayList<>();
            mProgressBar = getView().findViewById(R.id.pg_gameIsloading_gd);
            ListView lv_platforms = getView().findViewById(R.id.lv_game_display);
            lv_platforms.setOnScrollListener(this);

            lv_platforms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent publishersDetailIntent = new Intent(getContext(), PublishersDetailActivity.class);
                    publishersDetailIntent.putExtra(PublishersDetailActivity
                            .PUBLISHERS_DETAIL_EXTRA,mPublisherList
                            .get(position));
                    startActivity(publishersDetailIntent);
                }
            });
            mPublishersAdapter = new PublisherAdapter(getContext(),mPublisherList);
            lv_platforms.setAdapter(mPublishersAdapter);

            networkTask();
        }
    }

    @Override
    public void onPublisherPost(ArrayList<Publisher> _publisher) {
        if(getView() != null){
            mNextUrl = mPublisherTask.getNextUrl();
            AddGameData(_publisher);
            mIsLoaded = true;
        }
    }

    private void AddGameData(final ArrayList<Publisher> _publisher){
        mPublisherList.addAll(_publisher);
        mPublishersAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);

    }

    private void networkTask(){
        mPublisherTask = new PublisherTask(this);
        if(!mIsLoaded && !mSorted ) {
            mPublisherTask.execute(mUrlStart);
        }else if(mSorted && !mIsLoaded){
            mPublisherTask.execute(mUrlStart + mUrlEnd);
        }

        if(mIsLoaded){
            mPublisherTask.execute(mNextUrl);
        }
        else {
            mPublisherList.clear();
            mPublishersAdapter.notifyDataSetChanged();
            preLast = 0;
        }
        mProgressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view.getId() == R.id.lv_game_display) {
            final int lastRow = firstVisibleItem + visibleItemCount;
            if (lastRow == totalItemCount) {
                if (preLast != lastRow && mIsLoaded) {
                    Log.i("Last", "Last");
                    preLast = lastRow;
                    mProgressBar.setVisibility(View.VISIBLE);

                    networkTask();
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    //Not currently used
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}


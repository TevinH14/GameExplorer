package com.example.gameexplorer.fragment.developerFragments;

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
import com.example.gameexplorer.activity.DeveloperDetailActivity;
import com.example.gameexplorer.adapter.DeveloperAdapter;
import com.example.gameexplorer.model.Developer;
import com.example.gameexplorer.networkHelper.developerTasks.DeveloperTask;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import java.util.ArrayList;

public class DevelopersFragment extends Fragment implements DeveloperTask.OnDeveloperFinished,
        AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener {

    private static String mUrlStart;
    private String mUrlEnd;
    private String mNextUrl;
    private ArrayList<Developer> mDeveloperList;
    private DeveloperAdapter mDeveloperAdapter;
    private DeveloperTask mDeveloperTask;
    private boolean mIsLoaded = false;
    private boolean mSorted = false;
    private int preLast;
    private ProgressBar mProgressBar;

    public static DevelopersFragment newInstance() {

        Bundle args = new Bundle();

        DevelopersFragment fragment = new DevelopersFragment();
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
            mUrlStart = NetworkUtils.getDevelopersUrl();
            mDeveloperList = new ArrayList<>();
            mProgressBar = getView().findViewById(R.id.pg_gameIsloading_gd);
            ListView lv_platforms = getView().findViewById(R.id.lv_game_display);
            lv_platforms.setOnScrollListener(this);

            lv_platforms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent devDetailIntent = new Intent(getContext(), DeveloperDetailActivity.class);
                    devDetailIntent.putExtra(DeveloperDetailActivity
                            .DEVELOPER_DETAIL_EXTRA,mDeveloperList
                            .get(position));
                    startActivity(devDetailIntent);
                }
            });
            mDeveloperAdapter = new DeveloperAdapter(getContext(),mDeveloperList);
            lv_platforms.setAdapter(mDeveloperAdapter);

            networkTask();
        }
    }

    private void AddGameData(final ArrayList<Developer> _developers){
        mDeveloperList.addAll(_developers);
        mDeveloperAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);

    }

    private void networkTask(){
        mDeveloperTask = new DeveloperTask(this);
        if(!mIsLoaded && !mSorted ) {
            mDeveloperTask.execute(mUrlStart);
        }else if(mSorted && !mIsLoaded){
            mDeveloperTask.execute(mUrlStart + mUrlEnd);
        }

        if(mIsLoaded){
            mDeveloperTask.execute(mNextUrl);
        }
        else {
            mDeveloperList.clear();
            mDeveloperAdapter.notifyDataSetChanged();
            preLast = 0;
        }
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeveloperPost(ArrayList<Developer> developers) {
        if(getView() != null){
            mNextUrl = mDeveloperTask.getNextUrl();
            AddGameData(developers);
            mIsLoaded = true;
        }
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

}

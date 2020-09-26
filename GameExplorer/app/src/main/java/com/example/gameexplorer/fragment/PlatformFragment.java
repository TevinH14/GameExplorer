package com.example.gameexplorer.fragment;

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
import com.example.gameexplorer.activity.GameDetailActivity;
import com.example.gameexplorer.adapter.DisplayPlatformAdapter;
import com.example.gameexplorer.adapter.GamesDisplayAdapter;
import com.example.gameexplorer.model.GamePlatform;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.networkHelper.GamesTask;
import com.example.gameexplorer.networkHelper.NetworkUtils;
import com.example.gameexplorer.networkHelper.PlatformTask;

import java.util.ArrayList;

public class PlatformFragment extends Fragment implements PlatformTask.OnPlatformFinished,
        AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener {
    private static String mUrlStart;
    private String mUrlEnd;
    private String mNextUrl;
    private ArrayList<GamePlatform> mPlatformsList;
    private DisplayPlatformAdapter mPlatformAdapter;
    private PlatformTask mPlatformTask;
    private boolean mIsLoaded = false;
    private boolean mSorted = false;
    private int preLast;
    private ProgressBar mProgressBar;

    public static PlatformFragment newInstance() {
        Bundle args = new Bundle();

        PlatformFragment fragment = new PlatformFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_display,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null && getContext() != null){
            mUrlStart = NetworkUtils.getPlatformUrl();
            mPlatformsList = new ArrayList<>();
            mProgressBar = getView().findViewById(R.id.pg_gameIsloading_gd);
            ListView lv_platforms = getView().findViewById(R.id.lv_game_display);
            lv_platforms.setOnScrollListener(this);

            lv_platforms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent gameDetailIntent = new Intent(getContext(), GameDetailActivity.class);
//                    gameDetailIntent.putExtra(GameDetailActivity
//                            .GAME_DETAIL_EXTRA,mPlatformsList
//                            .get(position)
//                            .getId());
//                    startActivity(gameDetailIntent);
                }
            });
            mPlatformAdapter = new DisplayPlatformAdapter(getContext(),mPlatformsList);
            lv_platforms.setAdapter(mPlatformAdapter);

            networkTask();
        }

    }

    @Override
    public void onPlatformPost(ArrayList<GamePlatform> platforms) {
        if(getView() != null){
            mNextUrl = mPlatformTask.getNextUrl();
            AddGameData(platforms);
            mIsLoaded = true;
        }
    }
    private void AddGameData(final ArrayList<GamePlatform> platforms){
        mPlatformsList.addAll(platforms);
        mPlatformAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

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

    private void networkTask(){
        mPlatformTask = new PlatformTask(this);
        if(!mIsLoaded && !mSorted ) {
            mPlatformTask.execute(mUrlStart);
        }else if(mSorted && !mIsLoaded){
            mPlatformTask.execute(mUrlStart + mUrlEnd);
        }

        if(mIsLoaded){
            mPlatformTask.execute(mNextUrl);
        }
        else {
            mPlatformsList.clear();
            mPlatformAdapter.notifyDataSetChanged();
            preLast = 0;
        }
        mProgressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

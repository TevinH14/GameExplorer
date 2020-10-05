package com.example.gameexplorer.fragment.gamesFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.GameDetailActivity;
import com.example.gameexplorer.adapter.gameAdapters.GamesDisplayAdapter;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.networkHelper.gameTasks.GamesTask;

import java.util.ArrayList;

public class GamesFragment extends Fragment implements GamesTask.OnGamesFinished,
        AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener {
    private static String mUrlStart;
    private String mUrlEnd;
    private String mNextUrl;
    private ArrayList<Games> mGameList;
    private GamesDisplayAdapter mGameAdapter;
    private GamesTask mGameTask;
    private boolean mIsLoaded = false;
    private boolean mSorted = false;
    private int preLast;
    private ProgressBar mProgressBar;


    public static GamesFragment newInstance(String url) {
        mUrlStart = url;
        Bundle args = new Bundle();

        GamesFragment fragment = new GamesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_display,container,false);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home  && getActivity() != null) {
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null && getContext() != null){
            mGameList = new ArrayList<>();

            //set up spinner
            Spinner spinner = getView().findViewById(R.id.sp_sort_game);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.sort_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setOnItemSelectedListener(this);
            spinner.setAdapter(adapter);

            mProgressBar = getView().findViewById(R.id.pg_gameIsloading_gd);

            ListView lv_games = getView().findViewById(R.id.lv_game_display);
            lv_games.setOnScrollListener(this);
            lv_games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent gameDetailIntent = new Intent(getContext(), GameDetailActivity.class);
                    gameDetailIntent.putExtra(GameDetailActivity
                            .GAME_DETAIL_EXTRA,mGameList
                            .get(position).getSlugName());
                    startActivity(gameDetailIntent);
                }
            });

            //set listView adapter
            mGameAdapter = new GamesDisplayAdapter(getContext(),mGameList);
            lv_games.setAdapter(mGameAdapter);
        }
    }

    @Override
    public void onGamesPost(final ArrayList<Games> games) {
        if(getView() != null && games != null){
            mNextUrl = mGameTask.getNextUrl();
            AddGameData(games);
            mIsLoaded = true;
        }
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onGamePre() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void AddGameData(final ArrayList<Games> games){
        mGameList.addAll(games);
        mGameAdapter.notifyDataSetChanged();

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

            switch (position) {
                case 0:
                    mIsLoaded = false;
                    mSorted = false;
                    break;
                case 1:
                    mUrlEnd = "&ordering=name";
                    mIsLoaded = false;
                    mSorted = true;
                    break;
                case 2:
                    mUrlEnd = "&ordering=-name";
                    mIsLoaded = false;
                    mSorted = true;
                    break;

                case 3:
                    mUrlEnd = "&ordering=-rating";
                    mIsLoaded = false;
                    mSorted = true;
                    break;
                case 4:
                    mUrlEnd = "&ordering=rating";
                    mIsLoaded = false;
                    mSorted = true;
                    break;
                case 5:
                    mUrlEnd = "&ordering=-released";
                    mIsLoaded = false;
                    mSorted = true;
                    break;
                case 6:
                    mUrlEnd = "&ordering=released";
                    mIsLoaded = false;
                    mSorted = true;
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
        networkTask();

    }

    private void networkTask(){
        mGameTask = new GamesTask(this);
        if(!mIsLoaded && !mSorted ) {
            mGameTask.execute(mUrlStart);
        }else if(mSorted && !mIsLoaded){
            mGameTask.execute(mUrlStart + mUrlEnd);
        }

        if(mNextUrl != null) {
            if (mIsLoaded) {
                mGameTask.execute(mNextUrl);
            } else if(!mNextUrl.equals("null")) {
                mGameList.clear();
                mGameAdapter.notifyDataSetChanged();
                preLast = 0;
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

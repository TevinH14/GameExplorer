package com.example.gameexplorer.fragment.creatorFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.gameexplorer.activity.CreatorDetailActivity;
import com.example.gameexplorer.adapter.CreatorsAdapter;
import com.example.gameexplorer.model.Creator;

import com.example.gameexplorer.networkHelper.creatorTask.CreatorTask;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import java.util.ArrayList;

public class CreatorFragment extends Fragment implements CreatorTask.OnCreatorFinished,
        AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener {
    private static String mUrlStart;
    private String mUrlEnd;
    private String mNextUrl;
    private ArrayList<Creator> mCreatorList;
    private CreatorsAdapter mCreatorAdapter;
    private CreatorTask mCreatorTask;
    private boolean mIsLoaded = false;
    private boolean mSorted = false;
    private int preLast;
    private ProgressBar mProgressBar;
    public static CreatorFragment newInstance() {

        Bundle args = new Bundle();

        CreatorFragment fragment = new CreatorFragment();
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
            mUrlStart = NetworkUtils.getCreatorsUrl();

            mCreatorList = new ArrayList<>();

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
                    Intent gameDetailIntent = new Intent(getContext(), CreatorDetailActivity.class);
                    gameDetailIntent.putExtra(CreatorDetailActivity
                            .CREATOR_DETAIL_EXTRA,mCreatorList
                            .get(position));
                    startActivity(gameDetailIntent);
                }
            });

            //set listView adapter
            mCreatorAdapter = new CreatorsAdapter(getContext(),mCreatorList);
            lv_games.setAdapter(mCreatorAdapter);

            networkTask();
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onCreatorPost(ArrayList<Creator> _creators) {
        if(getView() != null){
            mNextUrl = mCreatorTask.getNextUrl();
            AddGameData(_creators);
            mIsLoaded = true;
        }
    }

    private void AddGameData(final ArrayList<Creator> _creators){
        mCreatorList.addAll(_creators);
        mCreatorAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);

    }

    private void networkTask(){
        mCreatorTask = new CreatorTask(this);
        if(!mIsLoaded && !mSorted ) {
            mCreatorTask.execute(mUrlStart);
        }else if(mSorted && !mIsLoaded){
            mCreatorTask.execute(mUrlStart + mUrlEnd);
        }

        if(mIsLoaded){
            mCreatorTask.execute(mNextUrl);
        }
        else {
            mCreatorList.clear();
            mCreatorAdapter.notifyDataSetChanged();
            preLast = 0;
        }
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}

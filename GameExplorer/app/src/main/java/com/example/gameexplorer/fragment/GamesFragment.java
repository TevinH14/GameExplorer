package com.example.gameexplorer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.GameDetailActivity;
import com.example.gameexplorer.adapter.GamesDisplayAdapter;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.networkHelper.GamesTask;

import java.util.ArrayList;

public class GamesFragment extends Fragment implements GamesTask.OnGamesFinished {
    public static final String GAME_DETAIL_EXTRA = "GAME_DETAIL_EXTRA";
    private static String mUrl;
    public static GamesFragment newInstance(String url) {
        mUrl = url;
        Bundle args = new Bundle();

        GamesFragment fragment = new GamesFragment();
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
        GamesTask gt = new GamesTask(this);
        gt.execute(mUrl);
    }

    @Override
    public void onGamesPost(final ArrayList<Games> games) {
        if(getView() != null && getContext() != null && getArguments() != null){
            ListView lv_games = getView().findViewById(R.id.lv_game_display);
            lv_games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent gameDetailIntent = new Intent(getContext(), GameDetailActivity.class);
                    gameDetailIntent.putExtra(GAME_DETAIL_EXTRA,games.get(position));
                    startActivity(gameDetailIntent);
                }
            });

            GamesDisplayAdapter ia = new GamesDisplayAdapter(getContext(),games);
            lv_games.setAdapter(ia);
        }
    }
}

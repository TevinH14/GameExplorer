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
import com.example.gameexplorer.firebaseHelper.RealTimeDatabaseHelper;
import com.example.gameexplorer.model.Games;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements RealTimeDatabaseHelper.FavoriteFinished {
    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_display,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new RealTimeDatabaseHelper(this);
        RealTimeDatabaseHelper.loadGame();
    }

    @Override
    public void onFavoritePost(final ArrayList<Games> _gamesList) {
        if(getView() != null) {
            ListView lv_games = getView().findViewById(R.id.lv_game_display);
            lv_games.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent gameDetailIntent = new Intent(getContext(), GameDetailActivity.class);
                    gameDetailIntent.putExtra(GameDetailActivity.GAME_DETAIL_EXTRA, _gamesList.get(position));
                    startActivity(gameDetailIntent);
                }
            });
            GamesDisplayAdapter ga = new GamesDisplayAdapter(getContext(), _gamesList);
            lv_games.setAdapter(ga);
        }
    }
}

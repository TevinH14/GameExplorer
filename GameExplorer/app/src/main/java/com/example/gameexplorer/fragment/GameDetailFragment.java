package com.example.gameexplorer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;
import com.example.gameexplorer.model.Games;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class GameDetailFragment extends Fragment {
    private static Games selectedGame;
    public static GameDetailFragment newInstance(Games game) {
        
        Bundle args = new Bundle();
        selectedGame = game;
        GameDetailFragment fragment = new GameDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_details,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null && selectedGame != null){
            TextView tv_title = getView().findViewById(R.id.tv_title_gameDetail);
            tv_title.setText(selectedGame.getTitle());
            ImageView iv_gameImage = getView().findViewById(R.id.iv_image_gameDetail);
            Picasso
                    .get()
                    .load(selectedGame.getBackgroundImage())
                    .resize(375,150)
                    .into(iv_gameImage,new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}

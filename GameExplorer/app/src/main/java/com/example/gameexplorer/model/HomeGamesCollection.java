package com.example.gameexplorer.model;

import java.util.ArrayList;

public class HomeGamesCollection {
    private ArrayList<String> mGameTitles;
    private ArrayList<String> mGameImageUrl;
    private ArrayList<Integer> mGameIds;

    public HomeGamesCollection(ArrayList<String> gameTitles,
                               ArrayList<String> gameImageUrl,
                               ArrayList<Integer> gameIds) {

        mGameTitles = gameTitles;
        mGameImageUrl = gameImageUrl;
        mGameIds = gameIds;
    }

    public ArrayList<String> getGameTitles() {
        return mGameTitles;
    }

    public void setGameTitles(ArrayList<String> mGameTitles) {
        this.mGameTitles = mGameTitles;
    }

    public ArrayList<String> getGameImageUrl() {
        return mGameImageUrl;
    }

    public void setGameImageUrl(ArrayList<String> mGameImageUrl) {
        this.mGameImageUrl = mGameImageUrl;
    }

    public ArrayList<Integer> getGameIds() {
        return mGameIds;
    }

    public void setGameIds(ArrayList<Integer> mGameIds) {
        this.mGameIds = mGameIds;
    }
}

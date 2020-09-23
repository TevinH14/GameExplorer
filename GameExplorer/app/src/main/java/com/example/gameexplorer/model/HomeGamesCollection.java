package com.example.gameexplorer.model;

import java.util.ArrayList;

public class HomeGamesCollection {
    private ArrayList<String> mGameTitles;
    private ArrayList<String> mGameImageUrl;
    private ArrayList<String> mGameSlug;

    public HomeGamesCollection(ArrayList<String> gameTitles,
                               ArrayList<String> gameImageUrl,
                               ArrayList<String> gameSlug) {

        mGameTitles = gameTitles;
        mGameImageUrl = gameImageUrl;
        mGameSlug = gameSlug;
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

    public ArrayList<String> getGameSlug() {
        return mGameSlug;
    }

    public void setGameIds(ArrayList<String> mGameIds) {
        this.mGameSlug = mGameIds;
    }
}

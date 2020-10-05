package com.example.gameexplorer.model;

import java.io.Serializable;

public class Developer implements Serializable {
    private int mId;
    private String mName;
    private String mImageUrl;
    private String mGameSlug[];
    private Games mGames[];

    public Developer(int mId, String mName, String mImageUrl, String[] mGameSlug) {
        this.mId = mId;
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.mGameSlug = mGameSlug;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String[] getGameSlug() {
        return mGameSlug;
    }

    public Games[] getGames() {
        return mGames;
    }

    public void setGames(Games[] mGames) {
        this.mGames = mGames;
    }
}

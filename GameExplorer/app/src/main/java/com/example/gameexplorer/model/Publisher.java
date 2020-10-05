package com.example.gameexplorer.model;

import java.io.Serializable;

public class Publisher implements Serializable {
    private int mId;
    private String mName;
    private String mImageUrl;
    private String mGameSlug[];

    private String mDescription;
    private Games mGames[];

    public Publisher(int mId, String mName, String mImageUrl, String[] mGameSlug) {
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Games[] getGames() {
        return mGames;
    }

    public void setGames(Games[] mGames) {
        this.mGames = mGames;
    }
}

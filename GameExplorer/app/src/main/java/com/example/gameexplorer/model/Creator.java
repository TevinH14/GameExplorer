package com.example.gameexplorer.model;

import java.io.Serializable;

public class Creator implements Serializable {
    private int mId;
    private String mName;
    private String mCreatorImage;
    private String mImageUrl;
    private int mGameCount;
    private String[] mGameSlug;
    private Games[] mGames;
    private String mDescription;

    public Creator(int mId, String mName, String mCreatorImage, String mImageUrl, int mGameCount, String[] mGameSlug) {
        this.mId = mId;
        this.mName = mName;
        this.mCreatorImage = mCreatorImage;
        this.mImageUrl = mImageUrl;
        this.mGameCount = mGameCount;
        this.mGameSlug = mGameSlug;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getCreatorImage() {
        return mCreatorImage;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public int getGameCount() {
        return mGameCount;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}

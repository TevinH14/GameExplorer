package com.example.gameexplorer.model;

public class GamePlatform {
    private int mId;
    private String mName;
    private String mImageUrl;

    public GamePlatform(int mId, String mName, String mImageUrl) {
        this.mId = mId;
        this.mName = mName;
        this.mImageUrl = mImageUrl;
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
}

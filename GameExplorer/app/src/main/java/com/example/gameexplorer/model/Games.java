package com.example.gameexplorer.model;

import java.io.Serializable;

public class Games implements Serializable {
    private String mTitle;
    private String mSlugName;
    private String mBackgroundImage;

    public Games(String mTitle, String mSlugName, String mBackgroundImage) {
        this.mTitle = mTitle;
        this.mSlugName = mSlugName;
        this.mBackgroundImage = mBackgroundImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSlugName() {
        return mSlugName;
    }

    public String getBackgroundImage() {
        return mBackgroundImage;
    }
}

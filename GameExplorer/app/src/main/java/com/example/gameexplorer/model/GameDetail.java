package com.example.gameexplorer.model;

public class GameDetail extends Games {
    private int mGameId;
    private int mCriticRating;
    private double mUserRating;
    private String mReleased;
    private String mUpdate;
    private String mWebsiteUrl;
    private String mRedditUrl;
    private String mMetacriticUrl;
    private String[] mPlatforms;
    private String[] mGenre;
    private String[] Tags;
    private String mDescription;
    private String[] mPublishers;
    private String[] mDevelopers;
    private String mEsrbRating;
    private String[] mStores;

    private String[] mScreenShotsUrl;

    private String[] mMoviesPreviews;
    private String[] mVideoUrl;

    private Games[] mSeriesGames;
    private Games[] mMoreGames;

    public GameDetail(String mTitle, String mSlugName, String mBackgroundImage, int mCriticRating,
                      double mUserRating, String mReleased, String mUpdate, String mWebsiteUrl,
                      String mRedditUrl, String mMetacriticUrl, String[] mPlatforms, String[] mGenre,
                      String[] tags, String mDescription, String[] mPublishers, String[] mDevelopers,
                      String mEsrbRating, String[] mStores) {

        super(mTitle, mSlugName, mBackgroundImage);
        this.mCriticRating = mCriticRating;
        this.mUserRating = mUserRating;
        this.mReleased = mReleased;
        this.mUpdate = mUpdate;
        this.mWebsiteUrl = mWebsiteUrl;
        this.mRedditUrl = mRedditUrl;
        this.mMetacriticUrl = mMetacriticUrl;
        this.mPlatforms = mPlatforms;
        this.mGenre = mGenre;
        Tags = tags;
        this.mDescription = mDescription;
        this.mPublishers = mPublishers;
        this.mDevelopers = mDevelopers;
        this.mEsrbRating = mEsrbRating;
        this.mStores = mStores;
    }

    //getters
    public int getCriticRating() {
        return mCriticRating;
    }
    public double getUserRating() {
        return mUserRating;
    }
    public String getReleased() {
        return mReleased;
    }
    public String getUpdate() {
        return mUpdate;
    }
    public String getWebsiteUrl() {
        return mWebsiteUrl;
    }
    public String getRedditUrl() {
        return mRedditUrl;
    }
    public String getMetacriticUrl() {
        return mMetacriticUrl;
    }
    public String[] getPlatforms() {
        return mPlatforms;
    }
    public String[] getGenre() {
        return mGenre;
    }
    public String[] getTags() {
        return Tags;
    }
    public String getDescription() {
        return mDescription;
    }
    public String[] getPublishers() {
        return mPublishers;
    }
    public String[] getDevelopers() {
        return mDevelopers;
    }
    public String getEsrbRating() {
        return mEsrbRating;
    }
    public String[] getStores() {
        return mStores;
    }
    public String[] getScreenShotsUrl() {
        return mScreenShotsUrl;
    }
    public String[] getVideoUrl() {
        return mVideoUrl;
    }
    public String[] getMoviesPreviews() {
        return mMoviesPreviews;
    }

    public Games[] getSeriesGames() {
        return mSeriesGames;
    }

    public void setSeriesGames(Games[] mSeriesGames) {
        this.mSeriesGames = mSeriesGames;
    }

    public Games[] getMoreGames() {
        return mMoreGames;
    }

    public void setMoreGames(Games[] mMoreGames) {
        this.mMoreGames = mMoreGames;
    }

    public int getGameId() {
        return mGameId;
    }

    public void setGameId(int mGameId) {
        this.mGameId = mGameId;
    }

    //setter
    public void setScreenShotsUrl(String[] mScreenShotsUrl) {
        this.mScreenShotsUrl = mScreenShotsUrl;
    }
    public void setVideoUrl(String[] mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }
    public void setMoviesPreviews(String[] mMoviesPreviews) {
        this.mMoviesPreviews = mMoviesPreviews;
    }
}

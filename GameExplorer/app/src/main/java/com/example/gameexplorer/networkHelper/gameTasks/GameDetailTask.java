package com.example.gameexplorer.networkHelper.gameTasks;

import android.os.AsyncTask;

import com.example.gameexplorer.model.GameDetail;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameDetailTask extends AsyncTask<String,Void, GameDetail> {

    final private OnGamesFinished mOnFinishedInterface;

    public interface OnGamesFinished{
        void onGameDetailPost(GameDetail games);
    }
    public GameDetailTask(OnGamesFinished mOnFinishedInterface) {
        this.mOnFinishedInterface = mOnFinishedInterface;
    }

    @Override
    protected GameDetail doInBackground(String... strings) {
        if(strings.length > 0) {
            String urlString = strings[0];
            if (urlString != null && !urlString.matches("")) {
                String data = NetworkUtils.getNetworkData(urlString);
                GameDetail gd = null;
                try {
                    JSONObject obj = new JSONObject(data);


                    String slug = obj.getString("slug");
                    String title = obj.getString("name");
                    int mRating = -1;
                    if(!obj.isNull("metacritic")){
                        mRating = obj.getInt("metacritic");
                    }
                    String image = obj.getString("background_image");
                    double uRating = obj.getDouble("rating");
                    String released = obj.getString("released");
                    String update = obj.getString("updated");
                    String webUrl = obj.getString("website");
                    String redditUrl = obj.getString("reddit_url");
                    String metacriticUrl = obj.getString("metacritic_url");
                    JSONArray platformJsonArray = obj.getJSONArray("platforms");
                    String[] platforms = new String[platformJsonArray.length()];
                    for (int i = 0; i < platformJsonArray.length() ; i++) {
                        JSONObject objArray = platformJsonArray.getJSONObject(i);
                        JSONObject platformObj  = objArray.getJSONObject("platform");
                        platforms[i] = platformObj.getString("name");
                    }
                    JSONArray genreJsonArray = obj.getJSONArray("genres");
                    String[] genres = new String[genreJsonArray.length()];
                    for (int i = 0; i < genreJsonArray.length() ; i++) {
                        JSONObject objArray = genreJsonArray.getJSONObject(i);
                        genres[i] = objArray.getString("name");
                    }

                    JSONArray tagJsonArray = obj.getJSONArray("tags");
                    String[] tags = new String[tagJsonArray.length()];
                    for (int i = 0; i < tagJsonArray.length() ; i++) {
                        JSONObject objArray = tagJsonArray.getJSONObject(i);
                        tags[i] = objArray.getString("name");
                    }
                    String description = obj.getString("description_raw");

                    JSONArray publishersJsonArray = obj.getJSONArray("publishers");
                    String[] publishers = new String[publishersJsonArray.length()];
                    for (int i = 0; i < publishersJsonArray.length() ; i++) {
                        JSONObject objArray = publishersJsonArray.getJSONObject(i);
                        publishers[i] = objArray.getString("name");
                    }

                    JSONArray developerJsonArray = obj.getJSONArray("developers");
                    String[] developers = new String[developerJsonArray.length()];
                    for (int i = 0; i < developerJsonArray.length() ; i++) {
                        JSONObject objArray = developerJsonArray.getJSONObject(i);
                        developers[i] = objArray.getString("name");
                    }

                    String esrb = obj.getString("esrb_rating");

                    JSONArray storesJsonArray = obj.getJSONArray("stores");
                    String[] stores = new String[storesJsonArray.length()];
                    for (int i = 0; i < storesJsonArray.length() ; i++) {
                        JSONObject objArray = storesJsonArray.getJSONObject(i);
                        stores[i] = objArray.getString("url");
                    }

                    gd = new GameDetail(title,slug,image,mRating,uRating,released,update,webUrl,
                            redditUrl,metacriticUrl,platforms,genres,tags,description,publishers,
                            developers,esrb,stores);

                    int id = obj.getInt("id");
                    gd.setGameId(id);

                    data = NetworkUtils.getNetworkData(urlString+NetworkUtils
                            .SCREENSHOT_END_POINT);
                    JSONObject objScreenShots = new JSONObject(data);
                    JSONArray imageArrayObj = objScreenShots.getJSONArray("results");
                    String[] imageArray = new String[imageArrayObj.length()];
                    for (int i = 0; i <imageArrayObj.length() ; i++) {
                        JSONObject objArray = imageArrayObj.getJSONObject(i);
                        imageArray[i] = objArray.getString("image");
                    }
                    gd.setScreenShotsUrl(imageArray);

                    data = NetworkUtils.getNetworkData(urlString+NetworkUtils
                            .MOVIES_END_POINT);
                    JSONObject objMovie = new JSONObject(data);
                    JSONArray movieArrayObj = objMovie.getJSONArray("results");
                    if(movieArrayObj.length() > 0) {
                        String[] movies = new String[movieArrayObj.length()];
                        String[] moviePreviews = new String[movieArrayObj.length()];
                        for (int i = 0; i < movieArrayObj.length(); i++) {
                            JSONObject objArray = movieArrayObj.getJSONObject(i);
                            moviePreviews[i] = objArray.getString("preview");
                            JSONObject dataObj = objArray.getJSONObject("data");
                            movies[i] = dataObj.getString("480");
                        }
                        gd.setVideoUrl(movies);
                        gd.setMoviesPreviews(moviePreviews);
                    }

                    data = NetworkUtils.getNetworkData(urlString+NetworkUtils
                            .SERIES_END_POINT);
                    JSONObject objectGames = new JSONObject(data);
                    JSONArray resultsArray = objectGames.getJSONArray("results");
                    int length = 5;
                    if (resultsArray.length() < 5){
                        length = resultsArray.length();
                    }
                    else {
                        length = 5;
                    }

                    Games[] gamesSeries = new Games[length];
                    for (int j = 0; j < length; j++) {
                        JSONObject seriesObj = resultsArray.getJSONObject(j);
                        String t = seriesObj.getString("name");
                        String s = seriesObj.getString("slug");
                        String i= seriesObj.getString("background_image");
                        gamesSeries[j] = new Games(t,s,i);
                    }
                    gd.setSeriesGames(gamesSeries);

                    data = NetworkUtils.getNetworkData(urlString+NetworkUtils
                            .SUGGESTED_END_POINT);
                    JSONObject objectMore = new JSONObject(data);
                    JSONArray resultsMore = objectMore.getJSONArray("results");
                     length = 5;
                    if (resultsMore.length() < 5){
                        length = resultsMore.length();
                    }
                    else {
                        length = 5;
                    }

                    Games[] gamesMore = new Games[length];
                    for (int j = 0; j < length; j++) {
                        JSONObject moreObj = resultsMore.getJSONObject(j);
                        String t = moreObj.getString("name");
                        String s = moreObj.getString("slug");
                        String i= moreObj.getString("background_image");
                        gamesMore[j] = new Games(t,s,i);
                    }
                    gd.setMoreGames(gamesMore);



                }catch (JSONException e){
                    e.printStackTrace();
                }
                return gd;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(GameDetail games) {
        mOnFinishedInterface.onGameDetailPost(games);
    }
}

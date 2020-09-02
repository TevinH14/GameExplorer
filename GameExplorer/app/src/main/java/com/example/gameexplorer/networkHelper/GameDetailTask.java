package com.example.gameexplorer.networkHelper;

import android.os.AsyncTask;

import com.example.gameexplorer.model.GameDetail;
import com.example.gameexplorer.model.Games;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameDetailTask extends AsyncTask<String,Void, GameDetail> {

    final private OnGamesFinished mOnFinishedInterface;

    public GameDetailTask(OnGamesFinished mOnFinishedInterface) {
        this.mOnFinishedInterface = mOnFinishedInterface;
    }
    public interface OnGamesFinished{
        void onGameDetailPost(GameDetail games);
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
                    int mRating = obj.getInt("metacritic");
                    String image = obj.getString("background_image");
                    double uRating = obj.getDouble("rating");
                    String released = obj.getString("released");
                    String update = obj.getString("update");
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
                        genres[i] = objArray.getString("url");
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

                    JSONArray storesJsonArray = obj.getJSONArray("Stores");
                    String[] stores = new String[storesJsonArray.length()];
                    for (int i = 0; i < storesJsonArray.length() ; i++) {
                        JSONObject objArray = storesJsonArray.getJSONObject(i);
                        stores[i] = objArray.getString("name");
                    }

                    gd = new GameDetail(title,slug,image,mRating,uRating,released,update,webUrl,
                            redditUrl,metacriticUrl,platforms,genres,tags,description,publishers,
                            developers,esrb,stores);
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

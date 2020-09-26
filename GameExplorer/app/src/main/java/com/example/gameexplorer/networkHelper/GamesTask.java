package com.example.gameexplorer.networkHelper;

import android.os.AsyncTask;

import com.example.gameexplorer.model.Games;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GamesTask extends AsyncTask<String,Void,ArrayList<Games>> {

    final private OnGamesFinished mOnGameFinishedInterface;

    private String mNextUrl;

    public String getNextUrl() {
        return mNextUrl;
    }

    public GamesTask(OnGamesFinished mOnFinishedInterface) {
        this.mOnGameFinishedInterface = mOnFinishedInterface;
    }

    public interface OnGamesFinished{
        void onGamesPost(ArrayList<Games> games);
    }
    @Override
    protected ArrayList<Games> doInBackground(String... strings) {
        if(strings.length > 0) {
            String urlString = strings[0];
            if (urlString != null && !urlString.matches("")) {
                ArrayList<Games> gamesList = getGames(urlString);
                //return arrayList
//                if(gamesList.size() != 0) {
                    while (gamesList.size() == 0) {
                        gamesList = getGames(mNextUrl);
                    }
//                }
                return gamesList;
            }
        }
        return null;
    }
 private ArrayList<Games> getGames(String urlString){
     String data = NetworkUtils.getNetworkData(urlString);
     ArrayList<Games> gamesList = new ArrayList<>();
     try {
         JSONObject object = new JSONObject(data);
         mNextUrl = object.getString("next");
         JSONArray resultsArray = object.getJSONArray("results");
         for (int i = 0; i <resultsArray.length() ; i++) {
             JSONObject obj = resultsArray.getJSONObject(i);
             String title = obj.getString("name");
             String slugName = obj.getString("slug");
             String imageUrl = obj.getString("background_image");
             int playtime = obj.getInt("playtime");
             if(playtime >0) {
                 gamesList.add(new Games(title, slugName, imageUrl));
             }
         }
     }catch (JSONException e){
         e.printStackTrace();
     }
     return gamesList;
 }
    @Override
    protected void onPostExecute(ArrayList<Games> games) {
        mOnGameFinishedInterface.onGamesPost(games);
    }
}

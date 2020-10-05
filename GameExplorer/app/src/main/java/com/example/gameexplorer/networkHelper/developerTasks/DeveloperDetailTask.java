package com.example.gameexplorer.networkHelper.developerTasks;

import android.os.AsyncTask;

import com.example.gameexplorer.model.Developer;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeveloperDetailTask extends AsyncTask<Developer,Void,Developer> {
    final private OnDeveloperFinishedFinished mOnFinishedInterface;

    public interface OnDeveloperFinishedFinished{
        void onDeveloperDetailPost(Developer developer);
    }

    public DeveloperDetailTask(OnDeveloperFinishedFinished mOnFinishedInterface) {
        this.mOnFinishedInterface = mOnFinishedInterface;
    }

    @Override
    protected Developer doInBackground(Developer... developers) {
        if (developers.length > 0){
            Developer dev = developers[0];
            if(dev != null){
                String data = NetworkUtils.getNetworkData(
                        "https://api.rawg.io/api/games?developers="
                                + dev.getId());
                try {
                    JSONObject objectGames = new JSONObject(data);
                    JSONArray resultsArray = objectGames.getJSONArray("results");
                    Games[] games = new Games[6];
                    String[] slugsArray = dev.getGameSlug();
                    for (int j = 0; j < 6; j++) {
                        JSONObject obj = resultsArray.getJSONObject(j);
                        String title = obj.getString("name");
                        String imageUrl = obj.getString("background_image");
                        games[j] = new Games(title,slugsArray[j],imageUrl);
                    }
                    dev.setGames(games);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                return dev;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Developer developer) {
        mOnFinishedInterface.onDeveloperDetailPost(developer);
    }
}

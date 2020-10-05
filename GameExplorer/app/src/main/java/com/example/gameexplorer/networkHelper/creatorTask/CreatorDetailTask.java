package com.example.gameexplorer.networkHelper.creatorTask;

import android.os.AsyncTask;

import com.example.gameexplorer.model.Creator;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreatorDetailTask extends AsyncTask<Creator,Void, Creator> {
    final private OnCreatorFinished mOnFinishedInterface;

    public interface OnCreatorFinished{
        void onCreatorDetailPost(Creator creator);
    }

    public CreatorDetailTask(OnCreatorFinished mOnFinishedInterface) {
        this.mOnFinishedInterface = mOnFinishedInterface;
    }
    @Override
    protected Creator doInBackground(Creator... creators) {
        if (creators.length > 0){
            Creator c = creators[0];
            if(c != null){
                String data = NetworkUtils.getNetworkData(
                        NetworkUtils.getCreatorsUrl()
                                + "/"
                                + c.getId());
                try {
                    JSONObject object = new JSONObject(data);
                    String description = object.getString("description");
                    c.setDescription(description);
                    data = NetworkUtils.getNetworkData(
                            "https://api.rawg.io/api/games?creators="
                                    + c.getId());
                    JSONObject objectGames = new JSONObject(data);
                    JSONArray resultsArray = objectGames.getJSONArray("results");
                    Games[] games = new Games[6];
                    String[] slugsArray = c.getGameSlug();
                    for (int j = 0; j < 6; j++) {
                        JSONObject obj = resultsArray.getJSONObject(j);
                        String title = obj.getString("name");
                        String imageUrl = obj.getString("background_image");
                        games[j] = new Games(title,slugsArray[j],imageUrl);
                    }
                    c.setGames(games);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                return c;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Creator creator) {
        mOnFinishedInterface.onCreatorDetailPost(creator);
    }
}

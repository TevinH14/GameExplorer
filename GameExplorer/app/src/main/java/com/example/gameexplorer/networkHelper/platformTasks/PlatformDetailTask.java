package com.example.gameexplorer.networkHelper.platformTasks;

import android.os.AsyncTask;

import com.example.gameexplorer.model.GamePlatform;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PlatformDetailTask extends AsyncTask<GamePlatform,Void, GamePlatform> {
    final private OnPlatformFinished mOnFinishedInterface;

    public interface OnPlatformFinished{
        void onPlatformDetailPost(GamePlatform platform);
    }

    public PlatformDetailTask(OnPlatformFinished mOnFinishedInterface) {
        this.mOnFinishedInterface = mOnFinishedInterface;
    }

    @Override
    protected GamePlatform doInBackground(GamePlatform... gamePlatforms) {
        if (gamePlatforms.length > 0){
            GamePlatform gp = gamePlatforms[0];
            if(gp != null){
                String data = NetworkUtils.getNetworkData(
                        NetworkUtils.getPlatformUrl()
                                + "/"
                                + gp.getId());
                try {
                    JSONObject object = new JSONObject(data);
                    String description = object.getString("description");
                    gp.setDescription(description);
                    data = NetworkUtils.getNetworkData(
                            "https://api.rawg.io/api/games?platforms="
                                    + gp.getId());
                    JSONObject objectGames = new JSONObject(data);
                    JSONArray resultsArray = objectGames.getJSONArray("results");
                    Games[] games = new Games[6];
                    String[] slugsArray = gp.getGameSlug();
                    for (int j = 0; j < 6; j++) {
                        JSONObject obj = resultsArray.getJSONObject(j);
                        String title = obj.getString("name");
                        String imageUrl = obj.getString("background_image");
                        games[j] = new Games(title,slugsArray[j],imageUrl);
                    }
                    gp.setGames(games);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                return gp;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(GamePlatform platform) {
        mOnFinishedInterface.onPlatformDetailPost(platform);
    }
}

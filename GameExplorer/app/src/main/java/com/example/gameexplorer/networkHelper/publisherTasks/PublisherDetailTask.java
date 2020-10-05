package com.example.gameexplorer.networkHelper.publisherTasks;

import android.os.AsyncTask;

import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.model.Publisher;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PublisherDetailTask extends AsyncTask<Publisher,Void, Publisher> {

    final private OnPublisherFinishedFinished mOnFinishedInterface;

    public interface OnPublisherFinishedFinished{
        void onPublisherDetailPost(Publisher publisher);
    }

    public PublisherDetailTask(OnPublisherFinishedFinished mOnFinishedInterface) {
        this.mOnFinishedInterface = mOnFinishedInterface;
    }

    @Override
    protected Publisher doInBackground(Publisher... publishers) {
        if (publishers.length > 0){
            Publisher pub = publishers[0];
            if(pub != null){
                String data = NetworkUtils.getNetworkData(
                        NetworkUtils.getPublishersUrl()
                                + "/"
                                + pub.getId());
                try {
                    JSONObject object = new JSONObject(data);
                    String description = object.getString("description");
                    pub.setDescription(description);
                    data = NetworkUtils.getNetworkData(
                            "https://api.rawg.io/api/games?publishers="
                                    + pub.getId());
                    JSONObject objectGames = new JSONObject(data);
                    JSONArray resultsArray = objectGames.getJSONArray("results");
                    Games[] games = new Games[6];
                    String[] slugsArray = pub.getGameSlug();
                    for (int j = 0; j < 6; j++) {
                        JSONObject obj = resultsArray.getJSONObject(j);
                        String title = obj.getString("name");
                        String imageUrl = obj.getString("background_image");
                        games[j] = new Games(title,slugsArray[j],imageUrl);
                    }
                    pub.setGames(games);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                return pub;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Publisher publisher) {
        mOnFinishedInterface.onPublisherDetailPost(publisher);
    }
}

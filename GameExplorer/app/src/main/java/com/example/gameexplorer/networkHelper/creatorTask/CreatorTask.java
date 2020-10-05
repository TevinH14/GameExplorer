package com.example.gameexplorer.networkHelper.creatorTask;

import android.os.AsyncTask;

import com.example.gameexplorer.model.Creator;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreatorTask extends AsyncTask<String,Void, ArrayList<Creator>> {

    final private OnCreatorFinished mOnCreatorFinished;
    private String mNextUrl;

    public String getNextUrl() {
        return mNextUrl;
    }

    public interface OnCreatorFinished{
        void onCreatorPost(ArrayList<Creator> _creators);
    }

    public CreatorTask(OnCreatorFinished mOnCreatorFinished) {
        this.mOnCreatorFinished = mOnCreatorFinished;
    }

    @Override
    protected ArrayList<Creator> doInBackground(String... strings) {
        if(strings.length > 0) {
            String urlString = strings[0];
            if (urlString != null && !urlString.matches("")) {
                String data = NetworkUtils.getNetworkData(urlString);
                ArrayList<Creator> creatorsList = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(data);
                    mNextUrl = object.getString("next");
                    JSONArray resultsArray = object.getJSONArray("results");
                    for (int i = 0; i <resultsArray.length() ; i++) {
                        JSONObject obj = resultsArray.getJSONObject(i);

                        int id = obj.getInt("id");
                        String name = obj.getString("name");
                        String creatorImage = obj.getString("image");
                        String imageUrl = obj.getString("image_background");
                        JSONArray gamesJsonArray = obj.getJSONArray("games");
                        int gameCount = obj.getInt("games_count");
                        String[] gamesSlugs = new String[gamesJsonArray.length()];
                        for (int j = 0; j < gamesJsonArray.length() ; j++) {
                            JSONObject objArray = gamesJsonArray.getJSONObject(j);
                            gamesSlugs[j] = objArray.getString("slug");
                        }
                        creatorsList.add(new
                                Creator(id,name,creatorImage,imageUrl,gameCount,gamesSlugs));

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                return creatorsList;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Creator> creators) {
        mOnCreatorFinished.onCreatorPost(creators);
    }
}

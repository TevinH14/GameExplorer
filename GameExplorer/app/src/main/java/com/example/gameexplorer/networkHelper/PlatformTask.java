package com.example.gameexplorer.networkHelper;

import android.os.AsyncTask;

import com.example.gameexplorer.model.GamePlatform;
import com.example.gameexplorer.model.Games;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlatformTask extends AsyncTask<String,Void, ArrayList<GamePlatform>> {

    final private OnPlatformFinished mOnPlatformFinished;
    private String mNextUrl;

    public String getNextUrl() {
        return mNextUrl;
    }

    public PlatformTask(OnPlatformFinished mOnPlatformFinished) {
        this.mOnPlatformFinished = mOnPlatformFinished;
    }

    public interface OnPlatformFinished{
        void onPlatformPost(ArrayList<GamePlatform> platforms);
    }

    @Override
    protected ArrayList<GamePlatform> doInBackground(String... strings) {
        if(strings.length > 0) {
            String urlString = strings[0];
            if (urlString != null && !urlString.matches("")) {
                String data = NetworkUtils.getNetworkData(urlString);
                ArrayList<GamePlatform> platformList = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(data);
                    mNextUrl = object.getString("next");
                    JSONArray resultsArray = object.getJSONArray("results");
                    for (int i = 0; i <resultsArray.length() ; i++) {
                        JSONObject obj = resultsArray.getJSONObject(i);

                        int id = obj.getInt("id");
                        String name = obj.getString("name");
                        String imageUrl = obj.getString("image_background");

                        platformList.add(new GamePlatform(id, name, imageUrl));

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                return platformList;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<GamePlatform> platforms) {
        mOnPlatformFinished.onPlatformPost(platforms);
    }
}

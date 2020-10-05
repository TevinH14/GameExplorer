package com.example.gameexplorer.networkHelper.developerTasks;

import android.os.AsyncTask;

import com.example.gameexplorer.model.Developer;
import com.example.gameexplorer.model.Publisher;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeveloperTask  extends AsyncTask<String,Void, ArrayList<Developer>> {

    final private OnDeveloperFinished mOnDeveloperFinished;
    private String mNextUrl;

    public String getNextUrl() {
        return mNextUrl;
    }

    public DeveloperTask(OnDeveloperFinished mOnDeveloperFinished) {
        this.mOnDeveloperFinished = mOnDeveloperFinished;
    }

    public interface OnDeveloperFinished {
        void onDeveloperPost(ArrayList<Developer> developers);
    }

    @Override
    protected ArrayList<Developer> doInBackground(String... strings) {
        if (strings.length > 0) {
            String urlString = strings[0];
            if (urlString != null && !urlString.matches("")) {
                String data = NetworkUtils.getNetworkData(urlString);
                ArrayList<Developer> developerList = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(data);
                    mNextUrl = object.getString("next");
                    JSONArray resultsArray = object.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject obj = resultsArray.getJSONObject(i);

                        int id = obj.getInt("id");
                        String name = obj.getString("name");
                        String imageUrl = obj.getString("image_background");
                        JSONArray gamesJsonArray = obj.getJSONArray("games");
                        String[] gamesSlugs = new String[gamesJsonArray.length()];
                        for (int j = 0; j < gamesJsonArray.length() ; j++) {
                            JSONObject objArray = gamesJsonArray.getJSONObject(j);
                            gamesSlugs[j] = objArray.getString("slug");
                        }

                        developerList.add(new Developer(id, name, imageUrl,gamesSlugs));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return developerList;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Developer> developers) {
        mOnDeveloperFinished.onDeveloperPost(developers);
    }
}

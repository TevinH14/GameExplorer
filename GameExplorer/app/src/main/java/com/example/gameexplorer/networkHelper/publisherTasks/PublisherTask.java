package com.example.gameexplorer.networkHelper.publisherTasks;

import android.os.AsyncTask;

import com.example.gameexplorer.model.Publisher;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PublisherTask extends AsyncTask<String,Void, ArrayList<Publisher>> {

    final private OnPublisherFinished mOnPublisherFinished;
    private String mNextUrl;

    public String getNextUrl() {
        return mNextUrl;
    }

    public PublisherTask(OnPublisherFinished mOnPublisherFinished) {
        this.mOnPublisherFinished = mOnPublisherFinished;
    }

    public interface OnPublisherFinished {
        void onPublisherPost(ArrayList<Publisher> platforms);
    }

    @Override
    protected ArrayList<Publisher> doInBackground(String... strings) {
        if (strings.length > 0) {
            String urlString = strings[0];
            if (urlString != null && !urlString.matches("")) {
                String data = NetworkUtils.getNetworkData(urlString);
                ArrayList<Publisher> platformList = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(data);
                    mNextUrl = object.getString("next");
                    JSONArray resultsArray = object.getJSONArray("results");
                    for (int i = 0; i <resultsArray.length() ; i++) {
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
                        platformList.add(new Publisher(id, name, imageUrl,gamesSlugs));

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
    protected void onPostExecute(ArrayList<Publisher> _publishers) {
        mOnPublisherFinished.onPublisherPost(_publishers);
    }
}

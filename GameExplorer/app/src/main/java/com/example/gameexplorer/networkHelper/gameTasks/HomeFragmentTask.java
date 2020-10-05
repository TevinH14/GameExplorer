package com.example.gameexplorer.networkHelper.gameTasks;

import android.os.AsyncTask;

import com.example.gameexplorer.model.HomeGamesCollection;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragmentTask extends AsyncTask<Void, Void, HashMap<Integer, ArrayList<HomeGamesCollection>>> {
	private String[] mCategoryStringArray;
	private static String mTopYear;

	public static String getTopYear() {
		return mTopYear;
	}

	public String[] getCategoryStringArray() {
		return mCategoryStringArray;
	}

	final private OnDetailFinished mOnFinishedInterface;

	public HomeFragmentTask(OnDetailFinished onFinished) {
		mOnFinishedInterface = onFinished;
	}

	public interface OnDetailFinished{
		void onGamePre();
		void onGamePost(HashMap<Integer, ArrayList<HomeGamesCollection>> _retValues);
	}


	@Override
	protected HashMap<Integer, ArrayList<HomeGamesCollection>> doInBackground(Void... voids) {
		// Add member ID to the end of the URL
		mCategoryStringArray = new String[4];
		String recentlyReleased =
				NetworkUtils.getNetworkData(
						NetworkUtils.getRecentlyUrl()+NetworkUtils.getPageLimit());
		mCategoryStringArray[0] = recentlyReleased;

		String upcomingRelease =
				NetworkUtils.getNetworkData(
						NetworkUtils.getUpcomingUrl()+NetworkUtils.getPageLimit());
		mCategoryStringArray[1] = upcomingRelease;

		String mostPopular =
				NetworkUtils.getNetworkData(
						NetworkUtils.getPopularGames()+NetworkUtils.getPageLimit());
		mCategoryStringArray[2] = mostPopular;
		mTopYear = NetworkUtils.getTopRatedYear();
		String topRated =
				NetworkUtils.getNetworkData(
						getTopYear()+NetworkUtils.getPageLimit());
		mCategoryStringArray[3] = topRated;

		HashMap<Integer,  ArrayList<HomeGamesCollection>> gameValues = new HashMap<>();

		try {
			for (int i = 0; i < mCategoryStringArray.length; i++) {
				ArrayList<String> gameSlugs = new ArrayList<>();
				ArrayList<String>  gameTitles = new ArrayList<>();
				ArrayList<String>  gamesImageUrls = new ArrayList<>();
				ArrayList<HomeGamesCollection>  categoryCollection = new ArrayList<>();

				JSONObject response = new JSONObject(mCategoryStringArray[i]);
				JSONArray resultsArray = response.getJSONArray("results");

				for (int j = 0; j < resultsArray.length(); j++) {
					JSONObject obj = resultsArray.getJSONObject(j);
					String slug= obj.getString("slug");
					String title = obj.getString("name");
					String imageUrl = obj.getString("background_image");
					gameSlugs.add(slug);
					gameTitles.add(title);
					gamesImageUrls.add(imageUrl);
				}
				categoryCollection.add(new HomeGamesCollection(gameTitles,gamesImageUrls,gameSlugs));
				gameValues.put(i,categoryCollection);
			}

		} catch(JSONException e) {
			e.printStackTrace();
		}



		return gameValues;
	}

	@Override
	protected void onPreExecute() {
		mOnFinishedInterface.onGamePre();
	}


	@Override
	protected void onPostExecute(HashMap<Integer, ArrayList<HomeGamesCollection>> _result) {
		mOnFinishedInterface.onGamePost(_result);
	}
}
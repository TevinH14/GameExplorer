package com.example.gameexplorer.networkHelper;

import android.os.AsyncTask;

import com.example.gameexplorer.fragment.HomeFragment;
import com.example.gameexplorer.model.HomeGamesCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class HomeFragmentTask extends AsyncTask<Void, Void, HashMap<Integer, ArrayList<HomeGamesCollection>>> {
	private String[] mCategoryStringArray;
	private String mYear;

	public String getYear() {
		return mYear;
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
		String recentlyReleased = NetworkUtils.getNetworkData(getRecentlyUrl());
		mCategoryStringArray[0] = recentlyReleased;
		String upcomingRelease = NetworkUtils.getNetworkData(getUpcomingUrl());
		mCategoryStringArray[1] = upcomingRelease;
		String mostPopular = NetworkUtils.getNetworkData(getPopularGames());
		mCategoryStringArray[2] = mostPopular;
		String topRated = NetworkUtils.getNetworkData(getTopRatedYear());
		mCategoryStringArray[3] = topRated;


		HashMap<Integer,  ArrayList<HomeGamesCollection>> gameValues = new HashMap<>();

		try {
			for (int i = 0; i < mCategoryStringArray.length; i++) {
				ArrayList<Integer> gameIds = new ArrayList<>();
				ArrayList<String>  gameTitles = new ArrayList<>();
				ArrayList<String>  gamesImageUrls = new ArrayList<>();
				ArrayList<HomeGamesCollection>  categoryCollection = new ArrayList<>();

				JSONObject response = new JSONObject(mCategoryStringArray[i]);
				JSONArray resultsArray = response.getJSONArray("results");

				for (int j = 0; j < resultsArray.length(); j++) {
					JSONObject obj = resultsArray.getJSONObject(j);
					int id = obj.getInt("id");
					String title = obj.getString("name");
					String imageUrl = obj.getString("background_image");
					gameIds.add(id);
					gameTitles.add(title);
					gamesImageUrls.add(imageUrl);
				}
				categoryCollection.add(new HomeGamesCollection(gameTitles,gamesImageUrls,gameIds));
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

	private String getCurrentDate(){
		//get today's date
		return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
	}

	private String getRecentlyUrl(){
		// get date for a month ago
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_YEAR, -30);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date resultDate = c1.getTime();
		String pastDate = dateFormat.format(resultDate);

		return "https://api.rawg.io/api/games?dates="
				+pastDate
				+","
				+getCurrentDate()
				+"&ordering=released";

	}

	private String getUpcomingUrl(){Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_YEAR, 365);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date resultDate = c1.getTime();
		String nextYearDate = dateFormat.format(resultDate);
		return "https://api.rawg.io/api/games?dates=" +
				getCurrentDate() +
				"," +
				nextYearDate +
				"&ordering=released";
	}

	private String getTopRatedYear(){
		final int min = 1995;
		final int max = 2019;
		final int randomYear = new Random().nextInt((max - min) + 1) + min;
		mYear = String.valueOf(randomYear);
		return  "https://api.rawg.io/api/games?dates=" +
				randomYear +
				"-01-01" +
				"," +
				randomYear +
				"-12-31" +
				"&ordering=-rating";
	}

	private String getPopularGames(){
		int yearNum = Calendar.getInstance().get(Calendar.YEAR);
		String yearString = String.valueOf(yearNum);
		return "https://api.rawg.io/api/games?dates=" +
				yearString +
				"-01-01," +
				yearString +
				"-12-31&ordering=-added";

	}
}
package com.example.gameexplorer.networkHelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class NetworkUtils {

	public static final String MOVIES_END_POINT = "/movies";
	public static final String SUGGESTED_END_POINT = "/suggested";
	public static final String SCREENSHOT_END_POINT = "/screenshots";
	public static final String SERIES_END_POINT = "/game-series";


	private static String mRandomYear;

	public static String getmRandomYear() {
		return mRandomYear;
	}

	public static boolean isConnected(Context _context) {
		
		ConnectivityManager mgr = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if(mgr != null) {
			NetworkInfo info = mgr.getActiveNetworkInfo();

			if(info != null) {
				return info.isConnected();
			}
		}
		return false;
	}
	
	public static String getNetworkData(String _url) {

		if(_url == null){
			return null;
		}

		HttpURLConnection connection = null;
		String data = null;
		URL url;

		try {
			url = new URL(_url);

			connection = (HttpURLConnection)url.openConnection();

			connection.connect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		InputStream is = null;
		try{
			if(connection != null){
				is = connection.getInputStream();
				data = IOUtils.toString(is,"UTF-8");
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
			if(connection != null){
				if(is != null){
					try{
						is.close();
					}catch(Exception e){
						e.printStackTrace();
					}
					connection.disconnect();

				}
			}
		}
		
		return data;
	}

	public static String getCurrentDate(){
		//get today's date
		return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
	}

	public static String getRecentlyUrl(){
		// get date for a month ago
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_YEAR, -30);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date resultDate = c1.getTime();
		String pastDate = dateFormat.format(resultDate);

		return "https://api.rawg.io/api/games?dates="
				+pastDate
				+","
				+getCurrentDate();

	}

	public static String getUpcomingUrl(){Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_YEAR, 365);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date resultDate = c1.getTime();
		String nextYearDate = dateFormat.format(resultDate);
		return "https://api.rawg.io/api/games?dates=" +
				getCurrentDate() +
				"," +
				nextYearDate;
	}

	public static String getTopRatedYear(){
		final int min = 1995;
		final int max = 2019;
		final int randomYear = new Random().nextInt((max - min) + 1) + min;
		mRandomYear = String.valueOf(randomYear);
		return  "https://api.rawg.io/api/games?dates=" +
				randomYear +
				"-01-01" +
				"," +
				randomYear +
				"-12-31";
	}

	public static String getPopularGames(){
		int yearNum = Calendar.getInstance().get(Calendar.YEAR);
		String yearString = String.valueOf(yearNum);
		return "https://api.rawg.io/api/games?dates=" +
				yearString +
				"-01-01," +
				yearString +
				"-12-31";

	}

	public static String getPageLimit(){
		return "&page_size=5";
	}

	public static String getGameUrl(){
		return "https://api.rawg.io/api/games";
	}

	public static String getPlatformUrl(){
		return "https://api.rawg.io/api/platforms";
	}

	public static String getPublishersUrl(){
		return "https://api.rawg.io/api/publishers";
	}

	public static String getDevelopersUrl(){
		return "https://api.rawg.io/api/developers";
	}

	public static String getCreatorsUrl(){
		return "https://api.rawg.io/api/creators";
	}


}
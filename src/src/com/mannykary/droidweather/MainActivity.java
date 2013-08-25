package com.mannykary.droidweather;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;
import com.mannykary.droidweather.ParseWU.Conditions;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	// TODO clean up UI

	HttpClient client;
	String currentConditions;
	String query;
	String units;

	public static final String API_KEY = "86509accc78ae0a6";
	public static final String PREFS_NAME = "MyPrefsFile";

	static final String CURRENT_QUERY = "currentQuery";
	static final String CURRENT_UNITS = "currentUnits";

	Conditions cName;
	HashMap<String, String> data = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			query = savedInstanceState.getString(CURRENT_QUERY);
			units = savedInstanceState.getString(CURRENT_UNITS);
		} else {

			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

			Intent intent = getIntent();

			if (intent.getStringExtra("droidweather.searchactivity.url") != null) {
				query = intent
						.getStringExtra("droidweather.searchactivity.url")
						+ ".json";
			} else {
				query = settings.getString(CURRENT_QUERY,
						"/q/CA/San_Diego.json");
			}

			units = settings.getString(CURRENT_UNITS, "metric");
		}

		setContentView(R.layout.activity_main);

		Log.i(MainActivity.class.getName(), "query: " + query);

		String URLConditions = "http://api.wunderground.com/api/" + API_KEY
				+ "/conditions" + query;
		String URLAstronomy = "http://api.wunderground.com/api/" + API_KEY
				+ "/astronomy" + query;
		String URLForecast = "http://api.wunderground.com/api/" + API_KEY
				+ "/forecast10day" + query;
		
		String URLForecastHourly = "http://api.wunderground.com/api/" + API_KEY
				+ "/hourly10day" + query;

		String JSONRequestCond = null;
		try {
			JSONRequestCond = new JSONReaderTask().execute(URLConditions).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String JSONRequestAstronomy = null;
		try {
			JSONRequestAstronomy = new JSONReaderTask().execute(URLAstronomy)
					.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String JSONRequestForecast = null;
		try {
			JSONRequestForecast = new JSONReaderTask().execute(URLForecast)
					.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String JSONRequestForecastHourly = null;
		try {
			JSONRequestForecastHourly = new JSONReaderTask().execute(URLForecastHourly)
					.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		data = null;

		try {
			data = new ParseWU().execute(JSONRequestCond, JSONRequestAstronomy,
					JSONRequestForecast, JSONRequestForecastHourly).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		displayCurrentCond();
		displayForecast();

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		savedInstanceState.putString(CURRENT_QUERY, query);
		savedInstanceState.putString(CURRENT_UNITS, units);

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(CURRENT_QUERY, query);
		editor.putString(CURRENT_UNITS, units);

		// Commit the edits!
		editor.commit();
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//return true;
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_search:
	        	Intent intent = new Intent(this, SearchActivity.class);
	    		startActivity(intent);
	    		//finish();
	            return true;
	        case R.id.action_settings:
	            //openSettings(); // TODO implement settings menu.
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void searchLocation(View view) {
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
		finish(); // might not be the best thing to do since we would not be
					// able to return from search activity.

	}

	public void toggleUnits(View view) {

		if (units.equals("metric")) {
			units = "US";
		} else if (units.equals("US")) {
			units = "metric";
		}
		Log.i(MainActivity.class.getName(), "units: " + units);
		displayCurrentCond();
		displayForecast();

	}

	public void displayCurrentCond() {
		TextView conditionsText = (TextView) findViewById(R.id.currentConditions);
		conditionsText.setText(data.get("conditions"));

		TextView tempText = (TextView) findViewById(R.id.currentTemp);

		TextView humidityText = (TextView) findViewById(R.id.currentHumidity);
		humidityText.setText(data.get("humidity"));

		//TextView locationText = (TextView) findViewById(R.id.currentLocation);
		getSupportActionBar().setTitle(data.get("location"));
		//locationText.setText(data.get("location"));

		TextView UVText = (TextView) findViewById(R.id.UV);
		UVText.setText(data.get("UV"));

		TextView feelsLikeText = (TextView) findViewById(R.id.feelsLike);

		TextView windText = (TextView) findViewById(R.id.wind);

		TextView observationTimeText = (TextView) findViewById(R.id.observationTime);
		observationTimeText.setText(data.get("observation_time"));

		TextView observationLocationText = (TextView) findViewById(R.id.observationLocation);
		observationLocationText.setText(data.get("observation_location"));

		if (units.equals("US")) {
			tempText.setText(data.get("temp_f"));
			feelsLikeText.setText(data.get("feels_like_f"));
			windText.setText(data.get("wind_mph"));
		} else {
			tempText.setText(data.get("temp"));
			feelsLikeText.setText(data.get("feels_like"));
			windText.setText(data.get("wind"));
		}

		ImageView iw = (ImageView) findViewById(R.id.currentGraphic);
		int resID = getResources().getIdentifier(data.get("drawable"),
				"drawable", getPackageName());
		iw.setImageResource(resID);
	}

	public void displayForecast() {

		// set text views
		TextView day1Name = (TextView) findViewById(R.id.forecastDay1Name);
		TextView day2Name = (TextView) findViewById(R.id.forecastDay2Name);
		TextView day3Name = (TextView) findViewById(R.id.forecastDay3Name);
		TextView day4Name = (TextView) findViewById(R.id.forecastDay4Name);
		TextView day5Name = (TextView) findViewById(R.id.forecastDay5Name);
		
		TextView day1High = (TextView) findViewById(R.id.forecastDay1High);
		TextView day2High = (TextView) findViewById(R.id.forecastDay2High);
		TextView day3High = (TextView) findViewById(R.id.forecastDay3High);
		TextView day4High = (TextView) findViewById(R.id.forecastDay4High);
		TextView day5High = (TextView) findViewById(R.id.forecastDay5High);

		TextView day1Low = (TextView) findViewById(R.id.forecastDay1Low);
		TextView day2Low = (TextView) findViewById(R.id.forecastDay2Low);
		TextView day3Low = (TextView) findViewById(R.id.forecastDay3Low);
		TextView day4Low = (TextView) findViewById(R.id.forecastDay4Low);
		TextView day5Low = (TextView) findViewById(R.id.forecastDay5Low);
		
		int current_hour = Integer.parseInt(data.get("current_time_hour"));
		int current_min = Integer.parseInt(data.get("current_time_min"));
		
		String day1, day2, day3, day4, day5;
		
		if( current_hour > 18 ){
			day1 = "day2";
			day2 = "day3";
			day3 = "day4";
			day4 = "day5";
			day5 = "day6";
		}
		else {
			day1 = "day1";
			day2 = "day2";
			day3 = "day3";
			day4 = "day4";
			day5 = "day5";
		}
		
		day1Name.setText(data.get(day1 + "_name"));
		day2Name.setText(data.get(day2 + "_name"));		
		day3Name.setText(data.get(day3 + "_name"));		
		day4Name.setText(data.get(day4 + "_name"));		
		day5Name.setText(data.get(day5 + "_name"));

		


		if (units.equals("US")) {
			day1High.setText(data.get(day1 + "_high_f"));
			day2High.setText(data.get(day2 + "_high_f"));
			day3High.setText(data.get(day3 + "_high_f"));
			day4High.setText(data.get(day4 + "_high_f"));
			day5High.setText(data.get(day5+ "_high_f"));
			day1Low.setText(data.get(day1 + "_low_f"));
			day2Low.setText(data.get(day2 + "_low_f"));
			day3Low.setText(data.get(day3 + "_low_f"));
			day4Low.setText(data.get(day4 + "_low_f"));
			day5Low.setText(data.get(day5 + "_low_f"));
		} else {
			day1High.setText(data.get(day1 + "_high_c"));
			day2High.setText(data.get(day2 + "_high_c"));
			day3High.setText(data.get(day3 + "_high_c"));
			day4High.setText(data.get(day4 + "_high_c"));
			day5High.setText(data.get(day5 + "_high_c"));
			day1Low.setText(data.get(day1 + "_low_c"));
			day2Low.setText(data.get(day2 + "_low_c"));
			day3Low.setText(data.get(day3 + "_low_c"));
			day4Low.setText(data.get(day4 + "_low_c"));
			day5Low.setText(data.get(day5 + "_low_c"));
		}

		// set day P.O.P. (only if greater than minPOP)
		TextView day1POP = (TextView) findViewById(R.id.forecastDay1POP);
		TextView day2POP = (TextView) findViewById(R.id.forecastDay2POP);
		TextView day3POP = (TextView) findViewById(R.id.forecastDay3POP);
		TextView day4POP = (TextView) findViewById(R.id.forecastDay4POP);
		TextView day5POP = (TextView) findViewById(R.id.forecastDay5POP);
		
		int minPOP = 25;
		
		if ( Integer.parseInt(data.get(day1 + "_pop")) > minPOP ){
			day1POP.setText("POP: " + data.get(day1 + "_pop") + "%");
		} else {
			day1POP.setText("");
		}
				
		if ( Integer.parseInt(data.get(day2 + "_pop")) > minPOP ){
			day2POP.setText("POP: " + data.get(day2 + "_pop") + "%");
		} else {
			day2POP.setText("");
		}
		
		if ( Integer.parseInt(data.get(day3 + "_pop")) > minPOP ){
			day3POP.setText("POP: " + data.get(day3 + "_pop") + "%");
		} else {
			day3POP.setText("");
		}
		
		if ( Integer.parseInt(data.get(day4 + "_pop")) > minPOP ){
			day4POP.setText("POP: " + data.get(day4 + "_pop") + "%");
		} else {
			day4POP.setText("");
		}
		
		if ( Integer.parseInt(data.get(day5 + "_pop")) > minPOP ){
			day5POP.setText("POP: " + data.get(day5 + "_pop") + "%");
		} else {
			day5POP.setText("");
		}
		

		// set day condition graphic
		ImageView day1Cond = (ImageView) findViewById(R.id.forecastDay1Graphic);
		day1Cond.setImageResource(getResources().getIdentifier(
				data.get(day1 + "_cond"), "drawable", getPackageName()));

		ImageView day2Cond = (ImageView) findViewById(R.id.forecastDay2Graphic);
		day2Cond.setImageResource(getResources().getIdentifier(
				data.get(day2 + "_cond"), "drawable", getPackageName()));

		ImageView day3Cond = (ImageView) findViewById(R.id.forecastDay3Graphic);
		day3Cond.setImageResource(getResources().getIdentifier(
				data.get(day3 + "_cond"), "drawable", getPackageName()));

		ImageView day4Cond = (ImageView) findViewById(R.id.forecastDay4Graphic);
		day4Cond.setImageResource(getResources().getIdentifier(
				data.get(day4 + "_cond"), "drawable", getPackageName()));

		ImageView day5Cond = (ImageView) findViewById(R.id.forecastDay5Graphic);
		day5Cond.setImageResource(getResources().getIdentifier(
				data.get(day5 + "_cond"), "drawable", getPackageName()));

	}

}

package com.mannykary.droidweather;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;
import com.mannykary.droidweather.R;
import com.mannykary.droidweather.ParseWU.Conditions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	// TODO clean up UI
	
	HttpClient client;
	String currentConditions;
	String query;
	
	public final static String baseURL = "http://api.wunderground.com/api/b40d28a40f580244/";
	
	static final String CURRENT_QUERY = "currentQuery";

	Conditions cName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null ){
			query = savedInstanceState.getString(CURRENT_QUERY);
		}
		else {
			Intent intent = getIntent();
			
			if ( intent.getStringExtra("droidweather.searchactivity.url") == null ) {
				query = "/q/CA/San_Diego.json";
			}
			else {
				query = intent.getStringExtra("droidweather.searchactivity.url") + ".json";
			}	
		}
		setContentView(R.layout.activity_main);
		
		Log.i(MainActivity.class.getName(), "query: " + query);

		String URLConditions = baseURL + "conditions" + query;
		String URLAstronomy = baseURL + "astronomy" + query;
		String URLForecast = baseURL + "forecast10day" + query;
	
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
			JSONRequestAstronomy = new JSONReaderTask().execute(URLAstronomy).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String JSONRequestForecast = null;
		try {
			JSONRequestForecast = new JSONReaderTask().execute(URLForecast).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String, String> data = null;

		try { 
			data = new ParseWU().execute(JSONRequestCond, JSONRequestAstronomy, JSONRequestForecast).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		TextView conditionsText = (TextView) findViewById(R.id.currentConditions);
		conditionsText.setText(data.get("conditions"));
			
		TextView tempText = (TextView) findViewById(R.id.currentTemp);
		tempText.setText(data.get("temp"));
	
		TextView humidityText = (TextView) findViewById(R.id.currentHumidity);
		humidityText.setText(data.get("humidity"));
		
		TextView locationText = (TextView) findViewById(R.id.currentLocation);
		locationText.setText(data.get("location"));
		
		TextView UVText = (TextView) findViewById(R.id.UV);
		UVText.setText(data.get("UV"));

		TextView feelsLikeText = (TextView) findViewById(R.id.feelsLike);
		feelsLikeText.setText(data.get("feels_like"));
		
		TextView windText = (TextView) findViewById(R.id.wind);
		windText.setText(data.get("wind"));
					
		TextView observationTimeText = (TextView) findViewById(R.id.observationTime);
		observationTimeText.setText(data.get("observation_time"));
		
		TextView observationLocationText = (TextView) findViewById(R.id.observationLocation);
		observationLocationText.setText(data.get("observation_location"));
		
	    ImageView iw = (ImageView) findViewById(R.id.currentGraphic);  
	    int resID = getResources().getIdentifier(data.get("drawable"), "drawable",  getPackageName());
	    iw.setImageResource(resID);
	    
	    displayForecast(data);
				
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's current game state
	    savedInstanceState.putString(CURRENT_QUERY, query);
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void searchLocation(View view) {
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
		finish(); // might not be the best thing to do since we would not be able to return from search activity.

	}
	
	public void displayForecast(HashMap<String, String> d){	
		
		// set day name headers
		TextView day1Name = (TextView) findViewById(R.id.forecastDay1Name);
		day1Name.setText(d.get("day1_name"));
		
		TextView day2Name = (TextView) findViewById(R.id.forecastDay2Name);
		day2Name.setText(d.get("day2_name"));
		
		TextView day3Name = (TextView) findViewById(R.id.forecastDay3Name);
		day3Name.setText(d.get("day3_name"));
		
		TextView day4Name = (TextView) findViewById(R.id.forecastDay4Name);
		day4Name.setText(d.get("day4_name"));
		
		TextView day5Name = (TextView) findViewById(R.id.forecastDay5Name);
		day5Name.setText(d.get("day5_name"));
		
		// set day highs
		TextView day1High = (TextView) findViewById(R.id.forecastDay1High);
		day1High.setText(d.get("day1_high_c"));
		
		TextView day2High = (TextView) findViewById(R.id.forecastDay2High);
		day2High.setText(d.get("day2_high_c"));
		
		TextView day3High = (TextView) findViewById(R.id.forecastDay3High);
		day3High.setText(d.get("day3_high_c"));
		
		TextView day4High = (TextView) findViewById(R.id.forecastDay4High);
		day4High.setText(d.get("day4_high_c"));
		
		TextView day5High = (TextView) findViewById(R.id.forecastDay5High);
		day5High.setText(d.get("day5_high_c"));	
		
		// set day low
		TextView day1Low = (TextView) findViewById(R.id.forecastDay1Low);
		day1Low.setText(d.get("day1_low_c"));
		
		TextView day2Low = (TextView) findViewById(R.id.forecastDay2Low);
		day2Low.setText(d.get("day2_low_c"));
		
		TextView day3Low = (TextView) findViewById(R.id.forecastDay3Low);
		day3Low.setText(d.get("day3_low_c"));
		
		TextView day4Low = (TextView) findViewById(R.id.forecastDay4Low);
		day4Low.setText(d.get("day4_low_c"));
		
		TextView day5Low = (TextView) findViewById(R.id.forecastDay5Low);
		day5Low.setText(d.get("day5_low_c"));
		
		// set day condition graphic		
	    ImageView day1Cond = (ImageView) findViewById(R.id.forecastDay1Graphic);  
	    day1Cond.setImageResource(getResources().getIdentifier(d.get("day1_cond"), "drawable",  getPackageName()));
		
	    ImageView day2Cond = (ImageView) findViewById(R.id.forecastDay2Graphic);  
	    day2Cond.setImageResource(getResources().getIdentifier(d.get("day2_cond"), "drawable",  getPackageName()));
	    
	    ImageView day3Cond = (ImageView) findViewById(R.id.forecastDay3Graphic);  
	    day3Cond.setImageResource(getResources().getIdentifier(d.get("day3_cond"), "drawable",  getPackageName()));
	    
	    ImageView day4Cond = (ImageView) findViewById(R.id.forecastDay4Graphic);  
	    day4Cond.setImageResource(getResources().getIdentifier(d.get("day4_cond"), "drawable",  getPackageName()));
	    
	    ImageView day5Cond = (ImageView) findViewById(R.id.forecastDay5Graphic);  
	    day5Cond.setImageResource(getResources().getIdentifier(d.get("day5_cond"), "drawable",  getPackageName()));

	}


}

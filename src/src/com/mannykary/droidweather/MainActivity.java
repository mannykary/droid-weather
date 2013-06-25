package com.mannykary.droidweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mannykary.droidweather.R;
import com.mannykary.droidweather.ParseWU.Conditions;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// TODO clean up UI
	
	HttpClient client;
	String currentConditions;
	String query;
	
	public final static String baseURL = "http://api.wunderground.com/api/b40d28a40f580244/";
	static final String CURRENT_QUERY = "currentQuery";
	//static final String STATE_LEVEL = "playerLevel";

	
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
		
		//HashMap<String, String> textViews = new ParseWU().execute(query).get();
		
		
		//String dataFeature = "conditions";
		//String location = "CA/San_Diego.json";
		String URLConditions = baseURL + "conditions" + query;
		String URLAstronomy = baseURL + "astronomy" + query;
		
		//Log.i(MainActivity.class.getName(), "URL:" + URLConditions);
		
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
		
		//String JSONRequestCond = JSONReaderTask.readJSONRequest(URLConditions);
		//String JSONRequestAstronomy = JSONReaderTask.readJSONRequest(URLAstronomy);
		
		HashMap<String, String> data = null;
		try {
			data = new ParseWU().execute(JSONRequestCond, JSONRequestAstronomy).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//parseConditions(JSONRequestCond);
		//boolean dayTime = getDayNight(JSONRequestAstronomy);
					
		//cName = getCondEnum(currentConditions, dayTime);
				
		//setCondition(cName);
		
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


/*
	public Conditions getCondEnum(String s, boolean b){
		
		Conditions c;
		if(b){		
			if( s.contains("Blowing Snow") || s.contains("Low Drifting Snow") ){
				c = Conditions.BLOWING_SNOW;
			}
			else if( s.contains("Squalls") ){
				c = Conditions.BLUSTERY;
			}
			else if( s.contains("Clear") ){
				c = Conditions.CLEAR;
			}
			else if( s.contains("Mostly Cloudy") || s.contains("Overcast") ){
				c = Conditions.CLOUDY;
			}
			else if( s.contains("Drizzle") || s.contains("Mist") ){
				c = Conditions.DRIZZLE;
			}
			else if( s.contains("Dust") || s.contains("Sand") || s.contains("Volcanic Ash") ){
				c = Conditions.DUST;
			}
			else if( s.contains("Fog") ){
				c = Conditions.FOGGY;
			}
			else if( s.contains("Freezing Drizzle") ){
				c = Conditions.FREEZING_DRIZZLE;
			}
			else if( s.contains("Freezing Rain") || s.contains("Ice Crystals") ){
				c = Conditions.FREEZING_RAIN;
			}
			else if( s.contains("Hail Showers") ){
				c = Conditions.MIXED_RAIN_AND_HAIL;
			}
			else if( s.contains("Hail") ){
				c = Conditions.HAIL;
			}
			else if( s.contains("Haze") ){
				c = Conditions.HAZE;
			}
			else if( s.contains("Freezing Rain") || s.contains("Ice Crystals") ){
				c = Conditions.FREEZING_RAIN;
			}
			else if( s.contains("Heavy Snow") ){
				c = Conditions.HEAVY_SNOW;
			}
			else if( s.contains("Light Rain") ){
				c = Conditions.LIGHT_RAIN;
			}
			else if( s.contains("Light Snow Showers") ){
				c = Conditions.LIGHT_SNOW_SHOWERS;
			}
			else if( s.contains("Mostly CLoudy") ){
				c = Conditions.MOSTLY_CLOUDY;
			}
			else if( s.contains("Snow Showers") ){
				c = Conditions.SNOW_SHOWERS;
			}
			else if( s.contains("Partly Cloudy") || s.contains("Scattered Clouds") ){
				c = Conditions.PARTLY_CLOUDY;
			}
			else if( s.contains("Thunderstorms and Rain") ){
				c = Conditions.THUNDERSHOWERS;
			}
			else if( s.contains("Light Thunderstorm") ){
				c = Conditions.THUNDERSTORMS;
			}
			else if( s.contains("Thunderstorm") ){
				c = Conditions.SEVERE_THUNDERSTORMS;
			}
			else if( s.contains("Rain") || s.contains("Spray") ){
				c = Conditions.SHOWERS;
			}
			else if( s.contains("Ice Pellet") || s.contains("Snow Grains") ){
				c = Conditions.SLEET;
			}
			else if( s.contains("Smoke") ){
				c = Conditions.SMOKY;
			}
			else if( s.contains("Light Snow") ){
				c = Conditions.SNOW_FLURRIES;
			}
			else if( s.contains("Snow") ){
				c = Conditions.SNOW;
			}
			else if( s.contains("Funnel Cloud") ){
				c = Conditions.TORNADO;
			}
			else{
				c = Conditions.DEFAULT_WEATHER_ICON;
			}
		} else{
			if( s.contains("Blowing Snow") || s.contains("Low Drifting Snow") ){
				c = Conditions.NIGHT_BLOWING_SNOW;
			}
			else if( s.contains("Squalls") ){
				c = Conditions.NIGHT_BLUSTERY;
			}
			else if( s.contains("Clear") ){
				c = Conditions.NIGHT_CLEAR;
			}
			else if( s.contains("Mostly Cloudy") || s.contains("Overcast") ){
				c = Conditions.NIGHT_CLOUDY;
			}
			else if( s.contains("Drizzle") || s.contains("Mist") ){
				c = Conditions.NIGHT_DRIZZLE;
			}
			else if( s.contains("Dust") || s.contains("Sand") || s.contains("Volcanic Ash") ){
				c = Conditions.DUST;
			}
			else if( s.contains("Fog") ){
				c = Conditions.NIGHT_FOGGY;
			}
			else if( s.contains("Freezing Drizzle") ){
				c = Conditions.NIGHT_FREEZING_DRIZZLE;
			}
			else if( s.contains("Freezing Rain") || s.contains("Ice Crystals") ){
				c = Conditions.NIGHT_FREEZING_RAIN;
			}
			else if( s.contains("Hail Showers") ){
				c = Conditions.NIGHT_MIXED_RAIN_AND_HAIL;
			}
			else if( s.contains("Hail") ){
				c = Conditions.NIGHT_HAIL;
			}
			else if( s.contains("Haze") ){
				c = Conditions.HAZE;
			}
			else if( s.contains("Freezing Rain") || s.contains("Ice Crystals") ){
				c = Conditions.NIGHT_FREEZING_RAIN;
			}
			else if( s.contains("Heavy Snow") ){
				c = Conditions.NIGHT_HEAVY_SNOW;
			}
			else if( s.contains("Light Rain") ){
				c = Conditions.NIGHT_SCATTERED_SHOWERS; // TODO confirm this.
			}
			else if( s.contains("Light Snow Showers") ){
				c = Conditions.NIGHT_LIGHT_SNOW_SHOWERS;
			}
			else if( s.contains("Mostly CLoudy") ){
				c = Conditions.NIGHT_MOSTLY_CLOUDY;
			}
			else if( s.contains("Snow Showers") ){
				c = Conditions.NIGHT_SNOW_SHOWERS;
			}
			else if( s.contains("Partly Cloudy") || s.contains("Scattered Clouds") ){
				c = Conditions.NIGHT_PARTLY_CLOUDY;
			}
			else if( s.contains("Thunderstorms and Rain") ){
				c = Conditions.NIGHT_THUNDERSHOWERS;
			}
			else if( s.contains("Light Thunderstorm") ){
				c = Conditions.NIGHT_THUNDERSTORMS;
			}
			else if( s.contains("Thunderstorm") ){
				c = Conditions.NIGHT_SEVERE_THUNDERSTORMS;
			}
			else if( s.contains("Rain") || s.contains("Spray") ){
				c = Conditions.NIGHT_SHOWERS;
			}
			else if( s.contains("Ice Pellet") || s.contains("Snow Grains") ){
				c = Conditions.NIGHT_SLEET;
			}
			else if( s.contains("Smoke") ){
				c = Conditions.SMOKY;
			}
			else if( s.contains("Light Snow") ){
				c = Conditions.NIGHT_SNOW_FLURRIES;
			}
			else if( s.contains("Snow") ){
				c = Conditions.NIGHT_SNOW;
			}
			else if( s.contains("Funnel Cloud") ){
				c = Conditions.NIGHT_TORNADO;
			}
			else{
				c = Conditions.NIGHT_DEFAULT_WEATHER_ICON;
			}
		}
			
		return c;
	}
	
	public void setCondition(Conditions c) {
		
		String drawableName = null;
		
		switch (c) {
				
			case BLOWING_SNOW:
				drawableName = "blowing_snow";
				break;
			case BLUSTERY:
				drawableName = "blustery";
				break;
			case CLEAR:
				drawableName = "clear";
				break;
			case CLOUDY:
				drawableName = "cloudy";
				break;
			case COLD:
				drawableName = "cold";
				break;
			case DEFAULT_WEATHER_ICON:
				drawableName = "default_weather_icon";
				break;
			case DRIZZLE:
				drawableName = "drizzle";
				break;
			case DUST:
				drawableName = "dust";
				break;
			case FAIR:
				drawableName = "fair";
				break;
			case FOGGY:
				drawableName = "foggy";
				break;
			case FREEZING_DRIZZLE:
				drawableName = "freezing_drizzle";
				break;
			case FREEZING_RAIN:
				drawableName = "freezing_rain";
				break;
			case HAIL:
				drawableName = "hail";
				break;
			case HAZE:
				drawableName = "haze";
				break;
			case HEAVY_SNOW:
				drawableName = "heavy_snow";
				break;
			case HOT:
				drawableName = "hot";
				break;
			case HURRICANE:
				drawableName = "hurricane";
				break;
			case ISOLATED_THUNDERSHOWERS:
				drawableName = "isolated_thundershowers";
				break;
			case ISOLATED_THUNDERSTORMS:
				drawableName = "isolated_thunderstorms";
				break;
			case LIGHT_RAIN:
				drawableName = "light_rain";
				break;
			case LIGHT_SNOW_SHOWERS:
				drawableName = "light_snow_showers";
				break;
			case MIXED_RAIN_AND_HAIL:
				drawableName = "mixed_rain_and_hail";
				break;
			case MIXED_RAIN_AND_SLEET:
				drawableName = "mixed_rain_and_sleet";
				break;
			case MIXED_RAIN_AND_SNOW:
				drawableName = "mixed_rain_and_snow";
				break;
			case MIXED_SNOW_AND_SLEET:
				drawableName = "mixed_snow_and_sleet";
				break;
			case MOSTLY_CLOUDY:
				drawableName = "mostly_cloudy";
				break;
			case NIGHT_BLOWING_SNOW:
				drawableName = "night_blowing_snow";
				break;
			case NIGHT_BLUSTERY:
				drawableName = "night_blustery";
				break;
			case NIGHT_CLEAR:
				drawableName = "night_clear";
				break;
			case NIGHT_CLOUDY:
				drawableName = "night_cloudy";
				break;
			case NIGHT_COLD:
				drawableName = "night_cold";
				break;
			case NIGHT_DEFAULT_WEATHER_ICON:
				drawableName = "night_default_weather_icon";
				break;
			case NIGHT_DRIZZLE:
				drawableName = "night_drizzle";
				break;
			case NIGHT_FAIR:
				drawableName = "night_fair";
				break;
			case NIGHT_FOGGY:
				drawableName = "night_foggy";
				break;
			case NIGHT_FREEZING_DRIZZLE:
				drawableName = "night_freezing_drizzle";
				break;
			case NIGHT_FREEZING_RAIN:
				drawableName = "night_freezing_rain";
				break;
			case NIGHT_HAIL:
				drawableName = "night_hail";
				break;
			case NIGHT_HEAVY_SNOW:
				drawableName = "night_heavy_snow";
				break;
			case NIGHT_HURRICANE:
				drawableName = "night_hurricane";
				break;
			case NIGHT_ISOLATED_THUNDERSHOWERS:
				drawableName = "night_isolated_thundershowers";
				break;
			case NIGHT_ISOLATED_THUNDERSTORMS:
				drawableName = "night_isolated_thunderstorms";
				break;
			case NIGHT_LIGHT_SNOW_SHOWERS:
				drawableName = "night_light_snow_showers";
				break;
			case NIGHT_MIXED_RAIN_AND_HAIL:
				drawableName = "night_mixed_rain_and_hail";
				break;
			case NIGHT_MIXED_RAIN_AND_SLEET:
				drawableName = "night_mixed_rain_and_sleet";
				break;
			case NIGHT_MIXED_RAIN_AND_SNOW:
				drawableName = "night_mixed_rain_and_snow";
				break;
			case NIGHT_MIXED_SNOW_AND_SLEET:
				drawableName = "night_mixed_snow_and_sleet";
				break;
			case NIGHT_MOSTLY_CLOUDY:
				drawableName = "night_mostly_cloudy";
				break;
			case NIGHT_PARTLY_CLOUDY:
				drawableName = "night_partly_cloudy";
				break;
			case NIGHT_PM_SHOWERS:
				drawableName = "night_pm_showers";
				break;
			case NIGHT_SCATTERED_SHOWERS:
				drawableName = "night_scattered_showers";
				break;
			case NIGHT_SCATTERED_SNOW_SHOWERS:
				drawableName = "night_scattered_snow_showers";
				break;
			case NIGHT_SCATTERED_THUNDERSTORMS:
				drawableName = "night_scattered_thunderstorms";
				break;
			case NIGHT_SEVERE_THUNDERSTORMS:
				drawableName = "night_severe_thunderstorms";
				break;
			case NIGHT_SHOWERS:
				drawableName = "night_showers";
				break;
			case NIGHT_SLEET:
				drawableName = "night_sleet";
				break;
			case NIGHT_SNOW_FLURRIES:
				drawableName = "night_snow_flurries";
				break;
			case NIGHT_SNOW_SHOWERS:
				drawableName = "night_snow_showers";
				break;
			case NIGHT_SNOW:
				drawableName = "night_snow";
				break;
			case NIGHT_THUNDERSHOWERS:
				drawableName = "night_thundershowers";
				break;
			case NIGHT_THUNDERSTORMS:
				drawableName = "night_thunderstorms";
				break;
			case NIGHT_TORNADO:
				drawableName = "night_tornado";
				break;
			case NIGHT_TROPICAL_STORM:
				drawableName = "night_tropical_storm";
				break;
			case NIGHT_WINDY:
				drawableName = "night_windy";
				break;
			case PARTLY_CLOUDY:
				drawableName = "partly_cloudy";
				break;
			case PM_SHOWERS:
				drawableName = "pm_showers";
				break;
			case SCATTERED_SHOWERS:
				drawableName = "scattered_showers";
				break;
			case SCATTERED_SNOW_SHOWERS:
				drawableName = "scattered_snow_showers";
				break;
			case SCATTERED_THUNDERSTORMS:
				drawableName = "scattered_thunderstorms";
				break;
			case SEVERE_THUNDERSTORMS:
				drawableName = "severe_thunderstorms";
				break;
			case SHOWERS:
				drawableName = "showers";
				break;
			case SLEET:
				drawableName = "sleet";
				break;
			case SMOKY:
				drawableName = "smoky";
				break;
			case SNOW_FLURRIES:
				drawableName = "snow_flurries";
				break;
			case SNOW_SHOWERS:
				drawableName = "snow_showers";
				break;
			case SNOW:
				drawableName = "snow";
				break;
			case SUNNY:
				drawableName = "sunny";
				break;
			case THUNDERSHOWERS:
				drawableName = "thundershowers";
				break;
			case THUNDERSTORMS:
				drawableName = "thunderstorms";
				break;
			case TORNADO:
				drawableName = "tornado";
				break;
			case TROPICAL_STORM:
				drawableName = "tropical_storm";
				break;
			case WINDY:
				drawableName = "windy";
				break;
			default:
				drawableName = "default_weather_icon";
		}
		
	    ImageView iw = (ImageView) findViewById(R.id.currentGraphic);  
	    int resID = getResources().getIdentifier(drawableName, "drawable",  getPackageName());
	    iw.setImageResource(resID);
	}
*/
	
/*
	public String readJSONRequest(String url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;

				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(MainActivity.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Log.i(MainActivity.class.getName(), builder.toString());
		return builder.toString();
	}
*/
}

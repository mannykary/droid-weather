package com.mannykary.droidweather;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class ParseWU extends AsyncTask<String, Void, HashMap<String, String>> {

	public enum Conditions {
		BLOWING_SNOW, BLUSTERY, CLEAR, CLOUDY, COLD, DEFAULT_WEATHER_ICON, DRIZZLE, DUST, FAIR, FOGGY, FREEZING_DRIZZLE, FREEZING_RAIN, HAIL, HAZE, HEAVY_SNOW, HOT, HURRICANE, ISOLATED_THUNDERSHOWERS, ISOLATED_THUNDERSTORMS, LIGHT_RAIN, LIGHT_SNOW_SHOWERS, MIXED_RAIN_AND_HAIL, MIXED_RAIN_AND_SLEET, MIXED_RAIN_AND_SNOW, MIXED_SNOW_AND_SLEET, MOSTLY_CLOUDY, NIGHT_BLOWING_SNOW, NIGHT_BLUSTERY, NIGHT_CLEAR, NIGHT_CLOUDY, NIGHT_COLD, NIGHT_DEFAULT_WEATHER_ICON, NIGHT_DRIZZLE, NIGHT_FAIR, NIGHT_FOGGY, NIGHT_FREEZING_DRIZZLE, NIGHT_FREEZING_RAIN, NIGHT_HAIL, NIGHT_HEAVY_SNOW, NIGHT_HURRICANE, NIGHT_ISOLATED_THUNDERSHOWERS, NIGHT_ISOLATED_THUNDERSTORMS, NIGHT_LIGHT_SNOW_SHOWERS, NIGHT_MIXED_RAIN_AND_HAIL, NIGHT_MIXED_RAIN_AND_SLEET, NIGHT_MIXED_RAIN_AND_SNOW, NIGHT_MIXED_SNOW_AND_SLEET, NIGHT_MOSTLY_CLOUDY, NIGHT_PARTLY_CLOUDY, NIGHT_PM_SHOWERS, NIGHT_SCATTERED_SHOWERS, NIGHT_SCATTERED_SNOW_SHOWERS, NIGHT_SCATTERED_THUNDERSTORMS, NIGHT_SEVERE_THUNDERSTORMS, NIGHT_SHOWERS, NIGHT_SLEET, NIGHT_SNOW_FLURRIES, NIGHT_SNOW_SHOWERS, NIGHT_SNOW, NIGHT_THUNDERSHOWERS, NIGHT_THUNDERSTORMS, NIGHT_TORNADO, NIGHT_TROPICAL_STORM, NIGHT_WINDY, PARTLY_CLOUDY, PM_SHOWERS, SCATTERED_SHOWERS, SCATTERED_SNOW_SHOWERS, SCATTERED_THUNDERSTORMS, SEVERE_THUNDERSTORMS, SHOWERS, SLEET, SMOKY, SNOW_FLURRIES, SNOW_SHOWERS, SNOW, SUNNY, THUNDERSHOWERS, THUNDERSTORMS, TORNADO, TROPICAL_STORM, WINDY
	}
	
	public final static String baseURL = "http://api.wunderground.com/api/b40d28a40f580244/";
	
	public boolean getDayNight(String j){
		try {
			JSONObject mainObject = new JSONObject(j);
			
			JSONObject moon_phase_obj = mainObject.getJSONObject("moon_phase");
			JSONObject current_time_obj = moon_phase_obj.getJSONObject("current_time");
			JSONObject sunrise_obj = moon_phase_obj.getJSONObject("sunrise");
			JSONObject sunset_obj = moon_phase_obj.getJSONObject("sunset");
			
			int current_time_hour = current_time_obj.getInt("hour");
			int current_time_min = current_time_obj.getInt("minute");
			
			int sunrise_hour = sunrise_obj.getInt("hour");
			int sunrise_min = sunrise_obj.getInt("minute");
			
			int sunset_hour = sunset_obj.getInt("hour");
			int sunset_min = sunset_obj.getInt("minute");
			
			boolean day;
			
			Log.i(MainActivity.class.getName(), "Time: " + current_time_hour + ":" + current_time_min );
			Log.i(MainActivity.class.getName(), "Sunrise: " + sunrise_hour + ":" + sunrise_min );
			Log.i(MainActivity.class.getName(), "Sunset: " + sunset_hour + ":" + sunset_min );
			
			if( current_time_hour > sunrise_hour && current_time_hour < sunset_hour ){
				day = true;
			} else if( current_time_hour == sunrise_hour && current_time_min > sunrise_min ){
				day = true;
			} else if( current_time_hour == sunset_hour && current_time_min < sunset_min ){
				day = true;
			} else{
				day = false;
			}
			
			return day;
			
		}
		catch ( Exception e ) {
			e.printStackTrace();
			return false; // this may not be the best thing to return. will have to keep an eye on this. TODO
		}

	}
	/*
	public void parseConditions(String j) {
		try {

			JSONObject mainObject = new JSONObject(j);

			// get Json current_observation object
			JSONObject current_observation_obj = mainObject
					.getJSONObject("current_observation");

			// get Json display_location object
			JSONObject display_location_obj = current_observation_obj
					.getJSONObject("display_location");

			// get Json observation_location object
			JSONObject observation_location_obj = current_observation_obj
					.getJSONObject("observation_location");


			TextView conditionsText = (TextView) findViewById(R.id.currentConditions);
			conditionsText.setText(current_observation_obj.getString("weather"));

			TextView tempText = (TextView) findViewById(R.id.currentTemp);
			tempText.setText(current_observation_obj.getString("temp_c") + "¼C");

			TextView humidityText = (TextView) findViewById(R.id.currentHumidity);
			humidityText.setText("Humidity: "
					+ current_observation_obj.getString("relative_humidity"));

			TextView locationText = (TextView) findViewById(R.id.currentLocation);
			locationText.setText(display_location_obj.getString("full"));

			TextView UVText = (TextView) findViewById(R.id.UV);
			UVText.setText("UV Index: " + current_observation_obj.getString("UV"));

			TextView feelsLikeText = (TextView) findViewById(R.id.feelsLike);
			feelsLikeText.setText("Feels Like: " + current_observation_obj
					.getString("feelslike_c") + "¼C");

			TextView windText = (TextView) findViewById(R.id.wind);
			windText.setText("Wind: " + current_observation_obj.getString("wind_string"));

			TextView observationTimeText = (TextView) findViewById(R.id.observationTime);
			observationTimeText.setText(current_observation_obj
					.getString("observation_time"));

			TextView observationLocationText = (TextView) findViewById(R.id.observationLocation);
			observationLocationText.setText(observation_location_obj
					.getString("city"));

			currentConditions = current_observation_obj.getString("weather");
		

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	*/
	public Conditions getCondEnum(String s, String b){
		
		Conditions c;
		if(b.equals("day")){		
			
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
		} else if (b.equals("night")){
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
		} else {
			c = Conditions.DEFAULT_WEATHER_ICON;
		}
			
		return c;
	}
	
	public String setCondition(Conditions c) {
		
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
		
	    return drawableName;
	}

	@Override
	protected HashMap<String, String> doInBackground(String... params) {
		//String URLConditions = baseURL + "conditions" + params[0];
		//String URLAstronomy = baseURL + "astronomy" + params[0];
		HashMap<String, String> data = new HashMap<String, String>();

		try {
			
			// extract current conditions
			
			JSONObject mainObject = new JSONObject(params[0]);

			// get Json current_observation object
			JSONObject current_observation_obj = mainObject
					.getJSONObject("current_observation");

			// get Json display_location object
			JSONObject display_location_obj = current_observation_obj
					.getJSONObject("display_location");

			// get Json observation_location object
			JSONObject observation_location_obj = current_observation_obj
					.getJSONObject("observation_location");
			
			//TextView conditionsText = (TextView) findViewById(R.id.currentConditions);
			//conditionsText.setText(current_observation_obj.getString("weather"));

			data.put("conditions", current_observation_obj.getString("weather"));			
			
			//TextView tempText = (TextView) findViewById(R.id.currentTemp);
			//tempText.setText(current_observation_obj.getString("temp_c") + "¼C");
			
			data.put("temp", current_observation_obj.getString("temp_c") + "¼C");
			
			data.put("temp_f", current_observation_obj.getString("temp_f") + "¼F");

			//TextView humidityText = (TextView) findViewById(R.id.currentHumidity);
			//humidityText.setText("Humidity: " + current_observation_obj.getString("relative_humidity"));

			data.put("humidity", "Humidity: " + current_observation_obj.getString("relative_humidity"));
			
			//TextView locationText = (TextView) findViewById(R.id.currentLocation);
			//locationText.setText(display_location_obj.getString("full"));

			data.put("location", display_location_obj.getString("full"));
			
			//TextView UVText = (TextView) findViewById(R.id.UV);
			//UVText.setText("UV Index: " + current_observation_obj.getString("UV"));

			data.put("UV", "UV Index: " + current_observation_obj.getString("UV"));
			
			
			//TextView feelsLikeText = (TextView) findViewById(R.id.feelsLike);
			//feelsLikeText.setText("Feels Like: " + current_observation_obj.getString("feelslike_c") + "¼C");

			data.put("feels_like", "Feels Like: " + current_observation_obj.getString("feelslike_c") + "¼C");
			
			data.put("feels_like_f", "Feels Like: " + current_observation_obj.getString("feelslike_f") + "¼F");
			
			//TextView windText = (TextView) findViewById(R.id.wind);
			//windText.setText("Wind: " + current_observation_obj.getString("wind_string"));

			data.put("wind", "Wind: " + current_observation_obj.getString("wind_kph") + " km/h " 
									  + current_observation_obj.getString("wind_dir") );
			
			data.put("wind_mph", "Wind: " + current_observation_obj.getString("wind_mph") + " mph " 
					  					  + current_observation_obj.getString("wind_dir") );

						
			//TextView observationTimeText = (TextView) findViewById(R.id.observationTime);
			//observationTimeText.setText(current_observation_obj.getString("observation_time"));

			data.put("observation_time", current_observation_obj.getString("observation_time"));
			
			//TextView observationLocationText = (TextView) findViewById(R.id.observationLocation);
			//observationLocationText.setText(observation_location_obj.getString("city"));

			data.put("observation_location", observation_location_obj.getString("city"));
			
			//currentConditions = current_observation_obj.getString("weather");
			
			// extract sunrise/sunset information
			
			JSONObject astObject = new JSONObject(params[1]);
			
			JSONObject moon_phase_obj = astObject.getJSONObject("moon_phase");
			JSONObject current_time_obj = moon_phase_obj.getJSONObject("current_time");
			JSONObject sunrise_obj = moon_phase_obj.getJSONObject("sunrise");
			JSONObject sunset_obj = moon_phase_obj.getJSONObject("sunset");
			
			int current_time_hour = current_time_obj.getInt("hour");
			int current_time_min = current_time_obj.getInt("minute");
			
			int sunrise_hour = sunrise_obj.getInt("hour");
			int sunrise_min = sunrise_obj.getInt("minute");
			
			int sunset_hour = sunset_obj.getInt("hour");
			int sunset_min = sunset_obj.getInt("minute");
			
			String dayNight;
			
			Log.i(ParseWU.class.getName(), "Time: " + current_time_hour + ":" + current_time_min );
			Log.i(ParseWU.class.getName(), "Sunrise: " + sunrise_hour + ":" + sunrise_min );
			Log.i(ParseWU.class.getName(), "Sunset: " + sunset_hour + ":" + sunset_min );
			
			if( current_time_hour > sunrise_hour && current_time_hour < sunset_hour ){
				dayNight = "day";
			} else if( current_time_hour == sunrise_hour && current_time_min > sunrise_min ){
				dayNight = "day";
			} else if( current_time_hour == sunset_hour && current_time_min < sunset_min ){
				dayNight = "day";			
			} else{
				dayNight = "night";
			}
			
			data.put("day_night", dayNight);
			
			Conditions cName = getCondEnum(current_observation_obj.getString("weather"), dayNight);
		
			// set the weather graphic
			
			String drawable = setCondition(cName);
			
			data.put("drawable", drawable);
			
			
			// extract forecast information
			JSONObject forecast10DayObject = new JSONObject(params[2]);
			
			JSONObject forecast_obj = forecast10DayObject.getJSONObject("forecast");
			JSONObject simpleforecast_obj = forecast_obj.getJSONObject("simpleforecast");
			JSONArray forecastday_arr = simpleforecast_obj.getJSONArray("forecastday");
			
			// change i depending of number of days in forecast.
			for( int i = 0; i < 5; i++ ){
				int j = i + 1;
				data.put("day" + j + "_high_c" , forecastday_arr.getJSONObject(i).getJSONObject("high").getString("celsius"));
				data.put("day" + j + "_low_c" , forecastday_arr.getJSONObject(i).getJSONObject("low").getString("celsius"));
				data.put("day" + j + "_high_f" , forecastday_arr.getJSONObject(i).getJSONObject("high").getString("fahrenheit"));
				data.put("day" + j + "_low_f" , forecastday_arr.getJSONObject(i).getJSONObject("low").getString("fahrenheit"));
				data.put("day" + j + "_name", forecastday_arr.getJSONObject(i).getJSONObject("date").getString("weekday_short"));
				
				Conditions dayCond = getCondEnum(forecastday_arr.getJSONObject(i).getString("conditions"), "day");
							
				data.put("day" + j + "_cond", setCondition(dayCond));
				
				Log.i(ParseWU.class.getName(), "day" + j + "_high_c: " + forecastday_arr.getJSONObject(i).getJSONObject("high").getString("celsius") );
				Log.i(ParseWU.class.getName(), "day" + j + "_low_c: " + forecastday_arr.getJSONObject(i).getJSONObject("low").getString("celsius") );
				Log.i(ParseWU.class.getName(), "day" + j + "_high_f: " + forecastday_arr.getJSONObject(i).getJSONObject("high").getString("fahrenheit") );
				Log.i(ParseWU.class.getName(), "day" + j + "_low_f: " + forecastday_arr.getJSONObject(i).getJSONObject("low").getString("fahrenheit") );
				Log.i(ParseWU.class.getName(), "day" + j + "_name: " + forecastday_arr.getJSONObject(i).getJSONObject("date").getString("weekday_short") );
				Log.i(ParseWU.class.getName(), "day" + j + "_cond: " + forecastday_arr.getJSONObject(i).getString("conditions"));
				
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		
		return data;
	}
}

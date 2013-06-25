package com.mannykary.droidweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import com.mannykary.droidweather.R;

public class SearchActivity extends Activity {

	EditText searchBox;
	ListView listView;
	Map<String, String> list;
	String[] listCities;
	String[] listURLs;

	// Button btnSearch;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		// String[] list = { "" };

		searchBox = (EditText) findViewById(R.id.search_box);

	    /*
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	        // For the main activity, make sure the app icon in the action bar
	        // does not behave as a button
	        ActionBar actionBar = getActionBar();
	        actionBar.setHomeButtonEnabled(false);
	    }
	    */
	    
		// btnSearch = (Button) findViewById(R.id.btnGetResults);

		/*
		 * Intent intent = new Intent(SearchActivity.this, MainActivity.class);
		 * intent.putExtra("some_key", value); intent.putExtra("some_other_key",
		 * "a value"); startActivity(intent);
		 */

		// TODO Auto-generated method stub
	}
	


	public void getSearchBoxText(View view) {
		String q = searchBox.getText().toString().trim().replace(' ', '+'); // TODO use URL or URI instead to catch malformed urls.
		String qURL = "http://autocomplete.wunderground.com/aq?query=" + q;
		String jq = null;
		
		try {
			jq = new JSONReaderTask().execute(qURL).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Log.i(SearchActivity.class.getName(), "json: " + jq);

		list = getSearchResults(jq);

		if (list.isEmpty()) {
			// provide error message saying that there are no results
			listCities = new String[1];
			listCities[0] = "No matches";
		} else {

			int mapSize = list.size();
			listCities = new String[mapSize];
			listURLs = new String[mapSize];

			int j = 0;

			for (Map.Entry<String, String> entry : list.entrySet()) {
				listCities[j] = entry.getKey();
				listURLs[j] = entry.getValue();
				// Log.i(SearchActivity.class.getName(), entry.getKey() + " " +
				// entry.getValue());
				j++;
			}

			for (int i = 0; i < mapSize; i++) {
				Log.i(SearchActivity.class.getName(), listCities[i] + " "
						+ listURLs[i]);
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listCities);
			
			listView = (ListView) findViewById(R.id.searchResults);
			listView.setAdapter(adapter);
			
			OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
			    public void onItemClick(AdapterView parent, View v, int position, long id) {
			        // Do something in response to the click		    	
			    	//Log.i(SearchActivity.class.getName(), "query url: " + listURLs[position] );	    	
			    	Intent intent = new Intent(SearchActivity.this, MainActivity.class);
					intent.putExtra("com.droidweather.searchactivity.url", listURLs[position]);
			    	startActivity(intent);
			    	finish();
			    }
			};

			listView.setOnItemClickListener(mMessageClickedHandler); 
			
			//listView.setOnClickListener(this); // TODO figure out how to see
													// which item in list is
													// being pressed.

		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listCities);
		
		listView = (ListView) findViewById(R.id.searchResults);
		listView.setAdapter(adapter);
		//listView.setOnClickListener(this); // TODO figure out how to see
												// which item in list is
												// being pressed.
		/*
		 * for( int i = 0; i < list.length; i++){
		 * Log.i(SearchActivity.class.getName(), "foobar" + list[i] ); }
		 */
	}

	public Map<String, String> getSearchResults(String s) {

		// String[] list = { "" };
		// ArrayList<String> ar = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();

		try {
			JSONObject mainObject = new JSONObject(s);
			JSONArray resultsArr = mainObject.getJSONArray("RESULTS");

			for (int i = 0; i < resultsArr.length(); i++) {

				JSONObject json_data = resultsArr.getJSONObject(i);
				map.put(json_data.getString("name"), json_data.getString("l"));

				Log.i(SearchActivity.class.getName(),
						json_data.getString("name") + " "
								+ json_data.getString("l"));
			}

			return map;

		} catch (Exception e) {
			e.printStackTrace();
			return map; // need to catch this better TODO
		}
	}
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
				Log.e(SearchActivity.class.toString(), "Failed to download file");
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
	/*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        // set the message to display
          alertbox.setMessage("Please Get Ready").show();

    }
*/


}

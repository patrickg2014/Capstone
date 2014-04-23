package com.cs440.capstone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CampusInfoSearch extends Activity implements OnClickListener {

	EditText searchText;
	Button searchButton;

	public final static String SEARCH_TEXT = ""; // This is used to pass the user inputed data to the next activity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		searchText = (EditText) findViewById(R.id.etSearch); // Lets find our EditText field
		searchButton = (Button) findViewById(R.id.bSearch); // Lets find our Button
		searchButton.setOnClickListener(this); // Set a on click listener to our button
		
		Log.d("Chris", "Created.");
	}

	@Override
	public void onClick(View arg0) {
		
		Log.d("Chris", "Clicked");
		
		switch (arg0.getId()) {
		case R.id.bSearch: // Case for when the user presses the search button
			Log.d("Chirs", "about to go to ViewSearchResults");
			String searchKey = searchText.getText().toString(); // Get the user inputed text from the EditText field
			Intent searchIntent = new Intent(this, ViewSearchResults.class); // Make an intent to go to the ViewSearchResults class
			searchIntent.putExtra(SEARCH_TEXT, searchKey); // Put the searchKey into the intent so ViewSearchResults knows what to look for
			startActivity(searchIntent); // Start ViewSearchResults
			searchText.setText(""); // Reset the EditText field
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
}
package com.cs440.capstone;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CampusInfoSearch extends Activity implements OnClickListener {

	EditText searchText;
	Button searchButton;
	ListView lv;
	LinearLayout ll;

	public final static String SEARCH_TEXT = ""; // This is used to pass the user inputed data to the next activity
	public static String ROW_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campus_info_search);
		searchText = (EditText) findViewById(R.id.etSearch); // Lets find our EditText field
		searchButton = (Button) findViewById(R.id.bSearch); // Lets find our Button
		lv = (ListView) findViewById(R.id.lvTheList); // Lets find our ListView
		searchButton.setOnClickListener(this); // Set a on click listener to our button

		popluateListView();
	}

	public void popluateListView() {
		
		Log.d("Chris", "populating list view in Campusinfoseach");
		
		CampusDatabase db = new CampusDatabase(this);
		db.open();

		Cursor cursor = db.getAllRows();
		// Allow activity to manage lifetime of cursror
		startManagingCursor(cursor);
		// Setup mapping from cursor to view feilds
		String[] fromFeildNames = new String[] { CampusDatabase.KEY_NAME, CampusDatabase.KEY_TYPE, CampusDatabase.KEY_IMAGE };

		int[] toViewIDs = new int[] { R.id.event_name, R.id.event_time,R.id.event_icon };

		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this,R.layout.horizontal_scroll_item_layout, cursor, fromFeildNames,toViewIDs);

		ListView myList = (ListView) findViewById(R.id.lvTheList);
		myList.setAdapter(myCursorAdapter);
		db.close();
	}
	
	public void buildingInfoClicked(View v){
		TextView theClicked = (TextView) v.findViewById(R.id.event_name);
		String s = theClicked.getText().toString();
		CampusDatabase tdb = new CampusDatabase(this);
		tdb.open();
		long rowid = tdb.findRowIDFromBuildingName(s); //Find rowid from building name...
		//pass that rowid to ViewCampusInfoFromDB
		Intent toViewCampusInfoFromDB = new Intent(this, ViewCampusInfoFromDB.class);
		String str = ""+rowid;
		toViewCampusInfoFromDB.putExtra(ROW_ID , str);
		startActivity(toViewCampusInfoFromDB);
	}

	public void eventInfoClicked(View v){
		Toast.makeText(this, "You want to look at events", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bSearch: // Case for when the user presses the search button
			String searchKey = searchText.getText().toString(); // Get the user inputed text from the EditText field
			
			if(searchKey.contentEquals("")){ //Check if the user actually inputed data
				Toast.makeText(this, "Please enter a search value.", Toast.LENGTH_LONG).show();
				break;
			}
			
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
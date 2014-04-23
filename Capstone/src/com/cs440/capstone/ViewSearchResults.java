package com.cs440.capstone;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSearchResults extends Activity {

	private String searchKey; // The input that the user entered, what they are looking for
	public static String ROW_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_search_results);

		Intent theIntent = getIntent();
		searchKey = theIntent.getStringExtra(CampusInfoSearch.SEARCH_TEXT); // Get the extra String that was stored in the intent
		
		
		popluateListViewFromDB();
		registerListClickCallback();
		Log.d("Chirs", "done.");
	}

	public void popluateListViewFromDB() {
		CampusDatabase db = new CampusDatabase(this);
		db.open();
		Cursor cursor = db.searchByName(searchKey);
		
		if(cursor == null){
			Toast.makeText(this, "Cursor is null!", Toast.LENGTH_LONG).show();
		}

		// Allow activity to manage lifetime of cursor
		startManagingCursor(cursor);

		// Setup mapping from cursor to view feilds
		String[] fromFeildNames = new String[] { CampusDatabase.KEY_NAME,
				CampusDatabase.KEY_TYPE, CampusDatabase.KEY_IMAGE, CampusDatabase.KEY_INFO};

		int[] toViewIDs = new int[] { R.id.item_name, R.id.item_type,R.id.item_icon };

		SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this,R.layout.item_layout, cursor, fromFeildNames, toViewIDs);

		ListView myList = (ListView) findViewById(R.id.listViewFromDB);
		myList.setAdapter(myCursorAdapter);
		db.close();
	}
	
	public void registerListClickCallback(){
		ListView myList = (ListView) findViewById(R.id.listViewFromDB);
		myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long idInDB) {
				
				CampusDatabase tdb = new CampusDatabase(ViewSearchResults.this);
				tdb.open();
				Cursor c = tdb.getRow(idInDB);
				if(c.moveToFirst()){
					String rowid = tdb.getId(c);
					Intent intentBuildingInfo = new Intent(ViewSearchResults.this, ViewCampusInfoFromDB.class);
					intentBuildingInfo.putExtra(ROW_ID , rowid);
					startActivity(intentBuildingInfo);
					
				}
			}
		
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_search_results, menu);
		return true;
	}

}

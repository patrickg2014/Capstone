package com.cs440.capstone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCampusInfoFromDB extends Activity {

	private long rowid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_campus_info_from_db);
		
		Intent intent = getIntent();
		String strrow = intent.getStringExtra(ViewSearchResults.ROW_ID);
		rowid = Long.parseLong(strrow);
		
		popluateViewFromDB();
	}
	
	public void popluateViewFromDB(){
		CampusDatabase tdb = new CampusDatabase(this);
		tdb.open();
		Cursor c = tdb.getRow(rowid);
		
		
		if(c.moveToFirst()){
			TextView tvBuildingName = (TextView) findViewById(R.id.tv_building_name);
			TextView tvBuildingType = (TextView) findViewById(R.id.tv_building_type);
			ImageView ivIcon = (ImageView) findViewById(R.id.info_icon);
			TextView tvBuildingInfo = (TextView) findViewById(R.id.tvBuildingInfo);
			
			String name = tdb.getBuildingName(c);
			String type = tdb.getBuildingType(c);
			String image = tdb.getBuildingImage(c);
			String info = tdb.getBuildingInfo(c);
			
			tvBuildingName.setText(name);
			tvBuildingType.setText(type);
			tvBuildingInfo.setText(info);
			ivIcon.setImageResource(tdb.intPic);
			ivIcon.bringToFront();
		
		}
		tdb.close();
	}
	
	//Let's go back to the main activity when we press
	//the back button.
	@Override
	public void onBackPressed(){
		Intent i = new Intent(this, CampusInfoSearch.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_campus_info_from_db, menu);
		return true;
	}

}

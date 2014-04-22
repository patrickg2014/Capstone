package com.cs440.capstone;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


@SuppressLint("NewApi")
public class HelpActivity extends Activity{


	
	private String[] mOptionTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;
	public static ParseUser currentUser;
	
	List<DrawerItem> dataList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) // where our app sets up
	{
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "bh3zRUQ5KI43dx5dcES5s5RelhfunoxR1Q9p0MFa",
				"GeAe5yOfQPOZ3FwYOCHSJGn6ldAUIkRuXjY8koHD");
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		currentUser = ParseUser.getCurrentUser();
		setContentView(R.layout.help_activity);

		initDrawer(savedInstanceState);
		
		
	}

	public void initDrawer(Bundle savedInstanceState) {
		mTitle = "test";

		mOptionTitles = getResources().getStringArray(R.array.Menu);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mOptionTitles));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mTitle);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// initialize drawer
		dataList = new ArrayList<DrawerItem>();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// Add Drawer Item to dataList
		dataList.add(new DrawerItem("Map", R.drawable.map));
		dataList.add(new DrawerItem("Camera", R.drawable.ic_action_camera));
		dataList.add(new DrawerItem("Search", R.drawable.ic_action_search));
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			dataList.add(new DrawerItem("Log Out Of Facebook",
					R.drawable.ic_action_cloud));
		} else {
			dataList.add(new DrawerItem("Log In To Facebook",
					R.drawable.ic_action_cloud));
		}
		dataList.add(new DrawerItem("About", R.drawable.ic_action_about));
		dataList.add(new DrawerItem("Settings", R.drawable.ic_action_settings));
		dataList.add(new DrawerItem("Help", R.drawable.ic_action_help));
		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
				dataList);

		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}


	}

	public void cameraActivity() // what allows us to switch to the camera
									// activity on button click
	{

		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
	}

	public void aboutActivity() // what allows us to switch to the camera
								// activity on button click
	{

		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

	public void settingsActivity() // what allows us to switch to the camera
									// activity on button click
	{

		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void helpActivity() // what allows us to switch to the camera
								// activity on button click
	{

		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}

	public void buildingActivity(String buildingName, String snippet) {
		Intent intent = new Intent(this, BuildingInfoActivity.class);
		intent.putExtra("Name", buildingName);
		intent.putExtra("Snippet", snippet);
		startActivity(intent);
	}

	public void eventActivity(String eventname, String snippet) {
		Intent intent = new Intent(this, EventInfoActivity.class);
		intent.putExtra("Name", eventname);
		intent.putExtra("Snippet", snippet);
		startActivity(intent);
	}

	public boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method changes to the CameraActivity when the phone is rotated
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
		if (newConfig.orientation == newConfig.ORIENTATION_LANDSCAPE) {
			cameraActivity();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return false;
	}

	public void selectItem(int possition) {

		mDrawerList.setItemChecked(possition, true);
		if (dataList.get(possition).getItemName().contentEquals("Camera")) {
			Log.d("Test", "CameraTiime");
			cameraActivity();
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (dataList.get(possition).getItemName().contentEquals("About")) {
			Log.d("Test", "About screen");
			aboutActivity();
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (dataList.get(possition).getItemName().contentEquals("Settings")) {
			Log.d("Test", "Settings screen");
			settingsActivity();
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (dataList.get(possition).getItemName().contentEquals("Help")) {
			Log.d("test", "Help screen");
			helpActivity();
			mDrawerLayout.closeDrawer(mDrawerList);
		}

		if (dataList.get(possition).getItemName()
				.contentEquals("Log In To Facebook")
				&& ParseFacebookUtils.getSession() == null) {
			Log.d("Test", "facebook in");

			//login();

			Log.d("Test", "facebook should have fqled");
			dataList.get(possition).setItemName("Log Out Of Facebook");

		} else {
			if (dataList.get(possition).getItemName()
					.contentEquals("Log Out Of Facebook")) {
				Log.d("Test", "facebook out ");

				ParseUser.logOut();

				dataList.get(possition).setItemName("Log In To Facebook");
			}

		}
		mDrawerList.setItemChecked(possition, false);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

		}
	}

	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

}
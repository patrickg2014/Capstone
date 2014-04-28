package com.cs440.capstone;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BuildingInfoActivity extends Activity {

	ParseImageView image;
	ExpandableTextView text;

	private String[] mOptionTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;
	public String name;
	List<DrawerItem> dataList;
	private Bitmap bitmap;
	private LinearLayout ll;
	private Button button;
	private ArrayList<Event> events;
	private boolean eventsShown = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) // where our app sets up
	{
		super.onCreate(savedInstanceState);
		Log.d("building", "got to the building info activity");

		Parse.initialize(this, "bh3zRUQ5KI43dx5dcES5s5RelhfunoxR1Q9p0MFa",
				"GeAe5yOfQPOZ3FwYOCHSJGn6ldAUIkRuXjY8koHD");
		setContentView(R.layout.building_info_actvity);
		ll = (LinearLayout)findViewById(R.id.linearlayout);
		image = (ParseImageView) findViewById(R.id.imageViewParse);
		image.setPlaceholder(getResources().getDrawable(
				R.drawable.ic_action_cloud));
		text = (ExpandableTextView) findViewById(R.id.textView1);
		text.setText("");
		Intent intent = getIntent();
		name = intent.getStringExtra("Name");
		String description = intent.getStringExtra("Snippet");
		ActionBar ab = getActionBar();
		ab.setTitle(name);
		initDrawer(savedInstanceState);
		queryPhoto();
		text.setText(description);
		button = (Button)findViewById(R.id.eventbutton);
		button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	if(!eventsShown){
		    		makeEvents();
		    	}
		    }
		});
	}
	
	public void makeEvents(){
		Log.d("events","make Events!");
		//final int N = 10; // total number of textviews to add
		events = CampusInfo.events;
		int numberOfEvents = events.size();
		//final ExpandableTextView[] myTextViews = new ExpandableTextView[numberOfEvents]; // create an empty array;
		
		for(Event e: CampusInfo.events)	//loops through all a markers to see which ones are within a certain radius of us
   		{	
   			if(e.m.getPosition()!=null){
   			Log.d("test", "1");
   			double longi= e.m.getPosition().longitude; //converting locations to Doubles as to allow comparison
   			double lati = e.m.getPosition().latitude;
   			Building host = CampusInfo.getBuilding(name);
   			double longi1= host.bound.getCenter().longitude;
   			double lati1 =host.bound.getCenter().latitude;
   			double distance= Math.abs(longi-longi1)+Math.abs(lati-lati1);
   			if(distance<=.00075)	//check to make sure they are in the radius
//   					(longi1+lati1)-(longi-lati)<=.001)
   			{
   				Log.d("test", "2");
   			// create a new textview
				final ExpandableTextView rowTextView = new ExpandableTextView(
						this);
				rowTextView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.layer_card_background));
				// set some properties of rowTextView or something
				// rowTextView.setPaddingRelative(15, 15, 15, 15);
				rowTextView.setText(e.snipit);
				ll.addView(rowTextView);
   						}
   			}
   		}
		
		
		eventsShown = true;
	}

	public void queryPhoto() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Building");
		ParseFile parseFile = null;
		query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
		query.whereEqualTo("name", name).whereEqualTo("image", parseFile);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects,
					com.parse.ParseException e) {
				if (e == null) {
					Log.d("query", "Retrieved " + objects.size() + " buildings");
					ParseFile photoFile = objects.get(0).getParseFile("image");
					if (photoFile != null) {
						image.setParseFile(photoFile);
						image.loadInBackground(new GetDataCallback() {
							@Override
							public void done(byte[] data, ParseException e) {
								image.setVisibility(View.VISIBLE);
							}
						});
					}

				} else {
					Log.d("query", "Error: " + e.getMessage());
				}
			}
		});
		//text.setText(textField);

	}

	@SuppressLint("NewApi")
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
		dataList.add(new DrawerItem("Tour", R.drawable.ic_action_gamepad));
		dataList.add(new DrawerItem("Navigate", R.drawable.ic_action_labels));
		dataList.add(new DrawerItem("Search", R.drawable.ic_action_search));
		if ((MainActivity.currentUser != null)
				&& ParseFacebookUtils.isLinked(MainActivity.currentUser)) {
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

	public void selectItem(int possition) {

		mDrawerList.setItemChecked(possition, true);
		if (dataList.get(possition).getItemName().contentEquals("Camera")) {
			Log.d("Test", "CameraTiime");
			finish();
			cameraActivity();
		}
		if (dataList.get(possition).getItemName().contentEquals("Facebook")) {
			Log.d("Test", "CameraTiime");
			// Intent intent = new Intent(MainActivity.this,
			// LoginUsingLoginFragmentActivity.class);
			// startActivity(intent);

		}
		if (dataList.get(possition).getItemName().contentEquals("About")) {
			Log.d("Test", "OPENGLLLLLLLLLLL");
			// Intent intent = new Intent(MainActivity.this,
			// OpenGlActivity.class);
			// startActivity(intent);

		}
		mDrawerList.setItemChecked(possition, false);
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

		}
	}
	
}

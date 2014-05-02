package com.cs440.capstone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements
		OnInfoWindowClickListener, SensorEventListener, OnQueryTextListener {

	public static final float heading = 0;
	public ArrayList<ArrayList<Marker>> keepers = new ArrayList();
	public ArrayList<ArrayList> listoflists = new ArrayList();
	private SensorManager mSensorManager;
	ArrayList<Marker> allMarkers = new ArrayList();
	ArrayList<Marker> currentlyvisable = new ArrayList();
	public static long shareTime;
	static GoogleMap map = null;
	public static ParseUser currentUser;
	public LatLng lastqueryloc = null;
	static LatLng myLocation = null;
	boolean maptouch = false;
	private String[] mOptionTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private long querytimer = 0;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;
	private long timer;
	List<DrawerItem> dataList;
	private SearchView searchView;
	private CampusInfo campusInfo;
	private MenuItem searchMenuItem;
	static LatLng shareloc;
	public static ArrayList<String> uids = null;
	public LatLng lastPlacesQuery = new LatLng(0,0);

	@Override
	protected void onCreate(Bundle savedInstanceState) // where our app sets up
	{
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "bh3zRUQ5KI43dx5dcES5s5RelhfunoxR1Q9p0MFa",
				"GeAe5yOfQPOZ3FwYOCHSJGn6ldAUIkRuXjY8koHD");
		ParseAnalytics.trackAppOpened(getIntent());
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		currentUser = ParseUser.getCurrentUser();

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		timer = System.currentTimeMillis();
		querytimer = System.currentTimeMillis() - 28000;
		Display display = getWindowManager().getDefaultDisplay();

		setContentView(R.layout.activity_main);
		// Map

		// Get a handle to the Map Fragment

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)) // sets
																				// up
																				// the
																				// map
																				// view
																				// we
																				// have
				.getMap();
		map.setOnInfoWindowClickListener(this);
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

		campusInfo = new CampusInfo(map, this);
		// CampusInfo.createMarkers();

		map.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng arg0) {
				// TODO Auto-generated method stub
				Log.d("maps", "should be unlocked");
				maptouch = true;

			}

		});

		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				if (maptouch == false) {
					Context context = getApplicationContext();
					CharSequence text = "Free to roam!";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				maptouch = true;

				return false;
			}

		});

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				Log.d("maps", "should be unlocked");
				if (maptouch == false) {
					Context context = getApplicationContext();
					CharSequence text = "Free to roam!";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				maptouch = true;

			}

		});

		map.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
			@Override
			public boolean onMyLocationButtonClick() {
				Log.d("maps", "should be locked");
				if (maptouch == true) {
					Context context = getApplicationContext();
					CharSequence text = "Locked on your location";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}

				maptouch = false;

				return true;

			}
		});

		int orientation = display.getRotation();
		
		initDrawer(savedInstanceState);

		if (ParseFacebookUtils.getSession() != null) {

			ParseException err = null;

			friendcall(ParseFacebookUtils.getSession(), ParseFacebookUtils
					.getSession().getState(), err);

			ParseInstallation installation = ParseInstallation
					.getCurrentInstallation();
			installation.put("user", ParseUser.getCurrentUser());
			installation.saveInBackground();

		}
		

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
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)
				&& ParseFacebookUtils.getSession().isOpened()) {
			dataList.add(new DrawerItem("Log Out Of Facebook",
					R.drawable.facebooklogosmall));
		} else if ((currentUser != null)
				&& ParseFacebookUtils.isLinked(currentUser)
				&& !ParseFacebookUtils.getSession().isOpened()) {
			dataList.add(new DrawerItem("Log In To Facebook",
					R.drawable.facebooklogosmall));
		} else {
			dataList.add(new DrawerItem("Log In To Facebook",
					R.drawable.facebooklogosmall));

		}
		dataList.add(new DrawerItem("Share Location",
				R.drawable.ic_action_place));
		dataList.add(new DrawerItem("Remove Your Location",
				R.drawable.ic_action_place));
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

		mDrawerLayout.openDrawer(mDrawerList);

	}
	
	public void placesQuery(HashMap<String,Object> params){
		Log.d("places", "yay");
	
		if (myLocation!=null) {
			params.put("location", myLocation.latitude + ","
					+ myLocation.longitude);
			ParseCloud.callFunctionInBackground("placesRequest", params,
					new FunctionCallback<Object>() {

						@Override
						public void done(Object object, ParseException e) {
							// TODO Auto-generated method stub
							try {
								JSONObject jo= new JSONObject((String)object);
								parseJSON((JSONObject) jo);
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							Log.d("places", object.toString());
						}
					});
		}
	}
	
	public void parseJSON(JSONObject jsonob) throws JSONException{
		
		
		JSONArray array = jsonob.getJSONArray("results");
		Log.d("places1",array.length()+"");
		// loop
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			JSONObject jb1 = new JSONObject(obj
					.getString("geometry"));
			JSONObject jb2 = new JSONObject(jb1.getString("location"));
			double lat= jb2.getDouble("lat");
			double lng= jb2.getDouble("lng");
			String name = obj.getString("name");
			String pic = obj.getString("icon");
			Building b = new Building(name, "", true, new LatLngBounds(new LatLng(lat,lng), new LatLng(lat,lng)));
			campusInfo.all.add(b);
			Log.d("places1", name);
			campusInfo.map.addMarker(new MarkerOptions()
			.title(name)
			.snippet("")
			.position(
					new LatLng(
							lat,
							lng))
			.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
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

	public void searchActivity() {

		Intent intent = new Intent(this, CampusInfoSearch.class);
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

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchMenuItem = menu.findItem(R.id.search);
		searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(this);
		// Log.d("Search",searchView.getQuery()+"");
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
		if (dataList.get(possition).getItemName()
				.contentEquals("Share Location")) {
			Log.d("Test", "share prompt");

			if (ParseUser.getCurrentUser() != null
					&& !ParseFacebookUtils.getSession().isClosed()) {

				sharePrompt();

			}

			if (ParseUser.getCurrentUser() == null
					|| ParseFacebookUtils.getSession().isClosed()) {
				Toast.makeText(
						this,
						"You Need to be logged in to Facebook to share your location with Friends",
						Toast.LENGTH_LONG).show();
			}
			mDrawerLayout.closeDrawer(mDrawerList);
		}

		if (dataList.get(possition).getItemName().contentEquals("About")) {
			Log.d("Test", "About screen");
			aboutActivity();
			mDrawerLayout.closeDrawer(mDrawerList);
		}

		if (dataList.get(possition).getItemName()
				.contentEquals("Remove Your Location")) {
			Log.d("Test", "Remove Location");
			removeLocation();
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
				.contentEquals("Log In To Facebook")) {
			Log.d("Test", "facebook in");
			if (ParseUser.getCurrentUser() == null
					|| ParseFacebookUtils.getSession().isClosed()) {
				login(possition);
			}
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

	private void removeLocation() {
		// TODO Auto-generated method stub
		ParseUser user = ParseUser.getCurrentUser();
		if (ParseUser.getCurrentUser() != null) {
			user.put("shareLocation", false);

			user.saveInBackground(new SaveCallback() {
				public void done(com.parse.ParseException e) {
					if (e == null) {
						// Save was successful!
						Log.d("parse", "upload location");
					} else {
						// Save failed. Inspect e for details.
						Log.d("parse", "failed to upload location");
					}
				}
			});
			Toast.makeText(this, "No longer sharing your location.",
					Toast.LENGTH_SHORT).show();

		}
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

	public class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

		}
	}
	


	public void onSensorChanged(SensorEvent event) {

		if (System.currentTimeMillis() - timer > 150 && !maptouch) {
			
			
			int heading = ((Math.round(event.values[0] + event.values[2])) % 360); // Rounds
																					// the
																					// current
																					// heading
																					// to
																					// full
																					// degrees
			Log.d("map", heading + "");
			centerMapOnMyLocation();
			if (myLocation != null) {
				Location temp = new Location("Location");
				temp.setLatitude(myLocation.latitude);
				temp.setLongitude(myLocation.longitude);
				Location temp2 = new Location("PlacesLocation");
				temp2.setLatitude(lastPlacesQuery.latitude);
				temp2.setLongitude(lastPlacesQuery.longitude);
				Log.d("places", temp.distanceTo(temp2) + "");
				if (temp.distanceTo(temp2)>400) {
					HashMap<String, Object> params = new HashMap<String, Object>();
					placesQuery(params);
					lastPlacesQuery = myLocation;
				}
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(myLocation.latitude,
								myLocation.longitude)) // Sets the center of the
														// map to Mountain View
						.zoom(18) // Sets the zoom
						.bearing(heading) // Sets the orientation of the camera
											// to east
						.tilt(67) // Sets the tilt of the camera to 30 degrees
						.build(); // Creates a CameraPosition from the builder

				map.animateCamera(
						CameraUpdateFactory.newCameraPosition(cameraPosition),
						130, null);
				timer = System.currentTimeMillis();

			}

		}
		if (System.currentTimeMillis() - querytimer > 30000) {
			if (myLocation != null) {
				ParseUser user = ParseUser.getCurrentUser();

				ParseGeoPoint geo = new ParseGeoPoint();
				geo.setLatitude(myLocation.latitude);
				geo.setLongitude(myLocation.longitude);
				if (ParseUser.getCurrentUser() != null) {
					user.put("Location", geo);
					user.put("LocationString", myLocation.latitude+","+myLocation.longitude);
					user.saveInBackground(new SaveCallback() {
						public void done(com.parse.ParseException e) {
							if (e == null) {
								// Save was successful!
								Log.d("parse", "upload location");
							} else {
								// Save failed. Inspect e for details.
								Log.d("parse", "failed to upload location");
							}
						}
					});
					querytimer = System.currentTimeMillis();

				}

				if (ParseFacebookUtils.getSession() != null
						&& myLocation != null) {

					if (lastqueryloc == null) {
						lastqueryloc = new LatLng(0, 0);
					}
					Double distance = Math.abs(myLocation.latitude
							- lastqueryloc.latitude)
							+ Math.abs(myLocation.longitude
									- lastqueryloc.longitude);
					ParseException err = null;
					/*
					 * if(ParseFacebookUtils.getSession().isOpened()){ friendcall(ParseFacebookUtils.getSession(), ParseFacebookUtils.getSession().getState(),err); }
					 */
					if (distance > 1) {
						Log.d("dis", distance + "");

						lastqueryloc = myLocation;
						call(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);
						if (ParseFacebookUtils.getSession().isOpened()) {
							if (lastqueryloc == null) {
								lastqueryloc = new LatLng(0, 0);
							}

							friendcall(ParseFacebookUtils.getSession(),
									ParseFacebookUtils.getSession().getState(),
									err);
							if (distance > 1) {
								Log.d("dis", distance + "");

								lastqueryloc = myLocation;
								call(ParseFacebookUtils.getSession(),
										ParseFacebookUtils.getSession()
												.getState(), err);
							}

						}
					}
				}
				if (shareloc != null && shareTime != 0) {
					if (System.currentTimeMillis() - shareTime > 10000000
							|| Math.abs(myLocation.latitude - shareloc.latitude)
									- Math.abs(myLocation.longitude
											- shareloc.longitude) > .001) {
						user = ParseUser.getCurrentUser();

						if (ParseUser.getCurrentUser() != null) {

							user.put("shareLocation", false);
							user.saveInBackground(new SaveCallback() {
								public void done(com.parse.ParseException e) {
									if (e == null) {
										// Save was successful!
										Log.d("parse",
												"upload location and text");
									} else {
										// Save failed. Inspect e for details.
										Log.d("parse",
												"failed to upload location");
									}
								}
							});

						}
					}
				}
			}
		}
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		boolean building = false;
		for (Building b : CampusInfo.all) {
			if (b.m.equals(arg0)) {
				buildingActivity(arg0.getTitle(), arg0.getSnippet());
				building = true;
				break;
			}
		}
		if (!building) {
			for (Event e : CampusInfo.events) {
				if (e.m.equals(arg0)) {
					eventActivity(arg0.getTitle(), arg0.getSnippet());
					break;
				}
			}
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);

	}

	static void centerMapOnMyLocation() {

		map.setMyLocationEnabled(true);

		Location location = map.getMyLocation();

		myLocation = null;
		if (location != null) {
			myLocation = new LatLng(location.getLatitude(),
					location.getLongitude());
		}

	}

	public static void call(Session session, SessionState state, Exception exception) {
		if (ParseFacebookUtils.getSession().isOpened() && myLocation != null) {
			String fqlQuery =

			"SELECT name, pic_big,venue, description, start_time, end_time, eid "
					+ "FROM event " + "WHERE eid IN (" + "SELECT eid "
					+ "FROM event_member " + "WHERE (uid IN (" + "SELECT uid2 "
					+ "FROM friend " + "WHERE uid1 = me())  "
					+ "OR uid = me())limit 10000) "
					+ "AND  (end_time>now() OR start_time>now()) "
					+ "AND venue.latitude > \""
					+ (CampusInfo.map.getMyLocation().getLatitude() - .1)
					+ "\"" + "AND venue.latitude < \""
					+ (CampusInfo.map.getMyLocation().getLatitude() + .1)
					+ "\"" + "AND venue.longitude < \""
					+ (CampusInfo.map.getMyLocation().getLongitude() + .1)
					+ "\"" + "AND venue.longitude  >\""
					+ (CampusInfo.map.getMyLocation().getLongitude() - .1)
					+ "\"";
			Bundle params = new Bundle();

			params.putString("q", fqlQuery);

			Session session1 = Session.getActiveSession();

			Request request = new Request(session1, "/fql", params,
					HttpMethod.GET, new Request.Callback() {
						public void onCompleted(Response response) {
							// Log.i("fql", "Got results: " + response.toString());

							try {
								GraphObject go = response.getGraphObject();
								JSONObject jso = go.getInnerJSONObject();
								JSONArray array = jso.getJSONArray("data");

								// loop
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.getJSONObject(i);
									JSONObject jb1 = new JSONObject(obj
											.getString("venue"));
									String name = obj.getString("name");
									String pic = obj.getString("pic_big");
								
									String startTime = obj.getString("start_time");
									if(startTime.length()<=10){
										
										startTime="there is no set start time";
									
									}
									else
									{
										
										SimpleDateFormat incomingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
										Date date = incomingFormat.parse(startTime);

										SimpleDateFormat outgoingFormat1 = new SimpleDateFormat("EEEE MMMM dd", java.util.Locale.getDefault());
										SimpleDateFormat outgoingFormat2 = new SimpleDateFormat("hh:mm", java.util.Locale.getDefault());
										startTime=outgoingFormat2.format(date) +" on "+ outgoingFormat1.format(date);
										
									
									}
									
									String endTime = obj.getString("end_time");
									if(endTime.length()<=10){
										
										endTime="there is no set end time";
								
									}else{
									
										
										
										SimpleDateFormat incomingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
										Date date = incomingFormat.parse(endTime);

										SimpleDateFormat outgoingFormat1 = new SimpleDateFormat("EEEE MMMM dd", java.util.Locale.getDefault());
										SimpleDateFormat outgoingFormat2 = new SimpleDateFormat("hh:mm", java.util.Locale.getDefault());
										endTime=outgoingFormat2.format(date) +" on "+ outgoingFormat1.format(date);
									}
									String description = obj
											.getString("description");

									String lat = jb1.getString("latitude");
									String longi = jb1.getString("longitude");
									CampusInfo.events.add(new Event(name,
											description, new LatLng(Double
													.parseDouble(lat), Double
													.parseDouble(longi)), pic, startTime, endTime));
									Log.d("fql", name + " " + description + " ");
								}

							} catch (Throwable t) {
								t.printStackTrace();
							}
						}
					});

			Request.executeBatchAsync(request);
			for (Event e : CampusInfo.events) {
				e.makeMarker();

			}
		}

	}

	public static void friendcall(Session session, SessionState state,
			Exception exception) {

		String fqlQuery = "SELECT uid, name FROM user WHERE  is_app_user  AND uid IN (SELECT uid2 FROM friend WHERE uid1 = me())";
		final List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
		uids = new ArrayList<String>();
		Bundle params = new Bundle();

		params.putString("q", fqlQuery);

		Session session1 = Session.getActiveSession();

		Request request = new Request(session1, "/fql", params, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						// Log.i("fql", "Got results: " + response.toString());

						try {
							GraphObject go = response.getGraphObject();
							JSONObject jso = go.getInnerJSONObject();
							JSONArray array = jso.getJSONArray("data");

							// loop
							for (int i = 0; i < array.length(); i++) {

								String uid = array.getJSONObject(i).getString(
										"uid");
								String name = array.getJSONObject(i).getString(
										"name");
								Log.d("uid", uid + "");

								ParseQuery<ParseUser> query = ParseUser
										.getQuery();
								query.whereEqualTo("Uid", uid).whereEqualTo(
										"shareLocation", true);

								query.findInBackground(new FindCallback<ParseUser>() {
									public void done(List<ParseUser> objects,
											ParseException e) {
										Log.d("share",
												"Inside done" + objects.size());

										Log.d("query",
												"Retrieved " + objects.size()
														+ " friends locations");
										for (int i = 0; i < objects.size(); i++) {
											// String id = results.get(i).getObjectId();
											Log.d("share",
													"inside the for loop");
											String name = objects.get(i)
													.getString("Name");
											String loctext = objects.get(i)
													.getString("locationText");
											double sharelat = objects
													.get(i)
													.getParseGeoPoint(
															"Location")
													.getLatitude();
											double sharelong = objects
													.get(i)
													.getParseGeoPoint(
															"Location")
													.getLongitude();
											Log.d("share", name + "    "
													+ loctext);
											CampusInfo.map
													.addMarker(new MarkerOptions()
															.title(name)
															.snippet(loctext)
															.position(
																	new LatLng(
																			sharelat,
																			sharelong))
															.icon(BitmapDescriptorFactory
																	.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

										}
									}
								});
								Log.d("share", name + " " + uid);
							}

						} catch (Throwable t) {
							t.printStackTrace();
						}
					}
				});

		Request.executeBatchAsync(request);
		for (Event e : CampusInfo.events) {
			e.makeMarker();

		}

	}

	public static void storecall(Session session, SessionState state,
			Exception exception) {

		String fqlQuery = "SELECT uid,name FROM user WHERE uid = me() ";

		Bundle params = new Bundle();

		params.putString("q", fqlQuery);

		Session session1 = Session.getActiveSession();

		Request request = new Request(session1, "/fql", params, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						// Log.i("fql", "Got results: " + response.toString());

						try {
							GraphObject go = response.getGraphObject();
							JSONObject jso = go.getInnerJSONObject();
							JSONArray array = jso.getJSONArray("data");

							// loop
							for (int i = 0; i < array.length(); i++) {
								String uid = array.getJSONObject(i).getString(
										"uid");
								String name = array.getJSONObject(i).getString(
										"name");
								ParseUser user = ParseUser.getCurrentUser();
								Log.d("share", uid + "");
								ParseInstallation installation = ParseInstallation
										.getCurrentInstallation();
								installation.put("uid", uid);
								installation.saveInBackground();

								if (ParseUser.getCurrentUser() != null) {
									user.put("Uid", uid);

									user.put("Name", name);
									// Store app language and version

									user.saveInBackground(new SaveCallback() {
										public void done(
												com.parse.ParseException e) {
											if (e == null) {
												// Save was successful!
												Log.d("parse", "upload uid");
											} else {
												// Save failed. Inspect e for details.
												Log.d("parse",
														"failed to upload uid");
											}
										}

									});
									//
									// ParseInstallation installation = ParseInstallation.getCurrentInstallation();
									// installation.put("uid",uid);
									// installation.saveInBackground();
									Log.d("fql1", uid + "");
								}

							}
						} catch (Throwable t) {
							t.printStackTrace();
						}
					}
				});

		Request.executeBatchAsync(request);

	}

	public void login(final int possition) {
		if (ParseUser.getCurrentUser() == null
				|| ParseFacebookUtils.getSession().isClosed()) {

			List<String> permissions = Arrays
					.asList("user_photos, user_events, user_friends, user_location, user_activities, friends_events");
			ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException err) {

					if (user == null) {
						Log.d("facebook",
								"Uh oh. The user cancelled the Facebook login.");
						Log.d("Test", "facebook should have fqled");
						dataList.get(possition).setItemName(
								"Log In To Facebook");

					} else if (user.isNew()) {
						Log.d("facebook",
								"User signed up and logged in through Facebook!");
						Log.d("Test", "facebook should have fqled");
						dataList.get(possition).setItemName(
								"Log Out Of Facebook");
						storecall(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);
						friendcall(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);
						call(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);

						ParseFacebookUtils.saveLatestSessionData(currentUser);
						ParseInstallation installation = ParseInstallation
								.getCurrentInstallation();
						installation.put("user", ParseUser.getCurrentUser());
						installation.saveInBackground();

					} else {
						Log.d("facebook", "User logged in through Facebook!");
						Log.d("Test", "facebook should have fqled");
						dataList.get(possition).setItemName(
								"Log Out Of Facebook");
						storecall(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);
						friendcall(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);

						call(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);
						ParseInstallation installation = ParseInstallation
								.getCurrentInstallation();
						installation.put("user", ParseUser.getCurrentUser());
						installation.saveInBackground();

					}
				}
			});
			if (ParseFacebookUtils.getSession().isOpened()) {
				dataList.get(possition).setItemName("Log Out Of Facebook");
			} else {
				dataList.get(possition).setItemName("Log In To Facebook");
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		Log.d("change", arg0);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		hideKeys(); // drop the keybord
		arg0.toLowerCase();

		ArrayList<Building> theAllList = campusInfo.getAllList();
		// ArrayList<Marker> currentlyVisable =
		// campusInfo.getCurrentlyVisableList();

		Log.d("Search", "Size: " + theAllList.size());

		for (int i = 0; i < theAllList.size(); i++) {
			Log.d("Search", "" + theAllList.get(i).getTitle() + "=" + arg0
					+ "?");

			if (theAllList.get(i).getTitle().toLowerCase().contains(arg0)) {
				buildingActivity(theAllList.get(i).getTitle(), theAllList
						.get(i).getSnipit());
				break;
			}
			if (i == theAllList.size() - 1) {
				Toast.makeText(this, "Nothing found.", Toast.LENGTH_LONG)
						.show();
			}
		}
		Log.d("Search", arg0);

		return false;
	}

	public void hideKeys() {
		searchView.setIconified(true);
		searchMenuItem.collapseActionView();
		this.getCurrentFocus().clearFocus();
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(findViewById(android.R.id.content)
				.getWindowToken(), 0);
	}

	public void sharePrompt() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		Log.d("prompt", "this should happen every time");

		alert.setTitle("Share your location");
		alert.setMessage("Message");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Share", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				ParseUser user = ParseUser.getCurrentUser();
				String valstring = value.toString();
				ParseGeoPoint geo = new ParseGeoPoint();
				geo.setLatitude(myLocation.latitude);
				geo.setLongitude(myLocation.longitude);
				if (ParseUser.getCurrentUser() != null) {
					user.put("Location", geo);
					user.put("LocationString", myLocation.latitude+","+myLocation.longitude);
					user.put("locationText", valstring);
					user.put("shareLocation", true);
					user.saveInBackground(new SaveCallback() {
						public void done(com.parse.ParseException e) {

							// Save was successful!
							Log.d("parse", "upload location and text");

							

						}
					});
					shareTime = System.currentTimeMillis();
					shareloc = myLocation;

				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();

	}

}
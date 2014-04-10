package com.cs440.capstone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CameraPosition.Builder;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnInfoWindowClickListener, SensorEventListener{

	public static final float heading = 0;
	public ArrayList<ArrayList<Marker>> keepers = new ArrayList();
	public ArrayList<ArrayList> listoflists = new ArrayList();
	private SensorManager mSensorManager;
	ArrayList<Marker> allMarkers = new ArrayList();
	ArrayList<Marker> currentlyvisable = new ArrayList();
	public LoginUsingLoginFragmentActivity logIn;
	static GoogleMap map = null;
	public ParseUser currentUser;
	
	static LatLng myLocation = null;
	boolean maptouch= false;
	 private String[] mOptionTitles;
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;
    private long timer;
    List<DrawerItem> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) //where our app sets up
		{
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "bh3zRUQ5KI43dx5dcES5s5RelhfunoxR1Q9p0MFa",
				"GeAe5yOfQPOZ3FwYOCHSJGn6ldAUIkRuXjY8koHD");
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)){
			
			query( ParseFacebookUtils.getSession() ,  ParseFacebookUtils.getSession().getState());
			Log.d("supertest", "this should have fqled");
		}
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		timer = System.currentTimeMillis();
		Display display = getWindowManager().getDefaultDisplay();
		int orientation = display.getRotation();
		if(orientation==3||orientation==1)
		{
			finish();
			cameraActivity();
		}
		setContentView(R.layout.activity_main);
		// Map

		// Get a handle to the Map Fragment
	
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))	//sets up the map view we have
				.getMap();
		map.setOnInfoWindowClickListener(this);
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		
		

		CampusInfo campusInfo = new CampusInfo(map);
		CampusInfo.createMarkers();
		
		 initDrawer(savedInstanceState);
		 map.setOnMapClickListener(new OnMapClickListener() {

		        @Override
		        public void onMapClick(LatLng arg0) {
		            // TODO Auto-generated method stub
		        	if(map.getCameraPosition().target!=myLocation){
						Log.d("maps","it should allow me to move it");
						maptouch=true;
						}
						else{
							Log.d("maps","it should be locked on my location");
							maptouch=false;
						}
		        }
		    });
				
			
	}
	
	public void initDrawer(Bundle savedInstanceState){
		mTitle = "test";
		 
	 	mOptionTitles = getResources().getStringArray(R.array.Menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOptionTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
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

	
	//initialize drawer
	dataList = new ArrayList<DrawerItem>();
    mTitle = mDrawerTitle = getTitle();
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
 // Add Drawer Item to dataList
    dataList.add(new DrawerItem("Map", R.drawable.ic_action_email));
    dataList.add(new DrawerItem("Camera", R.drawable.ic_action_camera));
    dataList.add(new DrawerItem("Tour", R.drawable.ic_action_gamepad));
    dataList.add(new DrawerItem("Navigate", R.drawable.ic_action_labels));
    dataList.add(new DrawerItem("Search", R.drawable.ic_action_search));
    if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
    dataList.add(new DrawerItem("Log Out Of Facebook", R.drawable.ic_action_cloud));
    }else{
    	dataList.add(new DrawerItem("Log In To Facebook", R.drawable.ic_action_cloud));
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
    
    mDrawerLayout.openDrawer(mDrawerList);
	
	}
	
	
	public void cameraActivity() //what allows us to switch to the camera activity on button click
	{
		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
	}
	
	public void buildingActivity(String buildingName, String snippet){
		Intent intent = new Intent(this, BuildingInfoActivity.class);
		intent.putExtra("Name", buildingName);
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
	  if(newConfig.orientation == newConfig.ORIENTATION_LANDSCAPE){
		  cameraActivity();
	  }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		// Associate searchable configuration with the SearchView
	    SearchManager searchManager =
	           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView =
	            (SearchView) menu.findItem(R.id.search).getActionView();
	    searchView.setSearchableInfo(
	            searchManager.getSearchableInfo(getComponentName()));
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
        if(dataList.get(possition).getItemName().contentEquals("Camera")){
        	Log.d("Test", "CameraTiime");
        	cameraActivity();
        }
        if(dataList.get(possition).getItemName().contentEquals("Settings")){
        	Intent intent = new Intent(this, LoginActivity.class);
    		startActivity(intent);
        }
        if(dataList.get(possition).getItemName().contentEquals("Log In To Facebook") && currentUser == null){
        	Log.d("Test", "facebook in");
        	
        	 onLoginButtonClicked();
             dataList.get(possition).setItemName("Log Out Of Facebook");
        	
        }
        else{
        	if(dataList.get(possition).getItemName().contentEquals("Log Out Of Facebook")){
        	Log.d("Test", "facebook out ");
        	 
        	 ParseUser.logOut();
             dataList.get(possition).setItemName("Log In To Facebook");
        	}
        	
        }
       
        mDrawerLayout.closeDrawer(mDrawerList);

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
	
	
public void onSensorChanged(SensorEvent event) {
		
		if(System.currentTimeMillis()-timer>90){
		if(maptouch=true){
		int heading =  ((Math.round(event.values[0]+event.values[2]))%360); //Rounds the current heading to full degrees
		Log.d("map",heading+"");
		centerMapOnMyLocation();
		if(myLocation!=null){
		CameraPosition cameraPosition = new CameraPosition.Builder()
	    .target(new LatLng(myLocation.latitude,myLocation.longitude))      // Sets the center of the map to Mountain View
	    .zoom(18)                   // Sets the zoom
	    .bearing(heading)                // Sets the orientation of the camera to east
	    .tilt(67)                   // Sets the tilt of the camera to 30 degrees
	    .build();                   // Creates a CameraPosition from the builder
		
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),75,null);
		timer=System.currentTimeMillis();
		}
		}
		}
	}



	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		buildingActivity(arg0.getTitle(),arg0.getSnippet());
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
 

    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
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
    private void onLoginButtonClicked() {
		
		List<String> permissions = Arrays.asList("user_photos, user_events, user_friends, user_location, user_activities, friends_events");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				
				if (user == null) {
					Log.d("facebook",
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d("facebook",
							"User signed up and logged in through Facebook!");
					query( ParseFacebookUtils.getSession() ,  ParseFacebookUtils.getSession().getState());
					
				} else {
					Log.d("facebook",
							"User logged in through Facebook!");
					query( ParseFacebookUtils.getSession() ,  ParseFacebookUtils.getSession().getState());
				}
			}
		});
		
	}
    public void query(Session session, SessionState state) {
    	Log.d("supertest", "this should have fqled");
     	String fqlQuery = 
     			
     			"SELECT name,  venue, description, start_time,end_time, eid " +
              			"FROM event " +
              			"WHERE eid IN (" +
              			"SELECT eid " +
              			"FROM event_member " +
              			"WHERE (uid IN (" +
              			"SELECT uid2 " +
              			"FROM friend " +
              			"WHERE uid1 = me())  " +
              			"OR uid = me())limit 10000) " +
              			"AND  (end_time>now() OR start_time>now()) "+
              			"AND venue.latitude > \""+(CampusInfo.map.getMyLocation().getLatitude()-.1)+"\""+
              			"AND venue.latitude < \""+(CampusInfo.map.getMyLocation().getLatitude()+.1)+"\""+
              			"AND venue.longitude < \""+(CampusInfo.map.getMyLocation().getLongitude()+.1)+"\""+
              			"AND venue.longitude  >\""+(CampusInfo.map.getMyLocation().getLongitude()-.1)+"\"";
     	Bundle params = new Bundle();
     	
     	params.putString("q", fqlQuery);
     	
     	Session session1 = Session.getActiveSession();
     	
     	
     	 
     	Request request = new Request(session1, 
     	    "/fql", 
     	    params, 
     	    HttpMethod.GET, 
     	    new Request.Callback(){ 
     	        public void onCompleted(Response response) {
     	        //Log.i("fql", "Got results: " + response.toString());
     	       
     	        try
     	        {
     	            GraphObject go  = response.getGraphObject();
     	            JSONObject  jso = go.getInnerJSONObject();
     	            JSONArray array =  jso.getJSONArray("data");

     	            		// loop
     	            		for(int i = 0; i < array.length(); i++) {
     	            		 JSONObject obj = array.getJSONObject(i);
     	            		 JSONObject jb1= new JSONObject(obj.getString("venue"));
     	            		 String name = obj.getString("name");
     	            		 String description = obj.getString("description");
     	            		 
     	            		 String lat = jb1.getString("latitude");
     	            		 String longi = jb1.getString("longitude");
     	            		 CampusInfo.events.add(new Event(name, description,new LatLng(Double.parseDouble(lat),Double.parseDouble(longi))));
     	            		 Log.d("fql", name+" "+description+" ");
     	            		}
     	               
     	                
     	            
     	        }
     	        catch ( Throwable t )
     	        {
     	            t.printStackTrace();
     	        }
     	    }
     	});
     	
     	Request.executeBatchAsync(request);
     	for(Building b: CampusInfo.all)
     	{
     		b.makeMarker();
     		
     	}

     }
   
	
	}

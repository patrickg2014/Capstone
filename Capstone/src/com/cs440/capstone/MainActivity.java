package com.cs440.capstone;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.facebook.model.GraphObject;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnInfoWindowClickListener{

	public ArrayList<ArrayList<Marker>> keepers = new ArrayList();
	public ArrayList<ArrayList> listoflists = new ArrayList();
	ArrayList<Marker> allMarkers = new ArrayList();
	ArrayList<Marker> currentlyvisable = new ArrayList();
	public LoginUsingLoginFragmentActivity logIn;
	GoogleMap map = null;
	
	 private String[] mOptionTitles;
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) //where our app sets up
		{
		super.onCreate(savedInstanceState);
		
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

		CampusInfo campusInfo = new CampusInfo(map);
		CampusInfo.createMarkers();
		
		query();
		 initDrawer(savedInstanceState);
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
    dataList.add(new DrawerItem("Facebook", R.drawable.ic_action_cloud));
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
        if(dataList.get(possition).getItemName().contentEquals("Facebook")){
        	Log.d("Test", "CameraTiime");
        	 Intent intent = new Intent(MainActivity.this, LoginUsingLoginFragmentActivity.class);
             startActivity(intent);
        	
        }
        if(dataList.get(possition).getItemName().contentEquals("About")){
        	
        	
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
	private void query() {
		
	       
	    // Create a new request for an HTTP GET with the
		if(LoginUsingLoginFragmentActivity.isLoggedIn()){
        	
        
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

	




	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		buildingActivity(arg0.getTitle(),arg0.getSnippet());
	}
 

}

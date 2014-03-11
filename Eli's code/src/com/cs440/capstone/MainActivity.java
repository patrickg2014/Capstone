package com.cs440.capstone;

import java.util.ArrayList;

import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ActionBar;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	public ArrayList<ArrayList<Marker>> keepers = new ArrayList();
	public ArrayList<ArrayList> listoflists = new ArrayList();
	ArrayList<Marker> allMarkers = new ArrayList();
	ArrayList<Marker> currentlyvisable = new ArrayList();
	GoogleMap map = null;
	
	 private String[] mOptionTitles;
	 private DrawerLayout mDrawerLayout;
	 private ListView mDrawerList;
	 private CharSequence mTitle;
	 private ActionBarDrawerToggle mDrawerToggle;

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

		CampusInfo campusInfo = new CampusInfo(map);
		CampusInfo.createMarkers();
		
		 mTitle = "test";
		 
		 	mOptionTitles = getResources().getStringArray(R.array.MainMenu);
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
	                getActionBar().setTitle(R.string.app_name);
	            }
	 
	            /** Called when a drawer has settled in a completely open state. */
	            public void onDrawerOpened(View drawerView) {
	                getActionBar().setTitle("Options");
	            }
	        };
	 
	        // Set the drawer toggle as the DrawerListener
	        mDrawerLayout.setDrawerListener(mDrawerToggle);
	 
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        getActionBar().setHomeButtonEnabled(true);
	}
	public void cameraActivity() //what allows us to switch to the camera activity on button click
	{
		Intent intent = new Intent(this, CameraActivity.class);
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
	  if(newConfig.orientation == newConfig.ORIENTATION_LANDSCAPE){
		  cameraActivity();
	  }
	  mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        
        return super.onOptionsItemSelected(item);
    }
 
    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
    	String choice = mOptionTitles[position];
        Toast.makeText(this, choice, Toast.LENGTH_SHORT).show();
        
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mOptionTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        if(choice.contentEquals("Camera")){
        	cameraActivity();
        }
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
 
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
      

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			selectItem(arg2);
			
		}
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

}

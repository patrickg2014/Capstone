package com.cs440.capstone;

import java.util.ArrayList;

import android.hardware.Camera;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.OrientationListener;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.app.ActionBar;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	public ArrayList<ArrayList<Marker>> keepers = new ArrayList();
	public ArrayList<ArrayList> listoflists = new ArrayList();
	ArrayList<Marker> allMarkers = new ArrayList();
	ArrayList<Marker> currentlyvisable = new ArrayList();
	private Button goToCam;
	public OrientationListener listen;
	GoogleMap map = null;
	private SurfaceHolder mHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) //where our app sets up
		{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		goToCam = (Button) findViewById(R.id.button1);	//sets up the button for going to the camera view this may switch to simply rotating the phone
		goToCam.setOnClickListener(new View.OnClickListener() //sets up a listener for that button
		{

			@Override
			public void onClick(View v) //on click we will go to the cameraActivity
			{
				// TODO Auto-generated method stub
				cameraActivity();
			}
		});

		// Map

		// Get a handle to the Map Fragment

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))	//sets up the map view we have
				.getMap();

		CampusInfo campusInfo = new CampusInfo(map);
		campusInfo.showMarkers();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

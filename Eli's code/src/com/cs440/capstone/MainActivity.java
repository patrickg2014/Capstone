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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.app.ActionBar;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	 public ArrayList<ArrayList<Marker>> keepers= new ArrayList();
	 public ArrayList<ArrayList> listoflists = new ArrayList();
	 ArrayList<Marker> allMarkers = new ArrayList ();
	 ArrayList<Marker> currentlyvisable = new ArrayList();
	private Button goToCam;
	GoogleMap map =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		goToCam = (Button) findViewById(R.id.button1);
		goToCam.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cameraActivity();
			}
		});
		
		// Map
		

        // Get a handle to the Map Fragment
		
        map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        
        CampusInfo campusInfo = new CampusInfo(map);
        campusInfo.showMarkers();
    	}
	
	public void whatshouldwesee()
	{
		Location myloc= map.getMyLocation();
		LatLng mylatlng = new LatLng(myloc.getLatitude(),myloc.getLongitude());
		currentlyvisable.clear();
		for(Marker m: allMarkers){
			double longi= m.getPosition().longitude;
			double lati = m.getPosition().latitude;
			double longi1= mylatlng.longitude;
			double lati1 =mylatlng.latitude;
			if((longi1+lati1)-(longi-lati)<=.001)
			{
				currentlyvisable.add(m);
			}
			
		}
	}

	
	public void cameraActivity(){
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



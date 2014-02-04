package com.cs440.capstone;

import android.hardware.Camera;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

@SuppressLint("NewApi")
public class MainActivity extends Activity {


	
	private Button goToCam;

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
		
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        
        //map.setMyLocationEnabled(true);

        
        LatLng pugetsound = new LatLng(47.2626, -122.4817);
        LatLng wheelock = new LatLng(47.263139,-122.478933);
        //LatLng sydney = new LatLng(map.getMyLocation().getLatitude(),
        //1		map.getMyLocation().getLatitude());

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pugetsound, 17));

        map.addMarker(new MarkerOptions()
        .title("Wheelock")
        .snippet("stuff")
        .position(wheelock));
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

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
	private EditText edittext;
	
	private Location currentLocation;
	GoogleMap map =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		edittext = (EditText) findViewById(R.id.editText1);
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
        
        
        
       // map.setMyLocationEnabled(true);

        
        LatLng pugetsound = new LatLng(47.2626, -122.4817);
<<<<<<< HEAD
        LatLng wheelock = new LatLng(47.263139,-122.478933);
=======
        LatLng sub = new LatLng (47.263144,-122.478944);
        LatLng jones = new LatLng (47.263635,-122.481138);
        LatLng mcintyre = new LatLng ( 47.264021,-122.480403);
        LatLng howarth = new LatLng (47.263257,-122.480435);
        LatLng music = new LatLng ( 47.263631,-122.48235);
        LatLng thompson = new LatLng ( 47.263668,-122.482882);
        LatLng harned = new LatLng (47.263668,-122.483466);
        LatLng collins = new LatLng (47.264389,-122.481723);
        LatLng wyatt = new LatLng (47.261859,-122.482608);
        LatLng pool = new LatLng (47.261276,-122.481685);
        LatLng weyerhaseuser = new LatLng (47.260482,-122.480537);
        LatLng todd = new LatLng (47.262404,-122.480832);
        LatLng regester = new LatLng ( 47.261938,-122.480628);
        LatLng seward = new LatLng (47.262004,-122.480081);
        LatLng trimble = new LatLng (47.26279,-122.480392);
        LatLng kilworth = new LatLng (47.26512,-122.481744);
        LatLng al = new LatLng (47.264843,-122.480779);
        LatLng schiff = new LatLng ( 47.265246,-122.480095);
        LatLng kittredge = new LatLng (47.263905,-122.478966);
        
>>>>>>> master
        //LatLng sydney = new LatLng(map.getMyLocation().getLatitude(),
        //1		map.getMyLocation().getLatitude());

        map.setMyLocationEnabled(true);
<<<<<<< HEAD
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pugetsound, 17));

        map.addMarker(new MarkerOptions()
        .title("Wheelock")
        .snippet("stuff")
        .position(wheelock));
=======

		
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pugetsound, 16));
        ArrayList<Marker> acadmicBuildings = new ArrayList ();
        ArrayList<Marker> dorms = new ArrayList ();
        ArrayList<Marker> art = new ArrayList ();
        ArrayList<Marker> general = new ArrayList ();
        
        listoflists.add(acadmicBuildings);
        listoflists.add(dorms);
        listoflists.add(general);
        listoflists.add(art);
        
        
        Marker subMarker= map.addMarker(new MarkerOptions()
        .title("The SUB")
        .snippet("Main building")
        .position(sub));
        general.add(subMarker);
        allMarkers.add(subMarker);
       Marker jonesMarker= map.addMarker(new MarkerOptions()
        .title("Jones hall")
        .snippet("some info")
        .position(jones));
       acadmicBuildings.add(jonesMarker);
       art.add(jonesMarker);
       general.add(jonesMarker);
       allMarkers.add(jonesMarker);
       Marker mcintyreMarker= map.addMarker(new MarkerOptions()
        .title("McIntyre hall")
        .snippet("some info")
        .position(mcintyre));
       acadmicBuildings.add(mcintyreMarker);
       general.add(mcintyreMarker);
       allMarkers.add(mcintyreMarker);
        Marker howarthMarker=map.addMarker(new MarkerOptions()
        .title("Howarth hall")
        .snippet("some info")
        .position(howarth));
        acadmicBuildings.add(howarthMarker);
        general.add(howarthMarker);
        allMarkers.add(howarthMarker);
        Marker musicMarker=map.addMarker(new MarkerOptions()
        .title("Music Building/Schneebeck Hall")
        .snippet("some info")
        .position(music));
        acadmicBuildings.add(musicMarker);
        art.add(musicMarker);
        allMarkers.add(musicMarker);
        Marker thompsonMarker= map.addMarker(new MarkerOptions()
        .title("Thompson Hall")
        .snippet("some info")
        .position(thompson));
        acadmicBuildings.add(thompsonMarker);
        allMarkers.add(thompsonMarker);
        allMarkers.add(howarthMarker);
        Marker harnedMarker=map.addMarker(new MarkerOptions()
        .title("Harned Hall")
        .snippet("some info")
        .position(harned));
        acadmicBuildings.add(harnedMarker);
        allMarkers.add(harnedMarker);
       Marker collinsMarker=  map.addMarker(new MarkerOptions()
        .title("Collins Memorial Libary")
        .snippet("some info")
        .position(collins));
       acadmicBuildings.add(collinsMarker);
       allMarkers.add(collinsMarker);
       general.add(collinsMarker);
        Marker wyattMarker=map.addMarker(new MarkerOptions()
        .title("Wyatt Hall")
        .snippet("some info")
        .position(wyatt));
        acadmicBuildings.add(wyattMarker);
        allMarkers.add(wyattMarker);
        Marker poolMarker= map.addMarker(new MarkerOptions()
        .title("Warner Hall & Wallace Pool")
        .snippet("some info")
        .position(pool));
        general.add(poolMarker);
        allMarkers.add(poolMarker);
        Marker wyerhaseuserMarker= map.addMarker(new MarkerOptions()
        .title("Weyerhaseuser Hall")
        .snippet("some info")
        .position(weyerhaseuser));
        acadmicBuildings.add(wyerhaseuserMarker);
        general.add(wyerhaseuserMarker);
        allMarkers.add(wyerhaseuserMarker);
        Marker toddMarker=map.addMarker(new MarkerOptions()
        .title("Todd/Phibbs Hall")
        .snippet("some info")
        .position(todd));
        dorms.add(toddMarker);
        allMarkers.add(toddMarker);
        Marker regesterMarker=map.addMarker(new MarkerOptions()
        .title("Regester Hall")
        .snippet("some info")
        .position(regester));
        dorms.add(regesterMarker);
        allMarkers.add(regesterMarker);
        Marker sewardMarker=map.addMarker(new MarkerOptions()
        .title("Seward Hall")
        .snippet("some info")
        .position(seward));
        dorms.add(sewardMarker);
        allMarkers.add(sewardMarker);
        Marker trimbleMarker=map.addMarker(new MarkerOptions()
        .title("Trimble Hall")
        .snippet("some info")
        .position(trimble));
        dorms.add(trimbleMarker);
        allMarkers.add(trimbleMarker);
        Marker kilworthMarker=map.addMarker(new MarkerOptions()
        .title("Kilworth Memorial Chapel")
        .snippet("some info")
        .position(kilworth));
        general.add(kilworthMarker);
        allMarkers.add(kilworthMarker);
        Marker alMarker=map.addMarker(new MarkerOptions()
        .title("Anderson/Langdon Hall")
        .snippet("some info")
        .position(al));
        dorms.add(alMarker);
        allMarkers.add(alMarker);
        Marker schiffMarker= map.addMarker(new MarkerOptions()
        .title("Schiff Hall")
        .snippet("some info")
        .position(schiff));
        dorms.add(schiffMarker);
        allMarkers.add(schiffMarker);
       Marker kittredgeMarker= map.addMarker(new MarkerOptions()
        .title("Kittredge Gallery")
        .snippet("some info")
        .position(kittredge));
       art.add(kittredgeMarker);
       allMarkers.add(kittredgeMarker); 
       addkeepers(allMarkers);
       showMarkers();
       
       map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
			
			@Override
			public void onMyLocationChange(Location location) {
				if(!currentLocation.equals(location))
				{
				currentLocation = location;
				whatshouldwesee();
			       for(Marker m: currentlyvisable)
			    	   Log.d("logout", m.getTitle());
				}
			}
		});
       
       ArrayList<Marker> v = visibility();
       if(v.size() > 0)
       {
    	   Log.d("logout", v.get(0).getTitle());
    	   edittext.setText(v.get(0).getTitle()); 
       }
>>>>>>> master
    	}
	
	public void whatshouldwesee()
	{
		//Location myloc= map.getMyLocation();
		Location myloc = currentLocation;
		LatLng mylatlng = new LatLng(myloc.getLatitude(),myloc.getLongitude());
		
		currentlyvisable.clear();
		for(Marker m: allMarkers){
			double longi= m.getPosition().longitude;
			double lati = m.getPosition().latitude;
			double longi1= mylatlng.longitude;
			double lati1 =mylatlng.latitude;
			
			if(Math.abs(longi-longi1) <= .001 && Math.abs(lati-lati1) <= .001)
//					(longi1+lati1)-(longi-lati)<=.001)
			{
				currentlyvisable.add(m);
			}
		
			
		}
	}
	
	public ArrayList<Marker> visibility()
	{
		VisibleRegion vr = map.getProjection().getVisibleRegion();
		/*
		LatLng farleft = vr.farLeft;
		LatLng nearleft = vr.nearLeft;
		LatLng farright = vr.farRight;
		LatLng nearright = vr.nearRight; */
		LatLngBounds b = vr.latLngBounds; 
		ArrayList<Marker> markersinbound = new ArrayList<Marker>();
		for(Marker m: allMarkers)
		{
			if(b.contains(m.getPosition()))
				markersinbound.add(m);
		}
		return markersinbound;
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
	
	
	
	public void showMarkers()
	{
		for(ArrayList<Marker> a: listoflists)
		{
			{
				
				for(Marker m: a)
				{
					m.setVisible(false);
				}
				
				
					
				}
			
			}
		for(ArrayList<Marker> a: keepers)
		{
			{
				
				for(Marker m: a)
				{
					m.setVisible(true);
				}
				
				
					
				}
			
			}
		}
	
	public void addkeepers(ArrayList<Marker> marks){
		keepers.add(marks);
	}
	
	public void removeKeepers(ArrayList<Marker> marks){
		keepers.remove(marks);
	}
		
}



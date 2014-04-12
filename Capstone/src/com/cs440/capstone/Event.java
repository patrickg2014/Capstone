package com.cs440.capstone;

import java.util.ArrayList;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Event{
	public String title;
	public LatLngBounds bound;
	public String snipit;
	public LatLng llng;
	public Marker m;

	public Event(String title, String snipit, LatLng latlng) {
		
		this.title=title;
		this.snipit=snipit;
		llng=latlng;
		makeMarker();
		
	
		
	}
	public void makeMarker(){
		
		
		m= CampusInfo.map.addMarker(new MarkerOptions().title(title)
				.snippet(snipit).position(llng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		boolean in = false;
		
			 
			 m.isVisible();
			 
		
		
		
	}
	

	
	
}

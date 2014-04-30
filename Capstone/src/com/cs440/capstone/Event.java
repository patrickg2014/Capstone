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
	public String pic;
	public String start;
	public String end;

	public Event(String title, String snipit, LatLng latlng, String pic, String start, String end) {
		
		this.title=title;
		this.snipit=snipit;
		llng=latlng;
		makeMarker();
		this.pic=pic;
		this.start=start;
		this.end=end;
		
	
		
	}
	public void makeMarker(){
		
		
		m= CampusInfo.map.addMarker(new MarkerOptions().title(title)
				.snippet(snipit).position(llng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		boolean in = false;
		
			 
			 m.isVisible();
			 
		
		
		
	}
	

	
	
}

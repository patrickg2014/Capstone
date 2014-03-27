package com.cs440.capstone;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Event{
	public String title;
	public LatLngBounds bound;
	public String snipit;
	public LatLng llng;

	public Event(String title, String snipit, LatLng latlng) {
		
		this.title=title;
		this.snipit=snipit;
		llng=latlng;
	
		
	}
	

	
	
}

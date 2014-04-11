package com.cs440.capstone;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Building{
	public String title;
	public Marker m;
	public LatLngBounds bound;
	public ArrayList<Building> insideList;
	public String snipit;
	public LatLng llng;
	public ArrayList<Building> insidePoints;




	public Building(String title, String snipit,Boolean building, LatLngBounds bounds) {

		this.title=title;
		this.snipit=snipit;
		insideList= new ArrayList<Building>();
		bound=bounds;
		llng= bound.getCenter();
		makeMarker();

		if( !CampusInfo.all.contains(this)&&building){
			CampusInfo.all.add(this);
		}


	}
	public Building(String title, String snipit, LatLng latlng) {

		this.title=title;
		this.snipit=snipit;
		llng=latlng;
		makeOldMarker();

	}
	public void makeMarker(){
		m= CampusInfo.map.addMarker(new MarkerOptions().title(title)
				.snippet(snipit).position(llng));
		 insidePoints=  new ArrayList<Building>();
		 m.isVisible();


	}

	public void makeOldMarker(){
		m= CampusInfo.map.addMarker(new MarkerOptions().title(title)
				.snippet(snipit).position(llng));
		 insidePoints=  new ArrayList<Building>();
		 m.isVisible();
	}

	public Marker getMarker(){
		return m;

	}

	public LatLngBounds getBounds(){
		return bound;

	}
	public void setVisible(boolean bool)
	{
		m.setVisible(bool);
	}

	public ArrayList<Building> getInsideList(){
		return insideList;


	}
	 public void addInsidePoint(Building inside){
		 insideList.add(inside);
	 }

}
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
	public ArrayList<Marker> insideList;
	public String snipit;
	public LatLng llng;
	public ArrayList<Marker> insidePoints;
	public boolean isParseBuilding;




	public Building(String title, String snipit,Boolean building, LatLngBounds bounds, Boolean parseStatus) {

		this.title=title;
		this.snipit=snipit;
		insideList= new ArrayList();
		bound=bounds;
		llng= bound.getCenter();
		isParseBuilding = parseStatus;
		makeMarker();

		if( !CampusInfo.all.contains(this)&&building){
			CampusInfo.all.add(this);
		}


	}
	public Building(String title, String snipit, LatLng latlng) {

		this.title=title;
		this.snipit=snipit;
		llng=latlng;
		makeMarker();

	}
	public void makeMarker(){
		m= CampusInfo.map.addMarker(new MarkerOptions().title(title)
				.snippet(snipit).position(llng));
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

	public ArrayList<Marker> getInsideList(){
		return insideList;


	}
	 public void addInsidePoint(Marker inside){
		 insideList.add(inside);
	 }
	 
	 public String getTitle(){
		 return title;
	 }
	 
	 public String getSnipit(){
		 return snipit;
	 }
	 
	 public boolean getParseStatus(){
		 return isParseBuilding;
	 }

}
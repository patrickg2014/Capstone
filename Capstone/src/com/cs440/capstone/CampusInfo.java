package com.cs440.capstone;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class CampusInfo {

	public ArrayList<ArrayList<Building>> keepers = new ArrayList();
	public static ArrayList<ArrayList<Building>> listoflists = new ArrayList();
	public static ArrayList<Marker> currentlyvisable = new ArrayList();
	public static ArrayList<Event> events = new ArrayList();
	public static GoogleMap map = null;
	public static ArrayList<Building> all = new ArrayList();
	private Context context;

	public CampusInfo(GoogleMap map,Context con) {
		context = con;
		Parse.initialize(context, "bh3zRUQ5KI43dx5dcES5s5RelhfunoxR1Q9p0MFa", "GeAe5yOfQPOZ3FwYOCHSJGn6ldAUIkRuXjY8koHD");
		this.map = map;
		//createMarkers();
		queryMarkers();
	}

	public void queryMarkers(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Building");
		query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
		query.whereNotEqualTo("name", "");

		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects,
					com.parse.ParseException e) {
				  if (e == null) {
			            Log.d("query", "Retrieved " + objects.size() + " buildings");
			            for (int i = 0; i < objects.size(); i++) {
			            	String id = objects.get(i).getObjectId();
							String name = objects.get(i).getString("name");
							String description = objects.get(i).getString("description");
							Log.d("query",name + " loaded");
							double latboundone = objects.get(i).getParseGeoPoint("latlngboundfirst").getLatitude();
							double longboundone = objects.get(i).getParseGeoPoint("latlngboundfirst").getLongitude();
							LatLng boundOne = new LatLng(latboundone,longboundone);
							double latboundtwo = objects.get(i).getParseGeoPoint("latlngboundsecond").getLatitude();
							double longboundtwo = objects.get(i).getParseGeoPoint("latlngboundsecond").getLongitude();
							LatLng boundTwo = new LatLng(latboundtwo,longboundtwo);
							
							
							LatLngBounds bounds = new LatLngBounds(boundOne,boundTwo);
							final Building build = new Building(name,description,true,bounds,true);
							currentlyvisable.add(build.getMarker());
							all.add(build);
							
							//For each build put in Building.insidelist their objects
							
							ParseQuery<ParseObject> query = ParseQuery.getQuery("Inside_POI");
							query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
							ParseObject obj = ParseObject.createWithoutData("Building", id);
							query.whereEqualTo("FK", obj);

							query.findInBackground(new FindCallback<ParseObject>() {
								@Override
								public void done(List<ParseObject> objects,
										com.parse.ParseException e) {
									  if (e == null) {
								            Log.d("query", "Retrieved " + objects.size() + " inside buildings");
								            for (int j = 0; j < objects.size(); j++) {
								            	String name = objects.get(j).getString("Title");
												String description = objects.get(j).getString("Description");
												Log.d("query",name + " loaded");
												double latbound = objects.get(j).getParseGeoPoint("latlng").getLatitude();
												double longbound = objects.get(j).getParseGeoPoint("latlng").getLongitude();
												LatLng bound = new LatLng(latbound,longbound);
												build.insideList.add(map.addMarker(new MarkerOptions().title(name).snippet(description).position(bound).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))));
											}
								        } else {
								            Log.d("query", "Error: " + e.getMessage());
								        }
								}
							});
							//build.insideList
						}
			        } else {
			            Log.d("query", "Error: " + e.getMessage());
			        }
			}
		});

	}

	
	public void addkeepers(ArrayList<Building> marks) {
		keepers.add(marks);
	}

	public void removeKeepers(ArrayList<Marker> marks) {
		keepers.remove(marks);
	}

	/*
	 * this allows for sorting what categories of markers are to be shown base
	 * don combinations of arraylists this will be updated later when the action
	 * bar is up to allow users to sort
	 */
	public void showMarkers() {
		for (ArrayList<Building> a : listoflists) {
			{
				for (Building b : a) {
					b.setVisible(true);
				}
			}
		}
		for (ArrayList<Building> a : keepers) {
			{
				for (Building b : a) {
					b.setVisible(true);
				}
			}
		}
	}
	
	public ArrayList<Building> getAllList(){
		return all;
	}
	
	static public Building getBuilding(String name){
		for(Building b: all){
			if(b.title.contentEquals(name)){
				return b;
			}
		}
		return null;
	}
	
	public ArrayList<Marker> getCurrentlyVisableList(){
		return currentlyvisable;
	}

}
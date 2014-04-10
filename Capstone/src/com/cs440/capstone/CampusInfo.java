package com.cs440.capstone;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CampusInfo {

	public ArrayList<ArrayList<Building>> keepers = new ArrayList();
	public static ArrayList<ArrayList<Building>> listoflists = new ArrayList();
	public static ArrayList<Marker> currentlyvisable = new ArrayList();
	public static ArrayList<Event> events = new ArrayList();
	public static GoogleMap map = null;
	public static ArrayList<Building> all = new ArrayList();

	public CampusInfo(GoogleMap map) {
		this.map = map;
		createMarkers();
	}

	/*
	 * hardcoded latlng values that we can get by parseing through an http
	 * request to google which we will do if we end up creating a way to have
	 * other apps created through our tools currently we are missing certain
	 * values and do not have any information about these buildings
	 */
	public static void createMarkers() {
		
		LatLng pugetsound = new LatLng(47.2626, -122.4817);
		
		ArrayList<Building> acadmic = new ArrayList<Building>();
		ArrayList<Building> dorms = new ArrayList<Building>();
		ArrayList<Building> art = new ArrayList<Building>();
		ArrayList<Building> general = new ArrayList<Building>();
		
		Building sub = new Building("Wheelock Student Center", "this is the snipit",true,new LatLngBounds(new LatLng(47.262887,-122.479227),new LatLng(47.263494,-122.47875)));
		general.add(sub);
		currentlyvisable.add(sub.getMarker());
		
		Building jones = new Building("Jones Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.263286,-122.481235),new LatLng(47.264006,-122.481004)));
		general.add(jones);
		art.add(jones);
		acadmic.add(jones);
		currentlyvisable.add(jones.getMarker());
		
		Building mcintyre = new Building("Jones Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.263945,-122.480714),new LatLng(47.264101,-122.480097)));
		general.add(mcintyre);
		acadmic.add(mcintyre);
		currentlyvisable.add(mcintyre.getMarker());
		
		Building howarth = new Building("Howarth Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.263184,-122.480714),new LatLng(47.263337,-122.480119)));
		general.add(howarth);
		acadmic.add(howarth);
		currentlyvisable.add(howarth.getMarker());
		
		Building music = new Building("Music Building/Shneebeck Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.263453,-122.482463),new LatLng(47.263976,-122.482278)));
		art.add(music);
		acadmic.add(music);
		currentlyvisable.add(music.getMarker());
		
		Building thompson = new Building("Thompson Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.263246,-122.483193),new LatLng(47.264087,-122.482823)));
		acadmic.add(thompson);
		currentlyvisable.add(thompson.getMarker());
		thompson.insideList.add(new Building("Slater Museum (2nd Floor)" , "this is the snipit",false,new LatLngBounds(new LatLng(47.263654,-122.482941),new LatLng(47.263654,-122.482941))));

		Building harned = new Building("Harned Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.263249,-122.483686),new LatLng(47.264076,-122.483209)));
		acadmic.add(harned);
		currentlyvisable.add(harned.getMarker());

		/*Building collins = new Building("Collins Memorial Library", "this is the snipit",true,new LatLngBounds(new LatLng(47.264316,-122.482045),new LatLng(47.264833,-122.481353)));
		general.add(collins);
		acadmic.add(collins);
		currentlyvisable.add(collins.getMarker());*/
		
		Building wyatt =new Building("Wyatt Hall", "this is the snipit",true, new LatLngBounds(new LatLng(47.261604,-122.48279),new LatLng(47.262121,-122.482517)));
		acadmic.add(wyatt);
		currentlyvisable.add(wyatt.getMarker());

		Building pool =new Building("Warner Gym/Pool", "this is the snipit", true,new LatLngBounds(new LatLng(47.260937,-122.48183),new LatLng(47.26156,-122.481551)));
		general.add(pool);
		currentlyvisable.add(pool.getMarker());
	
		Building weyerhaeuser = new Building("Weyerhaeuser Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.2601,-122.480655),new LatLng(47.260861,-122.480425)));
		general.add(weyerhaeuser);
		acadmic.add(weyerhaeuser);
		currentlyvisable.add(weyerhaeuser.getMarker());

		Building todd = new Building("Tood/Phibbs Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.262099,-122.480918),new LatLng(47.262743,-122.480693)));
		dorms.add(todd);
		currentlyvisable.add(todd.getMarker());

		Building regester = new Building("Regester Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.261935,-122.480838),new LatLng(47.262033,-122.48043)));
		dorms.add(regester);
		currentlyvisable.add(regester.getMarker());
	
		Building seward = new Building("Seward Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.261957,-122.480296),new LatLng(47.262055,-122.479872)));
		dorms.add(seward);
		currentlyvisable.add(seward.getMarker());

		Building trimble = new Building("Trimble Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.262816,-122.480848),new LatLng(47.262976,-122.479931)));
		dorms.add(trimble);
		currentlyvisable.add(trimble.getMarker());

		Building kilworth = new Building("Kilworth Chapel", "this is the snipit",true,new LatLngBounds(new LatLng(47.264964,-122.481803),new LatLng(47.265262,-122.481648)));
		general.add(kilworth);
		currentlyvisable.add(kilworth.getMarker());
	
		Building al = new Building("Anderson Langdon Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.264567,-122.480881),new LatLng(47.265142,-122.48065)));
		dorms.add(al);
		currentlyvisable.add(al.getMarker());
		
		Building schiff = new Building("Schiff Hall", "this is the snipit",true,new LatLngBounds(new LatLng(47.265222,-122.480258),new LatLng(47.265324,-122.479856)));
		dorms.add(schiff);
		currentlyvisable.add(schiff.getMarker());
		
		Building kittredge = new Building("Kittredge Art Gallery", "this is the snipit",true,new LatLngBounds(new LatLng(47.263799,-122.479094),new LatLng(47.264061,-122.478799)));
		art.add(kittredge);
		currentlyvisable.add(kittredge.getMarker());
	
		Building phiDelt = new Building("PhiDelt", "this is the snipit",true,new LatLngBounds(new LatLng(47.262157,-122.485419),new LatLng(47.262354,-122.485145)));
		dorms.add(phiDelt);
		currentlyvisable.add(phiDelt.getMarker());
		
		Building alphaPhi = new Building("APhi", "this is the snipit",true,new LatLngBounds(new LatLng(47.262456,-122.485408),new LatLng(47.262576,-122.485038)));
		dorms.add(alphaPhi);
		currentlyvisable.add(alphaPhi.getMarker());
				
		Building blueBeard = new Building("Blue Beard Coffee", "this is for testing because i was having coffee here and doing work",true,new LatLngBounds(new LatLng(47.255551,-122.4663),new LatLng(47.255801,-122.466021)));
		
		currentlyvisable.add(blueBeard.getMarker());

		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(pugetsound, 16));// sets
																		
		
		
		

		// creates markers and adds them to the arrays of the corosponding
		// catagories
		
		
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

}

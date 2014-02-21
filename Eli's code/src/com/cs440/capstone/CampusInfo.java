package com.cs440.capstone;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CampusInfo {

	public ArrayList<ArrayList<Marker>> keepers = new ArrayList();
	public ArrayList<ArrayList> listoflists = new ArrayList();
	static ArrayList<Marker> allMarkers = new ArrayList();
	ArrayList<Marker> currentlyvisable = new ArrayList();
	public GoogleMap map = null;

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
	public void createMarkers() {

		LatLng pugetsound = new LatLng(47.2626, -122.4817);
		LatLng sub = new LatLng(47.263144, -122.478944);
		LatLng jones = new LatLng(47.263635, -122.481138);
		LatLng mcintyre = new LatLng(47.264021, -122.480403);
		LatLng howarth = new LatLng(47.263257, -122.480435);
		LatLng music = new LatLng(47.263631, -122.48235);
		LatLng thompson = new LatLng(47.263668, -122.482882);
		LatLng harned = new LatLng(47.263668, -122.483466);
		LatLng collins = new LatLng(47.264389, -122.481723);
		LatLng wyatt = new LatLng(47.261859, -122.482608);
		LatLng pool = new LatLng(47.261276, -122.481685);
		LatLng weyerhaseuser = new LatLng(47.260482, -122.480537);
		LatLng todd = new LatLng(47.262404, -122.480832);
		LatLng regester = new LatLng(47.261938, -122.480628);
		LatLng seward = new LatLng(47.262004, -122.480081);
		LatLng trimble = new LatLng(47.26279, -122.480392);
		LatLng kilworth = new LatLng(47.26512, -122.481744);
		LatLng al = new LatLng(47.264843, -122.480779);
		LatLng schiff = new LatLng(47.265246, -122.480095);
		LatLng kittredge = new LatLng(47.263905, -122.478966);

		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(pugetsound, 16));// sets
																			// where
																			// the
																			// cameras
																			// focus
																			// starts
																			// and
																			// how
																			// zoomed
																			// out
																			// we
																			// are
																			// we
																			// may
																			// want
																			// to
																			// use
																			// this
																			// to
																			// zoom
																			// in
																			// on
																			// builds
																			// when
																			// you
																			// enter
																			// them
																			// later
		// arraylists of different categories as to allow sorting later
		ArrayList<Marker> acadmicBuildings = new ArrayList();
		ArrayList<Marker> dorms = new ArrayList();
		ArrayList<Marker> art = new ArrayList();
		ArrayList<Marker> general = new ArrayList();

		// creates markers and adds them to the arrays of the corosponding
		// catagories
		Marker subMarker = map.addMarker(new MarkerOptions().title("The SUB")
				.snippet("Main building").position(sub));
		general.add(subMarker);
		allMarkers.add(subMarker);
		Marker jonesMarker = map.addMarker(new MarkerOptions()
				.title("Jones hall").snippet("some info").position(jones));
		acadmicBuildings.add(jonesMarker);
		art.add(jonesMarker);
		general.add(jonesMarker);
		allMarkers.add(jonesMarker);
		Marker mcintyreMarker = map
				.addMarker(new MarkerOptions().title("McIntyre hall")
						.snippet("some info").position(mcintyre));
		acadmicBuildings.add(mcintyreMarker);
		general.add(mcintyreMarker);
		allMarkers.add(mcintyreMarker);
		Marker howarthMarker = map.addMarker(new MarkerOptions()
				.title("Howarth hall").snippet("some info").position(howarth));
		acadmicBuildings.add(howarthMarker);
		general.add(howarthMarker);
		allMarkers.add(howarthMarker);
		Marker musicMarker = map.addMarker(new MarkerOptions()
				.title("Music Building/Schneebeck Hall").snippet("some info")
				.position(music));
		acadmicBuildings.add(musicMarker);
		art.add(musicMarker);
		allMarkers.add(musicMarker);
		Marker thompsonMarker = map
				.addMarker(new MarkerOptions().title("Thompson Hall")
						.snippet("some info").position(thompson));
		acadmicBuildings.add(thompsonMarker);
		allMarkers.add(thompsonMarker);
		Marker harnedMarker = map.addMarker(new MarkerOptions()
				.title("Harned Hall").snippet("some info").position(harned));
		acadmicBuildings.add(harnedMarker);
		allMarkers.add(harnedMarker);
		Marker collinsMarker = map.addMarker(new MarkerOptions()
				.title("Collins Memorial Libary").snippet("some info")
				.position(collins));
		acadmicBuildings.add(collinsMarker);
		allMarkers.add(collinsMarker);
		general.add(collinsMarker);
		Marker wyattMarker = map.addMarker(new MarkerOptions()
				.title("Wyatt Hall").snippet("some info").position(wyatt));
		acadmicBuildings.add(wyattMarker);
		allMarkers.add(wyattMarker);
		Marker poolMarker = map.addMarker(new MarkerOptions()
				.title("Warner Hall & Wallace Pool").snippet("some info")
				.position(pool));
		general.add(poolMarker);
		allMarkers.add(poolMarker);
		Marker wyerhaseuserMarker = map.addMarker(new MarkerOptions()
				.title("Weyerhaseuser Hall").snippet("some info")
				.position(weyerhaseuser));
		acadmicBuildings.add(wyerhaseuserMarker);
		general.add(wyerhaseuserMarker);
		allMarkers.add(wyerhaseuserMarker);
		Marker toddMarker = map.addMarker(new MarkerOptions()
				.title("Todd/Phibbs Hall").snippet("some info").position(todd));
		dorms.add(toddMarker);
		allMarkers.add(toddMarker);
		Marker regesterMarker = map
				.addMarker(new MarkerOptions().title("Regester Hall")
						.snippet("some info").position(regester));
		dorms.add(regesterMarker);
		allMarkers.add(regesterMarker);
		Marker sewardMarker = map.addMarker(new MarkerOptions()
				.title("Seward Hall").snippet("some info").position(seward));
		dorms.add(sewardMarker);
		allMarkers.add(sewardMarker);
		Marker trimbleMarker = map.addMarker(new MarkerOptions()
				.title("Trimble Hall").snippet("some info").position(trimble));
		dorms.add(trimbleMarker);
		allMarkers.add(trimbleMarker);
		Marker kilworthMarker = map.addMarker(new MarkerOptions()
				.title("Kilworth Memorial Chapel").snippet("some info")
				.position(kilworth));
		general.add(kilworthMarker);
		allMarkers.add(kilworthMarker);
		Marker alMarker = map.addMarker(new MarkerOptions()
				.title("Anderson/Langdon Hall").snippet("some info")
				.position(al));
		dorms.add(alMarker);
		allMarkers.add(alMarker);
		Marker schiffMarker = map.addMarker(new MarkerOptions()
				.title("Schiff Hall").snippet("some info").position(schiff));
		dorms.add(schiffMarker);
		allMarkers.add(schiffMarker);
		Marker kittredgeMarker = map.addMarker(new MarkerOptions()
				.title("Kittredge Gallery").snippet("some info")
				.position(kittredge));
		art.add(kittredgeMarker);
		allMarkers.add(kittredgeMarker);
		// adds all of the lists to a master list
		listoflists.add(acadmicBuildings);
		listoflists.add(dorms);
		listoflists.add(general);
		listoflists.add(art);
		addkeepers(allMarkers);
	}

	public void addkeepers(ArrayList<Marker> marks) {
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
		for (ArrayList<Marker> a : listoflists) {
			{
				for (Marker m : a) {
					m.setVisible(false);
				}
			}
		}
		for (ArrayList<Marker> a : keepers) {
			{
				for (Marker m : a) {
					m.setVisible(true);
				}
			}
		}
	}

	public static ArrayList getall() {
		return allMarkers;
	}

}

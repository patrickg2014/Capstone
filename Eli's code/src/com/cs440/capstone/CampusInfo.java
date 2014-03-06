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

	public ArrayList<ArrayList<Marker>> keepers = new ArrayList();
	public ArrayList<ArrayList> listoflists = new ArrayList();
	public static HashMap<Marker,LatLngBounds> allMarkers = new HashMap<Marker,LatLngBounds>();
	public static HashMap<Marker,ArrayList<Marker>> insideMarkers = new HashMap<Marker,ArrayList<Marker>>();
	public ArrayList<Marker> currentlyvisable = new ArrayList();
	public static ArrayList<LatLngBounds> bounds= new ArrayList();
	
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
	
		
		LatLngBounds sub = new LatLngBounds(new LatLng(47.262887,-122.479227),new LatLng(47.263494,-122.47875));
		LatLngBounds jones = new LatLngBounds(new LatLng(47.263286,-122.481235),new LatLng(47.264006,-122.481004));
		LatLngBounds mcintyre = new LatLngBounds(new LatLng(47.263945,-122.480714),new LatLng(47.264101,-122.480097));
		LatLngBounds howarth = new LatLngBounds(new LatLng(47.263184,-122.480714),new LatLng(47.263337,-122.480119));
		LatLngBounds music = new LatLngBounds(new LatLng(47.263453,-122.482463),new LatLng(47.263976,-122.482278));
		LatLngBounds thompson = new LatLngBounds(new LatLng(47.263246,-122.483193),new LatLng(47.264087,-122.482823));
		LatLngBounds harned = new LatLngBounds(new LatLng(47.263249,-122.483686),new LatLng(47.264076,-122.483209));
		LatLngBounds collins = new LatLngBounds(new LatLng(47.264316,-122.482045),new LatLng(47.264833,-122.481353));
		LatLngBounds wyatt = new LatLngBounds(new LatLng(47.261604,-122.48279),new LatLng(47.262121,-122.482517));
		LatLngBounds pool = new LatLngBounds(new LatLng(47.260937,-122.48183),new LatLng(47.26156,-122.481551));
		LatLngBounds weyerhaseuser = new LatLngBounds(new LatLng(47.2601,-122.480655),new LatLng(47.260861,-122.480425));
		LatLngBounds todd = new LatLngBounds(new LatLng(47.262099,-122.480918),new LatLng(47.262743,-122.480693));
		LatLngBounds regester = new LatLngBounds(new LatLng(47.261935,-122.480838),new LatLng(47.262033,-122.48043));
		LatLngBounds seward = new LatLngBounds(new LatLng(47.261957,-122.480296),new LatLng(47.262055,-122.479872));
		LatLngBounds trimble = new LatLngBounds(new LatLng(47.262816,-122.480848),new LatLng(47.262976,-122.479931));
		LatLngBounds kilworth = new LatLngBounds(new LatLng(47.264964,-122.481803),new LatLng(47.265262,-122.481648));
		LatLngBounds al = new LatLngBounds(new LatLng(47.264567,-122.480881),new LatLng(47.265142,-122.48065));
		LatLngBounds schiff = new LatLngBounds(new LatLng(47.265222,-122.480258),new LatLng(47.265324,-122.479856));
		LatLngBounds kittredge = new LatLngBounds(new LatLng(47.263799,-122.479094),new LatLng(47.264061,-122.478799));
		LatLngBounds phiDelt = new LatLngBounds(new LatLng(47.262157,-122.485419),new LatLng(47.262354,-122.485145));
		LatLngBounds alphaPhi = new LatLngBounds(new LatLng(47.262456,-122.485408),new LatLng(47.262576,-122.485038));
		LatLng test1= new LatLng(47.263642,-122.483541);
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
		Marker whale= map.addMarker(new MarkerOptions().title("Baby Grey whale")
				.snippet("Main building").position(test1));
		
		Marker subMarker = map.addMarker(new MarkerOptions().title("Wheelock Student Center")
				.snippet("Main building").position(sub.getCenter()));
		general.add(subMarker);
		allMarkers.put(subMarker,sub);
		bounds.add(sub);
		Marker jonesMarker = map.addMarker(new MarkerOptions()
				.title("Jones Hall").snippet("some info").position(jones.getCenter()));
		acadmicBuildings.add(jonesMarker);
		art.add(jonesMarker);
		general.add(jonesMarker);
		allMarkers.put(jonesMarker, jones);
		bounds.add(jones);
		Marker mcintyreMarker = map
				.addMarker(new MarkerOptions().title("McIntyre Hall")
						.snippet("some info").position(mcintyre.getCenter()));
		acadmicBuildings.add(mcintyreMarker);
		general.add(mcintyreMarker);
		allMarkers.put(mcintyreMarker, mcintyre);
		bounds.add(mcintyre);
		Marker howarthMarker = map.addMarker(new MarkerOptions()
				.title("Howarth hall").snippet("some info").position(howarth.getCenter()));
		acadmicBuildings.add(howarthMarker);
		general.add(howarthMarker);
		allMarkers.put(howarthMarker, howarth);
		bounds.add(howarth);
		Marker musicMarker = map.addMarker(new MarkerOptions()
				.title("Music Building/Schneebeck Hall").snippet("some info")
				.position(music.getCenter()));
		acadmicBuildings.add(musicMarker);
		art.add(musicMarker);
		allMarkers.put(musicMarker, music);
		bounds.add(music);
		Marker thompsonMarker = map
				.addMarker(new MarkerOptions().title("Thompson Hall")
						.snippet("some info").position(thompson.getCenter()));
		acadmicBuildings.add(thompsonMarker);
		allMarkers.put(thompsonMarker, thompson);
		ArrayList<Marker> thompsonMarks = new ArrayList();
			Marker slater= map.addMarker(new MarkerOptions().title("Slater Museum of Natural History")
					.snippet("Main building").position(new LatLng (47.263663,-122.482974)));
		thompsonMarks.add(slater);
		insideMarkers.put(thompsonMarker, thompsonMarks);
		
		Marker harnedMarker = map.addMarker(new MarkerOptions()
				.title("Harned Hall").snippet("some info").position(harned.getCenter()));
		acadmicBuildings.add(harnedMarker);
		allMarkers.put(harnedMarker, harned);
		ArrayList<Marker> harnedMarks = new ArrayList();
		harnedMarks.add(whale);
		insideMarkers.put(harnedMarker, harnedMarks);
		Marker collinsMarker = map.addMarker(new MarkerOptions()
				.title("Collins Memorial Libary").snippet("some info")
				.position(collins.getCenter()));
		acadmicBuildings.add(collinsMarker);
		allMarkers.put(harnedMarker, harned);
		general.add(collinsMarker);
		bounds.add(collins);
		Marker wyattMarker = map.addMarker(new MarkerOptions()
				.title("Wyatt Hall").snippet("some info").position(wyatt.getCenter()));
		acadmicBuildings.add(wyattMarker);
		allMarkers.put(wyattMarker, wyatt);
		bounds.add(wyatt);
		Marker poolMarker = map.addMarker(new MarkerOptions()
				.title("Warner Hall & Wallace Pool").snippet("some info")
				.position(pool.getCenter()));
		general.add(poolMarker);
		allMarkers.put(poolMarker, pool);
		bounds.add(pool);
		Marker wyerhaseuserMarker = map.addMarker(new MarkerOptions()
				.title("Weyerhaseuser Hall").snippet("some info")
				.position(weyerhaseuser.getCenter()));
		acadmicBuildings.add(wyerhaseuserMarker);
		general.add(wyerhaseuserMarker);
		allMarkers.put(wyerhaseuserMarker, weyerhaseuser);
		bounds.add(weyerhaseuser);
		Marker toddMarker = map.addMarker(new MarkerOptions()
				.title("Todd/Phibbs Hall").snippet("some info").position(todd.getCenter()));
		dorms.add(toddMarker);
		allMarkers.put(toddMarker, todd);
		bounds.add(todd);
		Marker regesterMarker = map
				.addMarker(new MarkerOptions().title("Regester Hall")
						.snippet("some info").position(regester.getCenter()));
		dorms.add(regesterMarker);
		allMarkers.put(regesterMarker, regester);
		bounds.add(regester);
		Marker sewardMarker = map.addMarker(new MarkerOptions()
				.title("Seward Hall").snippet("some info").position(seward.getCenter()));
		dorms.add(sewardMarker);
		allMarkers.put(sewardMarker, seward);
		bounds.add(seward);
		Marker trimbleMarker = map.addMarker(new MarkerOptions()
				.title("Trimble Hall").snippet("some info").position(trimble.getCenter()));
		dorms.add(trimbleMarker);
		allMarkers.put(trimbleMarker, trimble);
		bounds.add(trimble);
		Marker kilworthMarker = map.addMarker(new MarkerOptions()
				.title("Kilworth Memorial Chapel").snippet("some info")
				.position(kilworth.getCenter()));
		general.add(kilworthMarker);
		allMarkers.put(kilworthMarker,kilworth);
		bounds.add(kilworth);
		Marker alMarker = map.addMarker(new MarkerOptions()
				.title("Anderson/Langdon Hall").snippet("some info")
				.position(al.getCenter()));
		dorms.add(alMarker);
		allMarkers.put(alMarker, al);
		bounds.add(al);
		Marker schiffMarker = map.addMarker(new MarkerOptions()
				.title("Schiff Hall").snippet("some info").position(schiff.getCenter()));
		dorms.add(schiffMarker);
		allMarkers.put(schiffMarker, schiff);
		bounds.add(schiff);
		Marker kittredgeMarker = map.addMarker(new MarkerOptions()
				.title("Kittredge Gallery").snippet("some info")
				.position(kittredge.getCenter()));
		art.add(kittredgeMarker);
		allMarkers.put(kittredgeMarker, kittredge);
		bounds.add(kittredge);
		
		
	
		Marker phiDeltMarker = map.addMarker(new MarkerOptions().title("Phi Delt")
				.snippet("dorm").position(phiDelt.getCenter()));
		general.add(phiDeltMarker);
		allMarkers.put(phiDeltMarker, phiDelt);
		bounds.add(phiDelt);
		Marker alphaPhiMarker = map.addMarker(new MarkerOptions().title("Alpha Phi")
				.snippet("Dorm").position(alphaPhi.getCenter()));
		general.add(alphaPhiMarker);
		allMarkers.put(alphaPhiMarker, alphaPhi);
		bounds.add(alphaPhi);
		
		// adds all of the lists to a master list
		listoflists.add(acadmicBuildings);
		listoflists.add(dorms);
		listoflists.add(general);
		listoflists.add(art);
		
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
					m.setVisible(true);
				}
			}
		}
		/*for (ArrayList<Marker> a : keepers) {
			{
				for (Marker m : a) {
					m.setVisible(true);
				}
			}
		}*/
	}

	
	
	public static LatLngBounds getBounds(int i) {
		return bounds.get(i);
	}

}

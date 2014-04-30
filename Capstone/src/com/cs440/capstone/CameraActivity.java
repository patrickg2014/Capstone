package com.cs440.capstone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class CameraActivity extends Activity implements SensorEventListener{
	
	private static Camera mainCam;
	private CameraPreview camView;
	private SensorManager mSensorManager;
	public ArrayList<Building> currentlyNear = new ArrayList<Building>();
	public ArrayList<Building> currentlyvisable = new ArrayList<Building>();
	public float heading;
	public Building insideBuilding;
	public Location currentLocation;
	Location myloc;
	public CameraOverlay camover;
	public static int width=4;
	public int height=4;
	public int dens= 40;
	public static int theScale=1;
	private LocationListener onLocationChange;
	private LocationManager lm;
	public LatLng mylatlng;
	private int counter=0;
	private int angleOfView=100;
	
	@SuppressLint("NewApi")
	android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
	private ArrayList<Event> currentlyNearEvents= new ArrayList<Event>();
	private long timer;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	//pulls and saved state for the activity
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.camera_layout);	// sets the layout
		getCameraInstance();	//calls to start up the camera
		camView = new CameraPreview(this, mainCam);	// allows the screen to see what the camera sensor is seeing
		FrameLayout preview = (FrameLayout)findViewById(R.id.camera_layout);	// updates the layout
		preview.addView(camView);	
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);// sets up a sensor manager to help with gps and comapss data
		currentLocation= CampusInfo.map.getMyLocation();	// gets our current location
		camover = (CameraOverlay)findViewById(R.id.overlay_layout);	//starts up a comeraOverlay instance which will allow us to write on top of the camera
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keeps the Camera from falling asleep
		Display display = getWindowManager().getDefaultDisplay();
		camover.passActivity(this);
		
		timer = System.currentTimeMillis();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		dens=(int) getResources().getDisplayMetrics().xdpi;
		screenScale();
		
		// Moving to constructor for use in GPS and location methods
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
		   
	    onLocationChange = new LocationListener() {
	        public void onLocationChanged(Location loc) {
	            //sets and displays the lat/long when a location is provided
	        	if(loc != null)
	        	{
	        		currentLocation=loc;
	        		// This is being called on heading updates, but should also
	        		// be called whenever we get a new location update since
	        		// you may not change headings but may change locations.
	        		
	        		if(currentLocation != null) //make sure that we dont get a null pointer 
	        		{
	        			whatshouldwesee();// update the information about what we are near and what is in our view 
	        		}
	        		Log.d("onLocationChanged","location: " + loc.toString() );
	        	}
	        }
	         
	        public void onProviderDisabled(String provider) {
	        // required for interface, not used
	        }
	         
	        public void onProviderEnabled(String provider) {
	        // required for interface, not used
	        }
	         
	        public void onStatusChanged(String provider, int status,
	        Bundle extras) {
	        // required for interface, not used
	        }
	        
	        
	    };
	    if(lm.isProviderEnabled("GPS_PROVIDER")){
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 0, onLocationChange);
	    }
	    else{
	    	 lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, onLocationChange);
	    }
	    
	    int orientation = display.getRotation();
		if(orientation==0||orientation==4)
		{
			finish();
		}
	    
		Log.d("Changing", "LETS GO!");
	}

	public static Camera getCameraInstance() //starts up a camera if the device has one available 
		{
		mainCam = null;
		try{
			mainCam = Camera.open();

		}catch(Exception e){
			//Camera is not available
		}
		return mainCam;
	}

	
    
	
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mainCam.startPreview();
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
		Display display = getWindowManager().getDefaultDisplay();
		int orientation = display.getRotation();
		if(orientation==0||orientation==4)
		{
			finish();
		}
	}

	@Override
    protected void onPause() {
        super.onPause();      // if you are using MediaRecorder, release it first
        releaseCamera();  // release the camera immediately on pause event
        mSensorManager.unregisterListener(this);
        

    }
	@Override
	   protected void onStop() {
	       super.onStop();      // if you are using MediaRecorder, release it first
	       releaseCamera();  // release the camera immediately on pause event
	       mSensorManager.unregisterListener(this);
	       
	       finish();

	   }
	

	/**
	 * This method changes to the CameraActivity when the phone is rotated
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d("Billy", "Here!");
	  super.onConfigurationChanged(newConfig);
	  
	}



    private void MainAcivity() {
		// TODO Auto-generated method stub
		
	}
    
    public void buildingActivity(String buildingName, String snippet) {
		Intent intent = new Intent(this, BuildingInfoActivity.class);
		intent.putExtra("Name", buildingName);
		intent.putExtra("Snippet", snippet);
		startActivity(intent);
	}

	public void eventActivity(String eventname, String snippet) {
		Intent intent = new Intent(this, EventInfoActivity.class);
		intent.putExtra("Name", eventname);
		intent.putExtra("Snippet", snippet);
		startActivity(intent);
	}


	private void releaseCamera(){
        if (mainCam != null){
        	mainCam.lock();  
            mainCam.release();        // release the camera for other applications
            mainCam = null;
        }
	
	
    }
    public void whatshouldwesee()
   	{	
   		//Location myloc= map.getMyLocation();
    	if(System.currentTimeMillis()-timer>200){
    	camover.nearList.clear();
    	checkCurrentlyNearevents();
    	
    	checkCurrentlyNear();
    	
    	if(!camover.insidebool)
    	{
    		checkCurrentlyVisable();
    	}
    	else{
    		
    		insidecurrentlyVisable();
	}
    	checkCurrentlyVisableEvents();
   		camover.invalidate();
   		
   		
   		Log.d("heading","Heading: " + Float.toString(heading) + " degrees");
   		timer = System.currentTimeMillis();
   	}
   	}
 
   		       

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		heading =((Math.round(event.values[0]+event.values[2])+45)%360); //Rounds the current heading to full degrees
		
		if(currentLocation != null) //make sure that we dont get a null pointer 
		{
			whatshouldwesee();// update the information about what we are near and what is in our view 
		}
	}
	
	private void screenScale()
	{
		theScale=(width)/angleOfView;
		Log.d("DENS", ""+theScale);

	}
	public void checkCurrentlyNear(){
		myloc = currentLocation;
   		mylatlng = new LatLng(myloc.getLatitude(),myloc.getLongitude());
   		currentlyNear.clear();
   		currentlyvisable.clear();
   		camover.xPos.clear();
   		camover.yPos.clear();
   		
   		for(Building b: CampusInfo.all)	//loops through all a markers to see which ones are within a certain radius of us
   		{	
   			Log.d("test", "1");
   			double longi= b.m.getPosition().longitude; //converting locations to Doubles as to allow comparison
   			double lati = b.m.getPosition().latitude;
   			double longi1= mylatlng.longitude;
   			double lati1 =mylatlng.latitude;
   			double distance= Math.abs(longi-longi1)+Math.abs(lati-lati1);
   			if(distance<=.00175)	//check to make sure they are in the radius
//   					(longi1+lati1)-(longi-lati)<=.001)
   			{
   				Log.d("test", "2");
   				if(b.getBounds().contains(mylatlng))
   						{
   					
   					camover.insidebool=true; 	
   					camover.inside=("Inside "+b.title);
   					insideBuilding=b;
   					currentlyNear.clear();
   					camover.nearList.clear();
   					camover.xPos.clear();
   					camover.yPos.clear();
   					
   					Log.d("Inside", "");
   					break;
   						}
   					else
   						{
   						currentlyNear.add(b);//if it is add it to the array
   						camover.insidebool=false;
   						}
   			}
   			
   		}
   		
   		}
   		
	
	public void checkCurrentlyVisable()
	{		 
		
		if(currentlyNear!=null){
			
		for(int i=0; i<(currentlyNear.size()/2); i++)// a loop to see which of those currently near markers are within our angle of view
   		{
   			Location location = new Location("mloc");
   			  location.setLatitude(currentlyNear.get(i).m.getPosition().latitude);
   			  location.setLongitude(currentlyNear.get(i).m.getPosition().longitude);
   			int locHead=(int) myloc.bearingTo(location);
   			int headingOptimized=(int) heading;
   			 if((myloc.bearingTo(location)-headingOptimized)<(-310+angleOfView/2)) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
   			 {
   				 locHead=(int)myloc.bearingTo(location)+360;	
   			 }
   			if((myloc.bearingTo(location)-headingOptimized)>360-angleOfView/2) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
  			 {
  				headingOptimized=(int)heading+360;	
  			 }
   			  if(Math.abs(locHead-headingOptimized)<(angleOfView/2))	// checks to see if it is in view
   			  {
   				Log.d("near", currentlyNear.get(i).title);
   				
   				camover.nearList.add(currentlyNear.get(i).m);
   				camover.xPos.add((float) (((locHead - headingOptimized)+angleOfView/2)%angleOfView)*(theScale)-theScale*2);	//hopefully DIP based
   				camover.yPos.add((float)(camover.yPos.size()*200)); //make sure that the text doesn't overlap
   				camover.invalidate();
   				
   				  }
   				 //rewrite the text
   			  }
   			if(camover.nearList.size()==0) // if currentlyvisable is empty, display no text 
   					{
   				camover.setDisplayText("");
   					}
		}
		}
			
	public void insidecurrentlyVisable()
	{	
		Log.d("INside","this should be working..."+insideBuilding.title);

		
		if(insideBuilding.insideList!=null){
	
		for(Marker m: insideBuilding.insideList)// a loop to see which of those currently near markers are within our angle of view
   		{
			Log.d("nearinside", "ahjalkfdjs");
			
   			Location location = new Location("mloc");
   			  location.setLatitude(m.getPosition().latitude);
   			  location.setLongitude(m.getPosition().longitude);
   			int locHead=(int) myloc.bearingTo(location);
   			int headingOptimized=(int) heading;
   			 if((myloc.bearingTo(location)-headingOptimized)<((-360+(angleOfView/2)))) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
   			 {
   				 locHead=(int)myloc.bearingTo(location)+360;	
   			 }
   			if((myloc.bearingTo(location)-headingOptimized)>(360-(angleOfView/2))) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
  			 {
  				headingOptimized=(int)heading+360;	
  			 }
   			  if(Math.abs(locHead-headingOptimized)<angleOfView/2)	// checks to see if it is in view
   			  {
   				Log.d("nearinside", m.getTitle());
   				if(!camover.nearList.contains(m)){
   				camover.nearList.add(m);
   				camover.xPos.add((float) (((locHead - headingOptimized)+angleOfView/2)%angleOfView)*(theScale)-theScale*2);	//hopefully DIP based
   				camover.yPos.add((float) (400)); //make sure that the text doesn't overlap
   				camover.invalidate();
   			
   				}
   				  }
   				 //rewrite the text
   			  }
   			if(camover.nearList.size()==0) // if currentlyvisable is empty, display no text 

   					{
   				camover.setDisplayText("");
   					}
		}
			}
	
	public void checkCurrentlyNearevents(){
		myloc = currentLocation;
   		mylatlng = new LatLng(myloc.getLatitude(),myloc.getLongitude());
   		currentlyNearEvents.clear();
   		
   		
   		for(Event e: CampusInfo.events)	//loops through all a markers to see which ones are within a certain radius of us
   		{	
   			if(e.m.getPosition()!=null){
   			Log.d("test", "1");
   			double longi= e.m.getPosition().longitude; //converting locations to Doubles as to allow comparison
   			double lati = e.m.getPosition().latitude;
   			double longi1= mylatlng.longitude;
   			double lati1 =mylatlng.latitude;
   			double distance= Math.abs(longi-longi1)+Math.abs(lati-lati1);
   			if(distance<=.00175)	//check to make sure they are in the radius
//   					(longi1+lati1)-(longi-lati)<=.001)
   			{
   				Log.d("test", "2");
   				
   						currentlyNearEvents.add(e);//if it is add it to the array
   						
   						}
   			}
   		}
   			
   		
   		
   		}
	
	public void checkCurrentlyVisableEvents()
	{		 
		
		if(currentlyNearEvents!=null){
			
		for(int i=0; i<(currentlyNearEvents.size()/2); i++)// a loop to see which of those currently near markers are within our angle of view
   		{
   			Location location = new Location("mloc");
   			  location.setLatitude(currentlyNearEvents.get(i).m.getPosition().latitude);
   			  location.setLongitude(currentlyNearEvents.get(i).m.getPosition().longitude);
   			int locHead=(int) myloc.bearingTo(location);
   			int headingOptimized=(int) heading;
   			 if((myloc.bearingTo(location)-headingOptimized)<(-310+angleOfView/2)) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
   			 {
   				 locHead=(int)myloc.bearingTo(location)+360;	
   			 }
   			if((myloc.bearingTo(location)-headingOptimized)>360-angleOfView/2) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
  			 {
  				headingOptimized=(int)heading+360;	
  			 }
   			  if(Math.abs(locHead-headingOptimized)<(angleOfView/2))	// checks to see if it is in view
   			  {
   				Log.d("near", currentlyNearEvents.get(i).title);
   				
   				camover.nearList.add(currentlyNearEvents.get(i).m);
   				camover.xPos.add((float) (((locHead - headingOptimized)+angleOfView/2)%angleOfView)*(theScale)-theScale*2);	//hopefully DIP based
   				camover.yPos.add((float)(camover.yPos.size()*200)); //make sure that the text doesn't overlap
   				camover.invalidate();
   				
   				  }
   				 //rewrite the text
   			  }
   			if(camover.nearList.size()==0) // if currentlyvisable is empty, display no text 
   					{
   				camover.setDisplayText("");
   					}
		}
		}
	
	}

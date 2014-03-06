package com.cs440.capstone;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import android.R.anim;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.GeomagneticField;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.BoringLayout.Metrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraActivity extends Activity implements SensorEventListener{
	
	private static Camera mainCam;
	private CameraPreview camView;
	private SensorManager mSensorManager;
	public ArrayList<Marker> marks = new ArrayList ();
	public ArrayList<Marker> currentlyNear = new ArrayList();
	public ArrayList<Marker> currentlyvisable = new ArrayList();
	public float heading;
	public Location currentLocation;
	Location myloc;
	private boolean mShowText;
	private int mTextPos;
	public CameraOverlay camover;
	public int width=4;
	public int height=4;
	public int dens= 40;
	public int theScale=1;
	private LocationListener onLocationChange;
	private LocationManager lm;
	public LatLng mylatlng;
	
	@SuppressLint("NewApi")
	android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	//pulls and saved state for the activity
		
		setContentView(R.layout.camera_layout);	// sets the layout
		getCameraInstance();	//calls to start up the camera
		camView = new CameraPreview(this, mainCam);	// allows the screen to see what the camera sensor is seeing
		FrameLayout preview = (FrameLayout)findViewById(R.id.camera_layout);	// updates the layout
		preview.addView(camView);	
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);// sets up a sensor manager to help with gps and comapss data
		currentLocation=getGPS();	// gets our current location
		camover = (CameraOverlay)findViewById(R.id.overlay_layout);	//starts up a comeraOverlay instance which will allow us to write on top of the camera
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keeps the Camera from falling asleep
		Display display = getWindowManager().getDefaultDisplay();
		int orientation = display.getRotation();
		if(orientation==0||orientation==4)
		{
			finish();
		}
		
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
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, onLocationChange);
	    }
	    else{
	    	 lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, onLocationChange);
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
	  if(newConfig.orientation == newConfig.ORIENTATION_PORTRAIT){
		  finish();
	  }
	}



    private void MainAcivity() {
		// TODO Auto-generated method stub
		
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
    	checkCurrentlyNear();
    	if(!camover.insidebool)
    	{
    		checkCurrentlyVisable();
    	}
    	else{
    		insidecurrentlyVisable();
    	}
   		camover.setDisplayArray(currentlyvisable);
   		camover.invalidate();
   		Log.d("heading","Heading: " + Float.toString(heading) + " degrees");
   	}
   		
 
 
   		       

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		heading = ((Math.round(event.values[0]+event.values[1])+90)%360); //Rounds the current heading to full degrees
		
		if(currentLocation != null) //make sure that we dont get a null pointer 
		{
			whatshouldwesee();// update the information about what we are near and what is in our view 
		}
	}
	private Location getGPS() 	// get the current gps location of our device
		{
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;
        
        for (int i=providers.size()-1; i>=0; i--) {
                l = lm.getLastKnownLocation(providers.get(i));
                if (l != null) break;
        }
        
    
        return l;
}
	private void screenScale()
	{
		theScale=(width)/100;
		Log.d("DENS", ""+theScale);

	}
	public void checkCurrentlyNear(){
		myloc = currentLocation;
   		mylatlng = new LatLng(myloc.getLatitude(),myloc.getLongitude());
   		
   		currentlyNear.clear();
   		currentlyvisable.clear();
   		camover.xPos.clear();
   		camover.yPos.clear();
   		
   		for(Marker temp : CampusInfo.allMarkers.keySet()){
   			marks.add(temp);
   		}
   		Marker m;
   		for(int i=0; i<marks.size(); i++)	//loops through all a markers to see which ones are within a certain radius of us
   		{	m= marks.get(i);
   			double longi= m.getPosition().longitude; //converting locations to Doubles as to allow comparison
   			double lati = m.getPosition().latitude;
   			double longi1= mylatlng.longitude;
   			double lati1 =mylatlng.latitude;
   			double distance= Math.abs(longi-longi1)+Math.abs(lati-lati1);
   			if(distance<=.00175)	//check to make sure they are in the radius
//   					(longi1+lati1)-(longi-lati)<=.001)
   			{
   				LatLngBounds b=CampusInfo.getBounds(i);
   				if(CampusInfo.allMarkers.get(m).contains(mylatlng))
   						{
   					
   					camover.insidebool=true; 	
   					camover.inside=("Inside "+m.getTitle());
   					currentlyNear.clear();
   					camover.invalidate();
   					Log.d("Inside", "we should be inside");
   					break;
   						}
   					else
   						{
   						currentlyNear.add(m);//if it is add it to the array
   						camover.insidebool=false;
   						}
   			}
   			
   		}
	}
	public void checkCurrentlyVisable()
	{
		for(Marker m: currentlyNear)// a loop to see which of those currently near markers are within our angle of view
   		{
   			Location location = new Location("mloc");
   			  location.setLatitude(m.getPosition().latitude);
   			  location.setLongitude(m.getPosition().longitude);
   			int locHead=(int) myloc.bearingTo(location);
   			int headingOptimized=(int) heading;
   			 if((myloc.bearingTo(location)-headingOptimized)<(-310)) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
   			 {
   				 locHead=(int)myloc.bearingTo(location)+360;	
   			 }
   			if((myloc.bearingTo(location)-headingOptimized)>310) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
  			 {
  				headingOptimized=(int)heading+360;	
  			 }
   			  if(Math.abs(locHead-headingOptimized)<50)	// checks to see if it is in view
   			  {
   				currentlyvisable.add(m);	//add it to the viewable array
   				Log.d("near", m.getTitle());

   				camover.xPos.add((float) (((locHead - headingOptimized)+50)%100)*(theScale)-theScale*2);	//hopefully DIP based
   				camover.yPos.add((float) (camover.xPos.size()*100)); //make sure that the text doesn't overlap
   				 //rewrite the text
   			  }
   			if(currentlyvisable.size()==0) // if currentlyvisable is empty, display no text 

   					{
   				camover.setDisplayText("");
   					}
   		}
	}
	public void insidecurrentlyVisable()
	{	Marker m;
		ArrayList<Marker> temp = new ArrayList();

		for(int i=0; i<CampusInfo.insideMarks.size(); i++)	//loops through all a markers to see which ones are within a certain radius of us
   		{	
			m= CampusInfo.insideMarks.get(i);
   			double longi= m.getPosition().longitude; //converting locations to Doubles as to allow comparison
   			double lati = m.getPosition().latitude;
   			double longi1= mylatlng.longitude;
   			double lati1 =mylatlng.latitude;
   			double distance= Math.abs(longi-longi1)+Math.abs(lati-lati1);
   			if(distance<=.00075)	//check to make sure they are in the radius
//   					(longi1+lati1)-(longi-lati)<=.001)
   			{
   				temp.add(m);//if it is add it to the array
   				}
	
		for(Marker m1: temp)// a loop to see which of those currently near markers are within our angle of view
   		{
   			Location location = new Location("mloc");
   			  location.setLatitude(m1.getPosition().latitude);
   			  location.setLongitude(m1.getPosition().longitude);
   			int locHead=(int) myloc.bearingTo(location);
   			int headingOptimized=(int) heading;
   			 if((myloc.bearingTo(location)-headingOptimized)<(-310)) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
   			 {
   				 locHead=(int)myloc.bearingTo(location)+360;	
   			 }
   			if((myloc.bearingTo(location)-headingOptimized)>310) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
  			 {
  				headingOptimized=(int)heading+360;	
  			 }
   			  if(Math.abs(locHead-headingOptimized)<50)	// checks to see if it is in view
   			  {
   				currentlyvisable.add(m1);	//add it to the viewable array
   				Log.d("near", m1.getTitle());

   				camover.xPos.add((float) (((locHead - headingOptimized)+50)%100)*(theScale)-theScale*2);	//hopefully DIP based
   				camover.yPos.add((float) (camover.xPos.size()*100)); //make sure that the text doesn't overlap
   				 //rewrite the text
   			  }
   			if(currentlyvisable.size()==0) // if currentlyvisable is empty, display no text 

   					{
   				camover.setDisplayText("");
   					}
		}
	}
			
	
	}
	}


package com.cs440.capstone;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.BoringLayout.Metrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class CameraActivity extends Activity implements SensorEventListener{
	
	private static Camera mainCam;
	private CameraPreview camView;
	private SensorManager mSensorManager;
	public ArrayList<Marker> allMarkers = new ArrayList ();
	public ArrayList<Marker> currentlyNear = new ArrayList();
	public ArrayList<Marker> currentlyvisable = new ArrayList();
	public float heading;
	public Location currentLocation;
	private boolean mShowText;
	private int mTextPos;
	public CameraOverlay camover;
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
   		Location myloc = currentLocation;
   		LatLng mylatlng = new LatLng(myloc.getLatitude(),myloc.getLongitude());
   		
   		currentlyNear.clear();
   		currentlyvisable.clear();
   		camover.xPos.clear();
   		camover.yPos.clear();
   		ArrayList<Marker>  marks= CampusInfo.getall();
   		for(Marker m: marks)	//loops through all a markers to see which ones are within a certain radius of us
   		{
   			double longi= m.getPosition().longitude; //converting locations to Doubles as to allow comparison
   			double lati = m.getPosition().latitude;
   			double longi1= mylatlng.longitude;
   			double lati1 =mylatlng.latitude;
   			if(Math.abs(longi-longi1) <= .001 && Math.abs(lati-lati1) <= .001)	//check to make sure they are in the radius
//   					(longi1+lati1)-(longi-lati)<=.001)
   			{
   				currentlyNear.add(m);	//if it is add it to the array
   			}
   			
   		}
   		int dens=(int) getResources().getDisplayMetrics().density;
   		
   		for(Marker m: currentlyNear)// a loop to see which of those currently near markers are within our angle of view
   		{
   			Location location = new Location("mloc");
   			  location.setLatitude(m.getPosition().latitude);
   			  location.setLongitude(m.getPosition().longitude);
   			int locHead=(int) myloc.bearingTo(location);
   			 if((myloc.bearingTo(location)-heading)<(-310)) //if the headings cross from 359-0 we will treat the bearing as if it is actually over 360
   			 {
   				 locHead=(int)myloc.bearingTo(location)+360;	
   			 }
   			  if(Math.abs(locHead-heading)<50)	// checks to see if it is in view
   			  {
   				currentlyvisable.add(m);	//add it to the viewable array
   				Log.d("near", m.getTitle());
<<<<<<< HEAD
   				camover.xPos.add((float) (((locHead - heading)+50))*(dens*4));	//hopefully DIP based
   				camover.yPos.add((float) (camover.xPos.size()*100)); //make sure that the text doesn't overlap
   				camover.setDisplayText(m.getTitle()); //rewrite the text
   			  }
   			
   			if(currentlyvisable.size()==0)	//if nothing is in view reset to a blank string
=======
   				camover.setDisplayText(m.getTitle()); // update the text being written to the screen
   				Button btn = new Button(this); //create a new button
   				btn.setText(m.getTitle()); // set the text of the button
   				btn.setBackgroundColor(Color.RED); // set the color of the button
   				btn.setTextColor(Color.WHITE); // set the color of the text
   				camover.setDisplayButton(btn); // set the button to the screen
   			}
   			if(currentlyvisable.size()==0) // if currentlyvisable is empty, display no text 
>>>>>>> 127eec836715416d4554a56fc0e7567743a67a2c
   					{
   				camover.setDisplayText("");
   					}
   		}
   		camover.setDisplayArray(currentlyvisable);
   		Log.d("Heading","Heading: " + Float.toString(heading) + " degrees");
   	}
   		public void onMyLocationChange(Location location) {				
   			//add the adjustment value for true north magnetic north difference 
   			currentLocation = location;	//update our location
   			whatshouldwesee();	//update the markers that we are near and should be able to view
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
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;
        
        for (int i=providers.size()-1; i>=0; i--) {
                l = lm.getLastKnownLocation(providers.get(i));
                if (l != null) break;
        }
        
    
        return l;
}
	
}

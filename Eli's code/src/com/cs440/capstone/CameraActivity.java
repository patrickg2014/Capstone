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
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.FrameLayout;

public class CameraActivity extends Activity implements SensorEventListener{
	
	private static Camera mainCam;
	private CameraPreview camView;
	private SensorManager mSensorManager;
	public ArrayList<Marker> allMarkers = new ArrayList ();
	public ArrayList<Marker> currentlyvisable = new ArrayList();
	public ArrayList<Marker> currentlyvisable1 = new ArrayList();
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_layout);
		getCameraInstance();
		camView = new CameraPreview(this, mainCam);
		FrameLayout preview = (FrameLayout)findViewById(R.id.camera_layout);
		preview.addView(camView);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		currentLocation=getGPS();
		camover = (CameraOverlay)findViewById(R.id.overlay_layout);
		
		
		
		Log.d("Changing", "LETS GO!");
	}

	public static Camera getCameraInstance(){
		mainCam = null;
		try{
			
			
			mainCam = Camera.open();
	
			
		}catch(Exception e){
			//Camera is not available
		}
		return mainCam;
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		
	}
	
    public static int getScreenOrientation(Activity activity) {
		return 0;
        
      }
    
    
	
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
    protected void onPause() {
        super.onPause();      // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
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
   		
   		currentlyvisable.clear();
   		ArrayList<Marker>  marks= CampusInfo.getall();
   		for(Marker m: marks){
   			double longi= m.getPosition().longitude;
   			double lati = m.getPosition().latitude;
   			double longi1= mylatlng.longitude;
   			double lati1 =mylatlng.latitude;
   			if(Math.abs(longi-longi1) <= .001 && Math.abs(lati-lati1) <= .001)
//   					(longi1+lati1)-(longi-lati)<=.001)
   			{
   				currentlyvisable.add(m);
   			}
   			
   		}
   		float mybearing =heading;
   		for(Marker m: currentlyvisable){
   			Location location = new Location("mloc");
   			  location.setLatitude(m.getPosition().latitude);
   			  location.setLongitude(m.getPosition().longitude);
   			if(Math.abs(myloc.bearingTo(location)-mybearing)<60)
   			{
   				currentlyvisable1.add(m);
   				Log.d("near", m.getTitle() + " is near");
   				
   			}
   			camover.setDisplayText(m.getTitle());
   		}
   	}
   		public void onMyLocationChange(Location location) {
   			if(currentLocation != location)
   			{
   			currentLocation = location;
   			whatshouldwesee();
   		       for(Marker m: currentlyvisable)
   		    	   Log.d("logout", m.getTitle());
   			}
   		}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		heading = Math.round(event.values[0]);
		Log.d("Heading","Heading: " + Float.toString(heading) + " degrees");
		if(currentLocation != null)
		{
			whatshouldwesee();
		}
	}
	private Location getGPS() {
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

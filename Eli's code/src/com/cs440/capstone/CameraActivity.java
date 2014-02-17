package com.cs440.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.FrameLayout;

public class CameraActivity extends Activity implements SensorEventListener{
	
	private static Camera mainCam;
	private CameraPreview camView;
	private SensorManager mSensorManager;

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

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		float degree = Math.round(event.values[0]);
		Log.d("Heading","Heading: " + Float.toString(degree) + " degrees");
		
	}
	
}

package com.cs440.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.FrameLayout;

public class CameraActivity extends Activity{
	
	private static Camera mainCam;
	private CameraPreview camView;
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
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		Log.d("Changing", "is this it" + this.getResources().getConfiguration().orientation);
		if(this.getResources().getConfiguration().orientation == 2){
			mainCam.setDisplayOrientation(0);
		}else{
			mainCam.setDisplayOrientation(90);
		}
	}
	
    public static int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
          if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270) {
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
          } else {
            return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
          }
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
          if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
            return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
          } else {
            return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
          }
        }
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
      }
	
	
	
	
	
}

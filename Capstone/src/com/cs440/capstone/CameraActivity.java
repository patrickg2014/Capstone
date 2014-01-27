package com.cs440.capstone;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;

public class CameraActivity extends Activity{
	
	private static Camera mainCam;
	private CameraPreview camView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_layout);
		getCameraInstance();
		camView = new CameraPreview(this, mainCam);
		FrameLayout preview = (FrameLayout)findViewById(R.id.camera_layout);
		preview.addView(camView);
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
}

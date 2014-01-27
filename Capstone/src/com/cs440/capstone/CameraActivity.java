package com.cs440.capstone;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;

public class CameraActivity extends Activity{
	
	private static Camera mainCam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_layout);
		getCameraInstance();
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

package com.cs440.capstone;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
        	Log.d("Changing", "surface created method");
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
            
        } catch (IOException e) {
            Log.d("TAG", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    	
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
    	Log.d("Changing", "yppppppppppppksedfpdomszdfmds");
        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        if (!mHolder.getSurface().equals(android.hardware.Sensor.TYPE_ORIENTATION)){
        	Log.d("Changing", "???");
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
            Log.d("Changing", "just stopped");
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        	Log.d("Changing", "just stopped BADDDDDDDDDDDDDDDDDDDDDD");
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // Checks the orientation of the screen for landscape and portrait and set portrait mode always
       //if portrait.. be set at 0
       // if landscape set at 90
       // mCamera.setDisplayOrientation(90);
        
        // start preview with new settings
        try {
        	Log.d("Changing", "starting again");
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("TAG", "Error starting camera preview: " + e.getMessage());
        }
    }
    

}
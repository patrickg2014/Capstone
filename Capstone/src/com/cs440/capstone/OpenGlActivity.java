package com.cs440.capstone;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;



public class OpenGlActivity extends Activity{

	private static final String TAG = "GLScene"; //for logging/debugging

	private GLSurfaceView _GLView; //the view that we're actually drawing

	/**
	 * Called when the activity is started
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        _GLView = new MyGLSurfaceView(this);
        setContentView(_GLView);
	}

	protected void onPause() {
		super.onPause();
		_GLView.onPause(); //tell the view to pause
	}

	protected void onResume() {
		super.onResume();
		_GLView.onResume(); //tell the view to resume
	}


}

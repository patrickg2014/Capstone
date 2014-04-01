package com.cs440.capstone;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * A basic activity for displaying a simple OpenGL rendering. This uses a slightly different structure than
 * with a regular Canvas.
 * 
 * @author Joel and Patrick Green
 * @version Fall 2013
 */
public class OpenGlActivity extends Activity implements OnTouchListener
{
	// Our OpenGL Surfaceview
    private GLSurfaceView glSurfaceView;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
 
        // Turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        // Super
        super.onCreate(savedInstanceState);
 
        // Fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
 
        // We create our Surfaceview for our OpenGL here.
        glSurfaceView = new GLSurf(this);
 
        // Set our view.
        setContentView(glSurfaceView);
 
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d("Touch", "Activity!");
		glSurfaceView.onTouchEvent(event);
		return false;
	}
 
}
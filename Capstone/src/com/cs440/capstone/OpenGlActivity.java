package com.cs440.capstone;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * A basic activity for displaying a simple OpenGL rendering. This uses a slightly different structure than
 * with a regular Canvas.
 * 
 * @author Joel and Patrick Green
 * @version Fall 2013
 */
public class OpenGlActivity extends Activity
{
	private static final String TAG = "GLBasic"; //for logging/debugging
	private GLSurfaceView _GLView; //the view that we're actually drawing

	/**
	 * Called when the activity is started
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_GLView = new GLBasicView(this); //we set the GLView programmatically rather than with the XML
		setContentView(_GLView);

		//we can build layout systems and add them in here

	}

	protected void onPause() {
		super.onPause();
		_GLView.onPause(); //tell the view to pause
	}

	protected void onResume() {
		super.onResume();
		_GLView.onResume(); //tell the view to resume
	}


	/**
	 * The actual view itself, includes as an inner class. Note that this also controls interaction (but not rendering)
	 * We put the OpenGL rendering in a separate class
	 */
	public class GLBasicView extends GLSurfaceView
	{
		private SceneRenderer _hexRenderer;
		
		public GLBasicView(Context context) {
			super(context);

			setEGLContextClientVersion(2); //specify OpenGL ES 2.0
			super.setEGLConfigChooser(8, 8, 8, 8, 16, 0); //may be needed for some targets; specifies 24bit color

			_hexRenderer = new SceneRenderer(context);
			setRenderer(_hexRenderer); //set the renderer

			/* 
			 * Render the view only when there is a change in the drawing data.
			 * We comment this out when we don't have UI (just animation)
			 */
			setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
		
		//This code below concept was used from the Google OpenGLES20Complete code
		    private float previousX; //The previous x touch point
		    private float mPreviousY; //The previous y touch point

		    /**
		     * This method is called when the user touches the device input screen
		     */
		    @Override
		    public boolean onTouchEvent(MotionEvent e) {
		        // MotionEvent reports input details from the touch screen
		        // and other input controls. In this case, you are only
		        // interested in events where the touch position changed.

		        float x = e.getX(); //The x point that is touched
		        float y = e.getY(); //The y point that is touched
		        switch (e.getAction()) {
		            case MotionEvent.ACTION_MOVE://When the movement is dragged we will do math to calculate the change in distance
		            	
		                float dx = x - previousX; //The change in x distance of the drag
		                float dy = y - mPreviousY; //The change in y distance of the drag
		                _hexRenderer.move(dx,dy);//This calls the move method in Hexagon Renderer so we can translate the view matrix
		               requestRender(); //Update the frame
		        }

		        previousX = x; //Update the last x touch
		        mPreviousY = y;//Update the last y touch
		        return true;
		    }

	}
}

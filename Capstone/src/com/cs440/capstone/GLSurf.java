package com.cs440.capstone;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
 
public class GLSurf extends GLSurfaceView {
 
    private final GLRenderer mRenderer;
 
    public GLSurf(Context context) {
        super(context);
 
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new GLRenderer(context);
        setRenderer(mRenderer);
        mRenderer.passSurface(this);
 
        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
 
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mRenderer.onPause();
    }
 
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mRenderer.onResume();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
    	Log.d("Touch", "SURFFF!");
      mRenderer.processTouchEvent(e);
      return true;
    }
    
    public GLRenderer getRenderer(){
    	return mRenderer;
    }
 
}
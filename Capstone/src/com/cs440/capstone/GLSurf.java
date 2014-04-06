package com.cs440.capstone;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
 
public class GLSurf extends GLSurfaceView {
 
    private final GLRenderer mRenderer;
	
	public GLSurf(Context context) {
		this(context,null);
	}
	
	public GLSurf(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		setEGLContextClientVersion(2); //specify OpenGL ES 2.0
		setEGLConfigChooser(8, 8, 8, 8, 16, 0); //may be needed for some targets; specifies 24bit color
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mRenderer = new GLRenderer(context);
		setRenderer(mRenderer); //set the renderer

		//render continuously (like for animation). Set to WHEN_DIRTY to manually control redraws (via GLSurfaceView.requestRender())
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//WHEN_DIRTY);
	}
	
	public GLRenderer getRenderer(){
		return mRenderer;
	}
 
}
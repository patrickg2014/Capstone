package com.cs440.capstone;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.android.gms.maps.model.Marker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class CameraOverlay extends ViewGroup {

	private boolean mShowText;
	private int mTextPos;
	public String inside="";
	public boolean insidebool=false;
	public ArrayList<Building> nearList=new ArrayList<Building> ();
	public ArrayList<Float> xPos=new ArrayList<Float> ();
	public ArrayList<Float> yPos=new ArrayList<Float> ();
	private Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ups);
	public CameraActivity camActivity;

	
	public CameraOverlay(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d("Setup","Setting up");
		Rect rect= new Rect();
		rect.set(0, 0, CameraActivity.width, 200);
		
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CameraOverlay, 0, 0);

		try {
			mShowText = a.getBoolean(R.styleable.CameraOverlay_showText, true);
			mTextPos = a.getInteger(R.styleable.CameraOverlay_labelPosition, 0);
		} finally {
			a.recycle();
		}
		setShowText(true);
		setWillNotDraw(false);
	}
	
	public void passActivity(CameraActivity theActivity){
		camActivity = theActivity;
	}
	
	@Override
	public void addView(View child) {
		// TODO Auto-generated method stub
		super.addView(child);
	}
	
	
	protected void onDraw(Canvas canvas) {
		
		Log.d("draw", "WE ARE DRAWING!!");
		Log.d("Setup", "invalidate");
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setTextSize(50);
		
		
		//canvas.drawText(displayText, 300, 300, paint);//draws text at x,y position
		int y= 200;
		Log.d("Test", nearList.size() +"");
		for(int i=0; i<(nearList.size()); i++){
			Log.d("Test", nearList.size() +"");
			
			canvas.drawBitmap(bmp, (float) (xPos.get(i)-(bmp.getWidth())),(float) (yPos.get(i)-(bmp.getHeight()/2)), paint);
			 paint.setStrokeWidth(0);
			 paint.setColor(Color.WHITE);
			canvas.drawText(nearList.get(i).title, xPos.get(i), yPos.get(i), paint);//draws text at x,y position
			 paint.setStrokeWidth(2);
			 paint.setColor(Color.BLACK);
			 canvas.drawText(nearList.get(i).title, xPos.get(i),yPos.get(i), paint);
			 
			 //For touch area testing!
			 canvas.drawLine(xPos.get(i)-100, yPos.get(i)-50, xPos.get(i) + (nearList.get(i).title.length()*25),  yPos.get(i)+100, paint);
			y = y + 200;
		}
			if(insidebool)
				{
					paint.setColor(Color.DKGRAY);
			        paint.setStrokeWidth(50);
			        paint.setStyle(Paint.Style.STROKE);
			        canvas.drawRect(0, 0, CameraActivity.width, 50, paint);
			        paint.setStrokeWidth(0);
			        paint.setColor(Color.WHITE);
			        canvas.drawText(inside, 50, 50, paint);//draws text at x,y position
			        paint.setStrokeWidth(2);
			        paint.setColor(Color.BLACK);
			        canvas.drawText(inside, 50, 50, paint);
		
					}
			 super.onDraw(canvas);
				
			}
	
	
	public boolean isShowText() {
		return mShowText;
	}
	public void setDisplayText(String setText) {
		invalidate();
	}

	public void setShowText(boolean showText) {
		mShowText = showText;
		invalidate();
		requestLayout();
	}

	public void setDisplayArray(ArrayList<Building> currentlyNear) {
		nearList = currentlyNear;
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.d("Touch","Hello !!!!!!!!!!!! AHHHHHHHH");
		float x = event.getX();
		float y = event.getY();
		Log.d("Touch",x+"   "+y);
	switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	    	for(int i=0; i<xPos.size(); i++){
	    		
	    		if(x >= xPos.get(i)-100 && x <= xPos.get(i) + (nearList.get(i).title.length()*25)){
	    			if(y >= yPos.get(i)-50 && y <= yPos.get(i)+100){
	    				Log.d("Touch", nearList.get(i).title+"  it worked>>>>>>.");
	    				camActivity.buildingActivity(nearList.get(i).title, "Look");
	    			}
	    		}
	    	}
	        break;

	    case MotionEvent.ACTION_MOVE:
	    	/*for(int i=0; i<xPos.size(); i++){
	    		
	    		if(x+(nearList.get(i).title.length()*50) >= xPos.get(i) && x-(nearList.get(i).title.length()*50) <= xPos.get(i)){
	    			if(y+50 >= yPos.get(i) && y-50 <= yPos.get(i)){
	    				Log.d("Touch", nearList.get(i).title+"  it worked!!!");
	    			}
	    		}
	    	}*/
	        break;

	    case MotionEvent.ACTION_UP:
	       //do something
	    	for(int i=0; i<xPos.size(); i++){
	    		
	    		if(x >= xPos.get(i)-100 && x <= xPos.get(i) + (nearList.get(i).title.length()*25)){
	    			if(y >= yPos.get(i)-50 && y <= yPos.get(i)+100){
	    				Log.d("Touch", nearList.get(i).title+"  it worked>>>>>>.");
	    				camActivity.buildingActivity(nearList.get(i).title, "Look");
	    			}
	    		}
	    	}
	        break;
	}
	return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
	

	

}

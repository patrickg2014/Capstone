package com.cs440.capstone;


import java.util.ArrayList;

import com.google.android.gms.maps.model.Marker;

import android.content.Context;
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
import android.widget.FrameLayout;

public class CameraOverlay extends View {

	private boolean mShowText;
	private int mTextPos;
	private String displayText="";
	private ArrayList<Marker> nearList=new ArrayList ();
	public ArrayList<Float> xPos=new ArrayList ();
	public ArrayList<Float> yPos=new ArrayList ();
	
	public CameraOverlay(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CameraOverlay, 0, 0);

		try {
			mShowText = a.getBoolean(R.styleable.CameraOverlay_showText, true);
			mTextPos = a.getInteger(R.styleable.CameraOverlay_labelPosition, 0);
		} finally {
			a.recycle();
		}
		setShowText(true);
	}
	
	/*@Override
	public void addView(View child) {
		// TODO Auto-generated method stub
		super.addView(child);
	}*/
	
	

	@Override
	protected void onDraw(Canvas canvas) {
		
		Log.d("draw", "WE ARE DRAWING!!");
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setTextSize(50);
	   ;
		Rect rect = new Rect();
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ups);
		//canvas.drawText(displayText, 300, 300, paint);//draws text at x,y position
		int y= 300;
		for(int i=0; i<nearList.size(); i++){
			canvas.drawBitmap(bmp, xPos.get(i),(float)y-75, paint);
			 paint.setStrokeWidth(0);
			 paint.setColor(Color.WHITE);
			canvas.drawText(nearList.get(i).getTitle(), xPos.get(i)+bmp.getWidth(), (float)y, paint);//draws text at x,y position
			 paint.setStrokeWidth(2);
			 paint.setColor(Color.BLACK);
			 canvas.drawText(nearList.get(i).getTitle(), xPos.get(i)+bmp.getWidth(), (float)y, paint);
			
			y = y + 200;
		}
		super.onDraw(canvas);
	}
	
	public void setDisplayText(String setMe){
		displayText = setMe;
		invalidate();
	}

	
	public boolean isShowText() {
		return mShowText;
	}

	public void setShowText(boolean showText) {
		mShowText = showText;
		invalidate();
		requestLayout();
	}

	public void setDisplayArray(ArrayList<Marker> currentlyNear) {
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
	        // do something
	        break;
	    case MotionEvent.ACTION_MOVE:
	        // do something
	        break;
	    case MotionEvent.ACTION_UP:
	       //do something
	    	for(int i=0; i<xPos.size(); i++){
	    		Log.d("Touch", nearList.get(i).getTitle()+ "  "+ xPos.get(i)+ "  "+yPos.get(i));
	    		if(x+500 >= xPos.get(i) && x-500 <= xPos.get(i)){
	    			if(y+200 >= yPos.get(i) && x-200 <= yPos.get(i)){
	    				Log.d("Touch", nearList.get(i).getTitle()+"  it worked");
	    			}
	    		}
	    	}
	        break;
	}
	return true;
	}

}

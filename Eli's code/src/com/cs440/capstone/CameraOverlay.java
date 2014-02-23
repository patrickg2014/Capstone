package com.cs440.capstone;


import java.util.ArrayList;

import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraOverlay extends View {

	private boolean mShowText;
	private int mTextPos;
	private String displayText="";
	private ArrayList<Marker> nearList;
	private Button displayButton;

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
		paint.setColor(Color.BLACK);
		paint.setTextSize(50);
		//canvas.drawText(displayText, 300, 300, paint);//draws text at x,y position
		int y= 300;
		for(int i=0; i<nearList.size(); i++){
			canvas.drawText(nearList.get(i).getTitle(), 300, y, paint);
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
	
	//Writes the given button to the screen
	public void setDisplayButton(Button btn){
		displayButton = btn;
		invalidate();
	}

}

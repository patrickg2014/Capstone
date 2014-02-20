package com.cs440.capstone;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class CameraOverlay extends View {

	private boolean mShowText;
	private int mTextPos;
	private String displayText="";
	public float xPos=0;
	public float yPos=0;

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
		canvas.drawText(displayText, xPos, 300, paint);//draws text at x,y position
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

}

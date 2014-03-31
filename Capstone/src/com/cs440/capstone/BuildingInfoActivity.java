package com.cs440.capstone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class BuildingInfoActivity extends Activity{
	
	ImageView image;
	TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) //where our app sets up
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.building_info_actvity);
		image = (ImageView)findViewById(R.id.imageView1);
		text = (TextView)findViewById(R.id.textView1);
		Intent intent = getIntent();
		String name = intent.getStringExtra("Name");
		ActionBar ab = getActionBar();
		ab.setTitle(name);
		
		}

}

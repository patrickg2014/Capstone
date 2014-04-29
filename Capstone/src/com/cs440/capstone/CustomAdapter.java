package com.cs440.capstone;

import android.content.Context;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

public class CustomAdapter extends AdapterView {
	private final Context context;
	private final int resourceID;

	public CustomAdapter(Context context, int resource, View view) {
		super(context);
		this.context = context;
		this.resourceID = resource;
	}

	@Override
	public Adapter getAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getSelectedView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAdapter(Adapter adapter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelection(int position) {
		// TODO Auto-generated method stub
		
	}
}

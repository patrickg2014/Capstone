package com.cs440.capstone;

import java.util.ArrayList;


import java.util.List;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


@SuppressLint("NewApi")
public class BuildingInfoActivity extends Activity {
	
	ImageView image;
	TextView text;
	
	 private String[] mOptionTitles;
		private DrawerLayout mDrawerLayout;
	    private ListView mDrawerList;
	    private ActionBarDrawerToggle mDrawerToggle;

	    private CharSequence mDrawerTitle;
	    private CharSequence mTitle;
	    CustomDrawerAdapter adapter;

	    List<DrawerItem> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) //where our app sets up
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.building_info_actvity);
		image = (ImageView)findViewById(R.id.imageView1);
		text = (TextView)findViewById(R.id.textView1);
		Intent intent = getIntent();
		String name = intent.getStringExtra("Name");
		text.setText(intent.getStringExtra("Snippet") + "josfhld;afkhfaslhbalsikjfhlasdkjfblsdkjfblkafsdlkfbjlskadjbflskjbflksdjbkl;sdl/fkasn/lfnsdf/lvnjgfdlsn;lkdnv;kdjfnvk;jndfs;kjvnsldfkjvlkdfl;kab;kjnv;klajnf;vklfdkjvnsdfklbjvsl;k;dfksvn;dfkj;sdknv;dfk;kldn;vkfnd;sjd;fdns;jng;fkdjdn;gjdfns;kjng;sdfkng;ksdfn;jfnf;dkljng;sdkljn;dfklnj;gdflksn;dfln;sldjfn;klnjg;kdfjb;n;kjdfn;sdkfng;sdfkjgn;sdfkln;fdkjn;sdfjgn;dfjg;ldfsng;njg;dfkjnflkasbdl,fkjbsdflkbsdlakbjflkasjdbflkbsdlfkbsd;lakfblskjfbslkd");
		ActionBar ab = getActionBar();
		ab.setTitle(name);
		initDrawer(savedInstanceState);
		}
	
	@SuppressLint("NewApi")
	public void initDrawer(Bundle savedInstanceState){
		mTitle = "test";
		 
	 	mOptionTitles = getResources().getStringArray(R.array.Menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOptionTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
 
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
            }
 
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mTitle);
            }
        };
 
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

	
	//initialize drawer
	dataList = new ArrayList<DrawerItem>();
    mTitle = mDrawerTitle = getTitle();
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
 // Add Drawer Item to dataList
    dataList.add(new DrawerItem("Map", R.drawable.ic_action_email));
    dataList.add(new DrawerItem("Camera", R.drawable.ic_action_camera));
    dataList.add(new DrawerItem("Tour", R.drawable.ic_action_gamepad));
    dataList.add(new DrawerItem("Navigate", R.drawable.ic_action_labels));
    dataList.add(new DrawerItem("Search", R.drawable.ic_action_search));
    dataList.add(new DrawerItem("Facebook", R.drawable.ic_action_cloud));
    dataList.add(new DrawerItem("About", R.drawable.ic_action_about));
    dataList.add(new DrawerItem("Settings", R.drawable.ic_action_settings));
    dataList.add(new DrawerItem("Help", R.drawable.ic_action_help));
    adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
            dataList);

    mDrawerList.setAdapter(adapter);
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);
     
    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
          public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                                                          // onPrepareOptionsMenu()
          }
     
          public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                                                          // onPrepareOptionsMenu()
          }
    };
     
    mDrawerLayout.setDrawerListener(mDrawerToggle);
     
    if (savedInstanceState == null) {
          selectItem(0);
    }
    
    
	}
	
	public void selectItem(int possition) {
		 
        mDrawerList.setItemChecked(possition, true);
        if(dataList.get(possition).getItemName().contentEquals("Camera")){
        	Log.d("Test", "CameraTiime");
        	//cameraActivity();
        }
        if(dataList.get(possition).getItemName().contentEquals("Facebook")){
        	Log.d("Test", "CameraTiime");
        	// Intent intent = new Intent(MainActivity.this, LoginUsingLoginFragmentActivity.class);
            // startActivity(intent);
        	
        }
        if(dataList.get(possition).getItemName().contentEquals("About")){
        	Log.d("Test", "OPENGLLLLLLLLLLL");
        	// Intent intent = new Intent(MainActivity.this, OpenGlActivity.class);
            // startActivity(intent);
        	
        }
        mDrawerLayout.closeDrawer(mDrawerList);

  }
	private class DrawerItemClickListener implements
    ListView.OnItemClickListener {
@Override
public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
    selectItem(position);

}
}
	

}

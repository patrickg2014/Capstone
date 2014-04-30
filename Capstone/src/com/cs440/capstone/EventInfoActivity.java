package com.cs440.capstone;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseImageView;


@SuppressLint("NewApi")
public class EventInfoActivity extends Activity {
	
	ParseImageView image;
	TextView text;
	
	 private String[] mOptionTitles;
		private DrawerLayout mDrawerLayout;
	    private ListView mDrawerList;
	    private ActionBarDrawerToggle mDrawerToggle;

	    private CharSequence mDrawerTitle;
	    private CharSequence mTitle;
	    CustomDrawerAdapter adapter;
	    public String name;
	    List<DrawerItem> dataList;
	    private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) //where our app sets up
		{
		super.onCreate(savedInstanceState);
		Log.d("event", "got to the event info activity");
		Parse.initialize(this, "bh3zRUQ5KI43dx5dcES5s5RelhfunoxR1Q9p0MFa",
				"GeAe5yOfQPOZ3FwYOCHSJGn6ldAUIkRuXjY8koHD");
		setContentView(R.layout.event_info_activity);
		//image = (ParseImageView)findViewById(R.id.imageViewParse);
		text = (TextView)findViewById(R.id.textView1);
		ImageView  view = (ImageView)findViewById(R.id.imageView1);
		text.setText(" ");
		Intent intent = getIntent();
		name = intent.getStringExtra("Name");
		
		String description = intent.getStringExtra("Snippet")+"\n \n";
		ActionBar ab = getActionBar();
		ab.setTitle(name);
		initDrawer(savedInstanceState);
		
		for(Event E: CampusInfo.events)
		{
			Log.d("imageload", "should have looped to loaded");
			Log.d("imageload", E.title+"  "+name);
			if(E.title.equals(name)){
				if(E.start!=E.end){
					description=  "Starts at  "+E.start+"\n\n"+"Ends at  "+E.end+"\n\n"+description;
					}
					else{
						description=  "Starts at  "+E.start+"\n\n"+description;
					}
				text.setText(description);
				try {
					Log.d("imageload", E.pic + "");
				    URL myFileUrl = new URL (E.pic);
				    Log.d("imageload", "url");
				    
				    // Create an object for subclass of AsyncTask
			        DownloadImageTask task = new DownloadImageTask(view);
			        // Execute the task
			        task.execute(new String[] {E.pic});
			        
				    /*HttpsURLConnection conn =
				      (HttpsURLConnection) myFileUrl.openConnection();
				    Log.d("imageload", "openConnection");
				    conn.setDoInput(true);
				    Log.d("imageload", "input");
				    conn.connect();
				    Log.d("imageload", "connect");
				    InputStream is = conn.getInputStream();
				    Log.d("imageload", "getStream");
				    view.setImageBitmap(BitmapFactory.decodeStream(is));*/
				 
				   Log.d("imageload","should have worked");
				 
				  } catch (IOException e) {
				    e.printStackTrace();
				  } catch (Exception e) {
				    e.printStackTrace();
				  }
		break;
		}
		}
		
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
    dataList.add(new DrawerItem("Map", R.drawable.map));
    dataList.add(new DrawerItem("Camera", R.drawable.ic_action_camera));
    dataList.add(new DrawerItem("Tour", R.drawable.ic_action_gamepad));
    dataList.add(new DrawerItem("Navigate", R.drawable.ic_action_labels));
    dataList.add(new DrawerItem("Search", R.drawable.ic_action_search));
    if ((MainActivity.currentUser != null) && ParseFacebookUtils.isLinked(MainActivity.currentUser)) {
        dataList.add(new DrawerItem("Log Out Of Facebook", R.drawable.ic_action_cloud));
        }else{
        	dataList.add(new DrawerItem("Log In To Facebook", R.drawable.ic_action_cloud));
        }
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
        mDrawerList.setItemChecked(possition, false);
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
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		  ImageView bmImage;

		  public DownloadImageTask(ImageView bmImage) {
		      this.bmImage = bmImage;
		  }

		  protected Bitmap doInBackground(String... urls) {
			  Bitmap map = null;
	            for (String url : urls) {
	                map = downloadImage(url);
	            }
	            return map;
		  }
		  
		// Creates Bitmap from InputStream and returns it
	        private Bitmap downloadImage(String url) {
	            Bitmap bitmap = null;
	            InputStream stream = null;
	            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	            bmOptions.inSampleSize = 1;
	 
	            try {
	                stream = getHttpConnection(url);
	                bitmap = BitmapFactory.
	                        decodeStream(stream, null, bmOptions);
	                stream.close();
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
	            return bitmap;
	        }
	        
	     // Makes HttpURLConnection and returns InputStream
	        private InputStream getHttpConnection(String urlString)
	                throws IOException {
	            InputStream stream = null;
	            URL url = new URL(urlString);
	            URLConnection connection = url.openConnection();
	 
	            try {
	                HttpURLConnection httpConnection = (HttpURLConnection) connection;
	                httpConnection.setRequestMethod("GET");
	                httpConnection.connect();
	 
	                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	                    stream = httpConnection.getInputStream();
	                }
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	            return stream;
	        }
	        
	        

		  protected void onPostExecute(Bitmap result) {
		      bmImage.setImageBitmap(result);
		  }
		}
	

}


package com.cs440.capstone;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs440.capstone.MainActivity.DrawerItemClickListener;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;


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
	public void initDrawer(Bundle savedInstanceState) {
		mTitle = "test";

		mOptionTitles = getResources().getStringArray(R.array.Menu);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mOptionTitles));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
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

		// initialize drawer
		dataList = new ArrayList<DrawerItem>();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// Add Drawer Item to dataList
		dataList.add(new DrawerItem("Map", R.drawable.map));
		dataList.add(new DrawerItem("Camera", R.drawable.ic_action_camera));
		dataList.add(new DrawerItem("Search", R.drawable.ic_action_search));
		if ((MainActivity.currentUser != null) && ParseFacebookUtils.isLinked(MainActivity.currentUser)
				&& ParseFacebookUtils.getSession().isOpened()) {
			dataList.add(new DrawerItem("Log Out Of Facebook",
					R.drawable.facebooklogosmall));
		} else if ((MainActivity.currentUser != null)
				&& ParseFacebookUtils.isLinked(MainActivity.currentUser)
				&& !ParseFacebookUtils.getSession().isOpened()) {
			dataList.add(new DrawerItem("Log In To Facebook",
					R.drawable.facebooklogosmall));
		} else {
			dataList.add(new DrawerItem("Log In To Facebook",
					R.drawable.facebooklogosmall));

		}
		dataList.add(new DrawerItem("Share Location",
				R.drawable.ic_action_place));
		dataList.add(new DrawerItem("Remove Your Location",
				R.drawable.ic_action_place));
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
		
		
		if (dataList.get(possition).getItemName().contentEquals("Camera")) {
			Log.d("Test", "CameraTiime");
			cameraActivity();
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (dataList.get(possition).getItemName()
				.contentEquals("Share Location")) {
			Log.d("Test", "share prompt");

			if (ParseUser.getCurrentUser() != null
					&& !ParseFacebookUtils.getSession().isClosed()) {

				sharePrompt();

			}

			if (ParseUser.getCurrentUser() == null
					|| ParseFacebookUtils.getSession().isClosed()) {
				Toast.makeText(
						this,
						"You Need to be logged in to Facebook to share your location with Friends",
						Toast.LENGTH_LONG).show();
			}
			mDrawerLayout.closeDrawer(mDrawerList);
		}

		if (dataList.get(possition).getItemName().contentEquals("About")) {
			Log.d("Test", "About screen");
			aboutActivity();
			mDrawerLayout.closeDrawer(mDrawerList);
		}

		if (dataList.get(possition).getItemName()
				.contentEquals("Remove Your Location")) {
			Log.d("Test", "Remove Location");
			removeLocation();
		}
		if (dataList.get(possition).getItemName().contentEquals("Settings")) {
			Log.d("Test", "Settings screen");
			settingsActivity();
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (dataList.get(possition).getItemName().contentEquals("Help")) {
			Log.d("test", "Help screen");
			helpActivity();
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		if (dataList.get(possition).getItemName()
				.contentEquals("Log In To Facebook")) {
			Log.d("Test", "facebook in");
			if (ParseUser.getCurrentUser() == null
					|| ParseFacebookUtils.getSession().isClosed()) {
				login(possition);
			}
			dataList.get(possition).setItemName("Log Out Of Facebook");

		} else {
			if (dataList.get(possition).getItemName()
					.contentEquals("Log Out Of Facebook")) {
				Log.d("Test", "facebook out ");

				ParseUser.logOut();

				dataList.get(possition).setItemName("Log In To Facebook");
			}

		}
		mDrawerList.setItemChecked(possition, false);
	}
	
	public void sharePrompt() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		Log.d("prompt", "this should happen every time");

		alert.setTitle("Share your location");
		alert.setMessage("Message");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Share", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				ParseUser user = ParseUser.getCurrentUser();
				String valstring = value.toString();
				ParseGeoPoint geo = new ParseGeoPoint();
				geo.setLatitude(MainActivity.myLocation.latitude);
				geo.setLongitude(MainActivity.myLocation.longitude);
				if (ParseUser.getCurrentUser() != null) {
					user.put("Location", geo);
					user.put("LocationString", MainActivity.myLocation.latitude+","+MainActivity.myLocation.longitude);
					user.put("locationText", valstring);
					user.put("shareLocation", true);
					user.saveInBackground(new SaveCallback() {
						public void done(com.parse.ParseException e) {

							// Save was successful!
							Log.d("parse", "upload location and text");

							

						}
					});
					MainActivity.shareTime = System.currentTimeMillis();
					MainActivity.shareloc = MainActivity.myLocation;

				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();

	}


	
	private void removeLocation() {
		// TODO Auto-generated method stub
		ParseUser user = ParseUser.getCurrentUser();
		if (ParseUser.getCurrentUser() != null) {
			user.put("shareLocation", false);

			user.saveInBackground(new SaveCallback() {
				public void done(com.parse.ParseException e) {
					if (e == null) {
						// Save was successful!
						Log.d("parse", "upload location");
					} else {
						// Save failed. Inspect e for details.
						Log.d("parse", "failed to upload location");
					}
				}
			});
			Toast.makeText(this, "No longer sharing your location.",
					Toast.LENGTH_SHORT).show();

		}
	}

	public void login(final int possition) {
		if (ParseUser.getCurrentUser() == null
				|| ParseFacebookUtils.getSession().isClosed()) {

			List<String> permissions = Arrays
					.asList("user_photos, user_events, user_friends, user_location, user_activities, friends_events");
			ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException err) {

					if (user == null) {
						Log.d("facebook",
								"Uh oh. The user cancelled the Facebook login.");
						Log.d("Test", "facebook should have fqled");
						dataList.get(possition).setItemName(
								"Log In To Facebook");

					} else if (user.isNew()) {
						Log.d("facebook",
								"User signed up and logged in through Facebook!");
						Log.d("Test", "facebook should have fqled");
						dataList.get(possition).setItemName(
								"Log Out Of Facebook");
						MainActivity.storecall(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);
						MainActivity.friendcall(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);
						MainActivity.call(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);

						ParseFacebookUtils.saveLatestSessionData(MainActivity.currentUser);
						ParseInstallation installation = ParseInstallation
								.getCurrentInstallation();
						installation.put("user", ParseUser.getCurrentUser());
						installation.saveInBackground();

					} else {
						Log.d("facebook", "User logged in through Facebook!");
						Log.d("Test", "facebook should have fqled");
						dataList.get(possition).setItemName(
								"Log Out Of Facebook");
						MainActivity.storecall(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);
						MainActivity.friendcall(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);
						MainActivity.call(ParseFacebookUtils.getSession(),
								ParseFacebookUtils.getSession().getState(), err);
						ParseInstallation installation = ParseInstallation
								.getCurrentInstallation();
						installation.put("user", ParseUser.getCurrentUser());
						installation.saveInBackground();

					}
				}
			});
			if (ParseFacebookUtils.getSession().isOpened()) {
				dataList.get(possition).setItemName("Log Out Of Facebook");
			} else {
				dataList.get(possition).setItemName("Log In To Facebook");
			}
		}

	}

	
	public void cameraActivity() // what allows us to switch to the camera
	// activity on button click
{

Intent intent = new Intent(this, CameraActivity.class);
startActivity(intent);
}

public void aboutActivity() // what allows us to switch to the camera
// activity on button click
{

Intent intent = new Intent(this, AboutActivity.class);
startActivity(intent);
}

public void searchActivity() {

Intent intent = new Intent(this, CampusInfoSearch.class);
startActivity(intent);

}

public void settingsActivity() // what allows us to switch to the camera
	// activity on button click
{

Intent intent = new Intent(this, SettingsActivity.class);
startActivity(intent);
}

public void helpActivity() // what allows us to switch to the camera
// activity on button click
{

Intent intent = new Intent(this, HelpActivity.class);
startActivity(intent);
}

public void buildingActivity(String buildingName, String snippet) {
Intent intent = new Intent(this, BuildingInfoActivity.class);
intent.putExtra("Name", buildingName);
intent.putExtra("Snippet", snippet);
startActivity(intent);
}

public void eventActivity(String eventname, String snippet) {
Intent intent = new Intent(this, EventInfoActivity.class);
intent.putExtra("Name", eventname);
intent.putExtra("Snippet", snippet);
startActivity(intent);
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


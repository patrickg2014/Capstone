package com.cs440.capstone;


import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.google.android.gms.maps.model.LatLng;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class LoginActivity extends Activity {

	private Button loginButton;
	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "bh3zRUQ5KI43dx5dcES5s5RelhfunoxR1Q9p0MFa",
				"GeAe5yOfQPOZ3FwYOCHSJGn6ldAUIkRuXjY8koHD");
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		setContentView(R.layout.main);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});

		// Check if there is a currently logged in user
		// and they are linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
			showUserDetailsActivity();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void onLoginButtonClicked() {
		LoginActivity.this.progressDialog = ProgressDialog.show(
				LoginActivity.this, "", "Logging in...", true);
		List<String> permissions = Arrays.asList("user_photos, user_events, user_friends, user_location, user_activities, friends_events");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				LoginActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d("facebook",
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d("facebook",
							"User signed up and logged in through Facebook!");
					showUserDetailsActivity();
				} else {
					Log.d("facebook",
							"User logged in through Facebook!");
					call( ParseFacebookUtils.getSession() ,  ParseFacebookUtils.getSession().getState(), err);
				}
			}
		});
	}

	private void showUserDetailsActivity() {
		Intent intent = new Intent(this, UserDetailsActivity.class);
		startActivity(intent);
	}
	
	
		 public void call(Session session, SessionState state, Exception exception) {
         	
         	String fqlQuery = 
         			
         			"SELECT name,  venue, description, start_time,end_time, eid " +
                  			"FROM event " +
                  			"WHERE eid IN (" +
                  			"SELECT eid " +
                  			"FROM event_member " +
                  			"WHERE (uid IN (" +
                  			"SELECT uid2 " +
                  			"FROM friend " +
                  			"WHERE uid1 = me())  " +
                  			"OR uid = me())limit 10000) " +
                  			"AND  (end_time>now() OR start_time>now()) "+
                  			"AND venue.latitude > \""+(CampusInfo.map.getMyLocation().getLatitude()-.1)+"\""+
                  			"AND venue.latitude < \""+(CampusInfo.map.getMyLocation().getLatitude()+.1)+"\""+
                  			"AND venue.longitude < \""+(CampusInfo.map.getMyLocation().getLongitude()+.1)+"\""+
                  			"AND venue.longitude  >\""+(CampusInfo.map.getMyLocation().getLongitude()-.1)+"\"";
         	Bundle params = new Bundle();
         	
         	params.putString("q", fqlQuery);
         	
         	Session session1 = Session.getActiveSession();
         	
         	
         	 
         	Request request = new Request(session1, 
         	    "/fql", 
         	    params, 
         	    HttpMethod.GET, 
         	    new Request.Callback(){ 
         	        public void onCompleted(Response response) {
         	        //Log.i("fql", "Got results: " + response.toString());
         	       
         	        try
         	        {
         	            GraphObject go  = response.getGraphObject();
         	            JSONObject  jso = go.getInnerJSONObject();
         	            JSONArray array =  jso.getJSONArray("data");

         	            		// loop
         	            		for(int i = 0; i < array.length(); i++) {
         	            		 JSONObject obj = array.getJSONObject(i);
         	            		 JSONObject jb1= new JSONObject(obj.getString("venue"));
         	            		 String name = obj.getString("name");
         	            		 String description = obj.getString("description");
         	            		 
         	            		 String lat = jb1.getString("latitude");
         	            		 String longi = jb1.getString("longitude");
         	            		 CampusInfo.events.add(new Event(name, description,new LatLng(Double.parseDouble(lat),Double.parseDouble(longi))));
         	            		 Log.d("fql", name+" "+description+" ");
         	            		}
         	               
         	                
         	            
         	        }
         	        catch ( Throwable t )
         	        {
         	            t.printStackTrace();
         	        }
         	    }
         	});
         	
         	Request.executeBatchAsync(request);
         	for(Building b: CampusInfo.all)
         	{
         		b.makeMarker();
         		
         	}

         }
         
	
}

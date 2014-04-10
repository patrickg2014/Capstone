package com.cs440.capstone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class UserDetailsActivity extends Activity {

	private ProfilePictureView userProfilePictureView;
	private TextView userNameView;
	private TextView userLocationView;
	private TextView userGenderView;
	private TextView userDateOfBirthView;
	private TextView userRelationshipView;
	private Button logoutButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.userdetails);

		userProfilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
		userNameView = (TextView) findViewById(R.id.userName);
		userLocationView = (TextView) findViewById(R.id.userLocation);
		userGenderView = (TextView) findViewById(R.id.userGender);
		userDateOfBirthView = (TextView) findViewById(R.id.userDateOfBirth);
		userRelationshipView = (TextView) findViewById(R.id.userRelationship);

		logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogoutButtonClicked();
			}
		});

		// Fetch Facebook user info if the session is active
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			makeMeRequest();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// Check if the user is currently logged
			// and show any cached content
			updateViewsWithProfileInfo();
		} else {
			// If the user is not logged in, go to the
			// activity showing the login view.
			startLoginActivity();
		}
	}

	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					
				
					
					public void onCompleted(GraphUser user,Response response) {
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
						
					}

					
				}); {
					/*@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							// Create a JSON object to hold the profile info
							JSONObject userProfile = new JSONObject();
							try {
								// Populate the JSON object
								userProfile.put("facebookId", user.getId());
								userProfile.put("name", user.getName());
								if (user.getLocation().getProperty("name") != null) {
									userProfile.put("location", (String) user
											.getLocation().getProperty("name"));
								}
								if (user.getProperty("gender") != null) {
									userProfile.put("gender",
											(String) user.getProperty("gender"));
								}
								if (user.getBirthday() != null) {
									userProfile.put("birthday",
											user.getBirthday());
								}
								if (user.getProperty("relationship_status") != null) {
									userProfile
											.put("relationship_status",
													(String) user
															.getProperty("relationship_status"));
								}

								// Save the user profile info in a user property
								ParseUser currentUser = ParseUser
										.getCurrentUser();
								currentUser.put("profile", userProfile);
								currentUser.saveInBackground();

								// Show the user info
								updateViewsWithProfileInfo();
							} catch (JSONException e) {
								Log.d("facebook",
										"Error parsing returned user data.");
							}

						} else if (response.getError() != null) {
							if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
									|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
								Log.d("facebook",
										"The facebook session was invalidated.");
								onLogoutButtonClicked();
							} else {
								Log.d("facebook",
										"Some other error: "
												+ response.getError()
														.getErrorMessage());
							}
						}
					}
				});*/}
		request.executeAsync();

	}

	private void updateViewsWithProfileInfo() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser.get("profile") != null) {
			JSONObject userProfile = currentUser.getJSONObject("profile");
			try {
				if (userProfile.getString("facebookId") != null) {
					String facebookId = userProfile.get("facebookId")
							.toString();
					userProfilePictureView.setProfileId(facebookId);
				} else {
					// Show the default, blank user profile picture
					userProfilePictureView.setProfileId(null);
				}
				if (userProfile.getString("name") != null) {
					userNameView.setText(userProfile.getString("name"));
				} else {
					userNameView.setText("");
				}
				if (userProfile.getString("location") != null) {
					userLocationView.setText(userProfile.getString("location"));
				} else {
					userLocationView.setText("");
				}
				if (userProfile.getString("gender") != null) {
					userGenderView.setText(userProfile.getString("gender"));
				} else {
					userGenderView.setText("");
				}
				if (userProfile.getString("birthday") != null) {
					userDateOfBirthView.setText(userProfile
							.getString("birthday"));
				} else {
					userDateOfBirthView.setText("");
				}
				if (userProfile.getString("relationship_status") != null) {
					userRelationshipView.setText(userProfile
							.getString("relationship_status"));
				} else {
					userRelationshipView.setText("");
				}
			} catch (JSONException e) {
				Log.d("facebook",
						"Error parsing saved user data.");
			}

		}
	}

	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();

		// Go to the login view
		startLoginActivity();
	}

	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}

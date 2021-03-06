/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cs440.capstone;



import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.widget.UserSettingsFragment;

public class LoginUsingLoginFragmentActivity extends FragmentActivity {
    private UserSettingsFragment userSettingsFragment;
    public static Request request;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_fragment_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
        
        userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
            	String fqlQuery = "SELECT name, venue, start_time,end_time, eid FROM event WHERE eid IN (SELECT eid FROM event_member WHERE (uid IN (SELECT uid2 FROM friend WHERE uid1 = me())  OR uid = me())limit 10000) AND  end_time>now() AND venue.latitude > \"47.257379\" AND venue.latitude < \"47.265393\" AND venue.longitude < \"-122.477989\" AND venue.longitude  >\"-122.485628\"";
            	Bundle params = new Bundle();
            	params.putString("q", fqlQuery);

            	Session session1 = Session.getActiveSession();
           
            	Request request = new Request(session1, 
            	    "/fql", 
            	    params, 
            	    HttpMethod.GET, 
            	    new Request.Callback(){ 
            	        public void onCompleted(Response response) {
            	        Log.i("fql", "Got results: " + response.toString());
            	    }
            	});
            	Request.executeBatchAsync(request);
            	

            }
            
        });
       this.request=request;
                       
    }

    

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        userSettingsFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private Session createSession() {
        Session activeSession = Session.getActiveSession();
        if (activeSession == null || activeSession.getState().isClosed()) {
            activeSession = new Session.Builder(this).setApplicationId("431689033600302").build();
            Session.setActiveSession(activeSession);
            activeSession.requestNewReadPermissions(new NewPermissionsRequest( userSettingsFragment, Arrays.asList("user_photos, user_events, user_friends, user_location, user_activities, friends_events")));
        }
        if(activeSession.isOpened())
        {
        	Log.i("fql", "should be open");
        }
        return activeSession;
    }

}

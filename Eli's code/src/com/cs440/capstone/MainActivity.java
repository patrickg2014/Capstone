package com.cs440.capstone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
return true;
}

public void doSearch(View view){
//do something when the search button is pressed
Intent intent = new Intent(this, DoSearch.class);
startActivity(intent);
}

public void viewCampus(View view){
Intent intent = new Intent(this,MapActivity.class);
startActivity(intent);
}

public void takeTour(View view){
Intent intent = new Intent(this,CameraActivity.class);
startActivity(intent);
}

}

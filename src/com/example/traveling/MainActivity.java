package com.example.traveling;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import 	android.support.v4.app.FragmentActivity;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import android.util.Log;

public class MainActivity extends Activity {
	String TAG;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//checkGooglePlayServices();
		setContentView(R.layout.activity_main);
		
	}
	
	private void checkGooglePlayServices(){
	    int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    switch (result) {
	        case ConnectionResult.SUCCESS:
	            Log.d(TAG, "SUCCESS");
	            break;

	        case ConnectionResult.SERVICE_INVALID:
	            Log.d(TAG, "SERVICE_INVALID");
	            GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_INVALID, this, 0).show();
	            break;

	        case ConnectionResult.SERVICE_MISSING:
	            Log.d(TAG, "SERVICE_MISSING");
	            GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_MISSING, this, 0).show();
	            break;

	        case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
	            Log.d(TAG, "SERVICE_VERSION_UPDATE_REQUIRED");
	            GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, this, 0).show();
	            break;

	        case ConnectionResult.SERVICE_DISABLED:
	            Log.d(TAG, "SERVICE_DISABLED");
	            GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_DISABLED, this, 0).show();
	            break;
	    }
	}
}

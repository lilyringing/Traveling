package com.example.traveling;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.FragmentTransaction;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.SupportMapFragment;
import android.util.Log;

public class MainActivity extends FragmentActivity {
	String TAG;
	View map;
	private DrawerLayout MenuList;
	private ListView MLDrawer;
	private ActionBarDrawerToggle drawerToggle;
	private CharSequence DrawerTitle;
    private CharSequence Title;
    private int view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		map = getLayoutInflater().inflate(R.layout.drawer, null);
		setContentView(map);
		view = 0;
		initActionBar();
		initDrawer();
		initDrawerList();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_search).getActionView();
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    //home
	    if (drawerToggle.onOptionsItemSelected(item)) {
	        return true;
	    }

	    return super.onOptionsItemSelected(item);
	}
	
	private void initActionBar(){
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}
	
	private void initDrawer(){
		MenuList = (DrawerLayout) findViewById(R.id.drawer_layout);
        MLDrawer = (ListView) findViewById(R.id.left_drawer);
        
        //MenuList.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        Title = DrawerTitle = getTitle();
        drawerToggle = new ActionBarDrawerToggle(
        		this, 
                MenuList,
                R.drawable.ic_drawer, 
                R.string.drawer_open,
                R.string.drawer_close) {
        		
        	@Override
    		public void onDrawerClosed(View view) {
    			super.onDrawerClosed(view);
    			getActionBar().setTitle(Title);
    		}

    		@Override
    		public void onDrawerOpened(View drawerView) {
    			super.onDrawerOpened(drawerView);
    			getActionBar().setTitle(DrawerTitle);
    		}
        };
        
        drawerToggle.syncState();
        MenuList.setDrawerListener(drawerToggle);
	}
	
	private void initDrawerList(){
		String[] drawer_menu = this.getResources().getStringArray(R.array.drawer_menu);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawer_menu);
        MLDrawer.setAdapter(adapter);
        
        //側選單點選偵聽器
        MLDrawer.setOnItemClickListener(new DrawerItemClickListener());
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}
	
	//Change main content view when click items in the drawer 
	private void selectItem(int position){
		String[] drawer_menu = this.getResources().getStringArray(R.array.drawer_menu);
		
		if(view != position){
			switch(position){
				case 0:	// Home
					setContentView(map);
					view = 0;
					break;
				case 1:	// Profile
					setContentView(R.layout.fragment_profile);
					view = 1;
					break;
				case 2:	// Favorite
					setContentView(R.layout.fragment_favorite);
					view = 2;
					break;
				default:
					return;
			}	
			
			initDrawer();
			initDrawerList();
			// Highlight the selected item, update the title, and close the drawer
			MLDrawer.setItemChecked(position, true);
			setTitle(drawer_menu[position]);
			MenuList.closeDrawer(MLDrawer);
		}else{
			return;
		}
	}
	
	public void showRestaurant(View view){
		
	}
	
	/*private void checkGooglePlayServices(){
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
	}*/
}

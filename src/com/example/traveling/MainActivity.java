package com.example.traveling;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ExpandableListView;
import android.app.FragmentTransaction;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SearchView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import android.util.Log;
import org.json.*;


public class MainActivity extends FragmentActivity implements MapDialog.DialogFragmentListener {
	String TAG;
	View map;
	private DrawerLayout MenuList;
	private ListView MLDrawer;
	private ActionBarDrawerToggle drawerToggle;
	private CharSequence DrawerTitle;
    private CharSequence Title;
    private int view;
	private GoogleMap gmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set map view
		map = getLayoutInflater().inflate(R.layout.drawer, null);
		setContentView(map);	
		view = 0;
		
		if(gmap == null){
			gmap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		
		initMap();
		initActionBar();
		initDrawer();
		initDrawerList();
		
		// Create another thread for network access
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());  
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build()); 
	    
	   
	}
	
	/*@Override
	public void onDestroy(){
		//try{
			super.onDestroy();
			Fragment f = getFragmentManager().findFragmentById(R.id.map);
		    if (f != null) 
		        getFragmentManager().beginTransaction().remove(f).commit();
			
		//}catch(NullPointerException e){
		//	Log.d("onDestroy", "NullPointerException:" + e);
		//}
	}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    //home
	    if (drawerToggle.onOptionsItemSelected(item)) {
	        return true;
	    }

	    return super.onOptionsItemSelected(item);
	}
	
	private void initMap(){
		gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);	// Normal Map
		
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(25.033611, 121.565000), 13); // Taipei 101
		gmap.animateCamera(update);
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
        
        //�湧�桅��詨�賢
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
					//ExpandableListView elv = (ExpandableListView) findViewById(R.id.profile_list);
					//ExpandableAdapter viewAdapter = ExpandableListView.getAdapter();
					//elv.setAdapter();
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
	
	public void showMyDiary(View view){
		TableLayout my_diary = (TableLayout)findViewById(R.id.my_diary);
        my_diary.setStretchAllColumns(true);
        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        try {
        	System.out.println("Send query");
            String result = DBconnector.executeQuery("SELECT * FROM user");
            
            /* When SQL results contain many data using JSONArray
               If only one data use JSONObject
               JSONObject jsonData = new JSONObject(result);*/
            System.out.println(result);
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                TableRow tr = new TableRow(MainActivity.this);
                tr.setLayoutParams(row_layout);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                
                TextView user_acc = new TextView(MainActivity.this);
                user_acc.setText(jsonData.getString("account"));
                user_acc.setLayoutParams(view_layout);
                
                TextView user_pwd = new TextView(MainActivity.this);
                user_pwd.setText(jsonData.getString("pwd"));
                user_pwd.setLayoutParams(view_layout);
                
                tr.addView(user_acc);
                tr.addView(user_pwd);
                my_diary.addView(tr);
            }
        } catch(Exception e) {
            // Log.e("log_tag", e.toString());
        }
	}
	
	public void showRestaurant(View view){
		DialogFragment RestaurantDialog = MapDialog.newInstance(R.string.restaurant, R.array.restaurant_menu);
		RestaurantDialog.show(getSupportFragmentManager(),"Restaurant");
	}
	
	public void showSpot(View view){
		DialogFragment SpotDialog = MapDialog.newInstance(R.string.spot, R.array.spot_menu);
		SpotDialog.show(getSupportFragmentManager(),"Spot");
		
	}
	
	@Override
	public void MarkOnMap(int title, int which){
		//remove all markers, polylines
		gmap.clear();
		
		//connect to DB
		String result = DBconnector.executeQuery("SELECT * FROM user");
		
        try{
        	JSONArray jsonArray = new JSONArray(result);
        	for(int i = 0; i < jsonArray.length(); i++){
        		JSONObject jsonData = jsonArray.getJSONObject(i);
            	String name = jsonData.getString("name");
            	int latitude = Integer.parseInt(jsonData.getString("latitude"));
            	int longtitude = Integer.parseInt(jsonData.getString("longtitude"));
            	
            	// Mark on the map
            	MarkerOptions mark = AddMarker(name, latitude, longtitude);
            	gmap.addMarker(mark);
        	}
        }catch(JSONException e){
        	Log.e("log_tag", e.toString());
        }
        	
	}
	
	public MarkerOptions AddMarker(String name, int latitude, int longtitude){
		MarkerOptions marker = new MarkerOptions();
		marker.position(new LatLng(latitude, longtitude));
		marker.title(name);
		marker.draggable(true);
		return marker;
	}
}

package com.example.traveling;

import java.util.List;
import java.util.ArrayList;

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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import android.content.res.Configuration;
import android.util.Log;
import org.json.*;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;


public class MainActivity extends FragmentActivity {
	String TAG;
	View map;
	private DrawerLayout dLayout;
	private ListView dListView;
	private ActionBarDrawerToggle drawerToggle;
	private CharSequence DrawerTitle;
    private CharSequence Title;
    CustomDrawerAdapter adapter;
    List<DrawerItem> datalist;
    
    private int view;
	private GoogleMap gmap;
	private MapFragment mapFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.drawer);
		
		// Initializing
        datalist = new ArrayList<DrawerItem>();
        Title = DrawerTitle = getTitle();
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dListView = (ListView) findViewById(R.id.left_drawer);

        dLayout.setDrawerShadow(R.drawable.drawer_shadow,GravityCompat.START);
        datalist.add(new DrawerItem("Home", R.drawable.ic_action_search));
        datalist.add(new DrawerItem("Profile", R.drawable.ic_action_search));
        
        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                datalist);

        dListView.setAdapter(adapter);
        dListView.setOnItemClickListener(new DrawerItemClickListener());
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
         
        drawerToggle = new ActionBarDrawerToggle(this, dLayout,
                    R.drawable.ic_drawer, R.string.drawer_open,
                    R.string.drawer_close) {
              public void onDrawerClosed(View view) {
                    getActionBar().setTitle(Title);
                    invalidateOptionsMenu(); // creates call to
                                             // onPrepareOptionsMenu()
              }
         
              public void onDrawerOpened(View drawerView) {
                    getActionBar().setTitle(DrawerTitle);
                    invalidateOptionsMenu(); // creates call to
                                             // onPrepareOptionsMenu()
              }
        };
         
        dLayout.setDrawerListener(drawerToggle);
         
        if (savedInstanceState == null) {
              SelectItem(0);
        }
      
		// Create another thread for network access
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());  
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build()); 
	    
	   
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public void SelectItem(int possition) {
		 
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (possition) {
        case 0:
        	fragment = new MapFragment();
            break;
        case 1:
        	fragment = new ProfileFragment();
            break;
        default:
            break;
        }
        
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                  .commit();


        dListView.setItemChecked(possition, true);
        setTitle(datalist.get(possition).getItemName());
        dLayout.closeDrawer(dListView);
        
    }
     
	
	@Override
	public void setTitle(CharSequence title) {
	      Title = title;
	      getActionBar().setTitle(Title);
	}
	 
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	      super.onPostCreate(savedInstanceState);
	      // Sync the toggle state after onRestoreInstanceState has occurred.
	      drawerToggle.syncState();
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	      // The action bar home/up action should open or close the drawer.
	      // ActionBarDrawerToggle will take care of this.
	      if (drawerToggle.onOptionsItemSelected(item)) {
	            return true;
	      }
	 
	      return false;
	}
	 
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	      super.onConfigurationChanged(newConfig);
	      // Pass any configuration change to the drawer toggles
	      drawerToggle.onConfigurationChanged(newConfig);
	}
	
	private class DrawerItemClickListener implements
    	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			SelectItem(position);
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
}

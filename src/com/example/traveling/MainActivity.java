package com.example.traveling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SearchView;

import com.facebook.Request;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.util.Log;
import org.json.*;


public class MainActivity extends FragmentActivity implements MapDialog.DialogFragmentListener, FBFragment.SetUserData{
	
	private DrawerLayout MenuList;
	private ListView MLDrawer;
	private ActionBarDrawerToggle drawerToggle;
	private CharSequence DrawerTitle;
    private CharSequence Title;
    private int view;							// record current view's "drawer list item id"
	private View map;							// Store the view of map page (because it can't be inflate twice)
    private GoogleMap gmap;						// A object which is used to manipulate google map
	private FBFragment fbfragment;
	private ViewPager FavoritePager;
	private PageAdapter pageadapter;
	HashMap<String, HashMap> extraMarkerInfo;	// A data structure which is used to store detail information of markers.
	String userid;
	String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set FB login
		if(savedInstanceState == null){			
			fbfragment = new FBFragment();
			if(userid == null){
				getSupportFragmentManager().beginTransaction()
				.add(android.R.id.content, fbfragment)
				.commit();
			}else{
				initialize();
			}
				
		}else{	// Or set the fragment from restored state info
			
			fbfragment = (FBFragment)getSupportFragmentManager().
						  findFragmentById(android.R.id.content);
		}
		
		// Create another thread for network access
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());  
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build()); 
	}
	
	public void init(View v){
		initialize();
	}
	
	/* Initialization of Traveling including map, action bar and navigation drawer */
	public void initialize(){
		// Set map view
		if(map == null){
			map = getLayoutInflater().inflate(R.layout.drawer, null);
		}
		setContentView(map);	
		view = 0;
		
		if(gmap == null){
			gmap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		
		// initialize map, action bar and navigation drawer
		initMap();
		initActionBar();
		initDrawer();
		initDrawerList();
	}
	
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
		gmap.setInfoWindowAdapter(new InfoWindowAdapter(){
			@Override
		    public View getInfoWindow(Marker arg0) {
		        return null;
		    }
			
			/* Set the content of InfoWindow which shows when user click the marker(地點標記) */
			@Override
		    public View getInfoContents(Marker m) {
				View mapIW = getLayoutInflater().inflate(R.layout.infowindow, null);
				TextView tvTitle = ((TextView) mapIW.findViewById(R.id.title));
	            tvTitle.setText(m.getTitle());
	            TextView tvSnippet = ((TextView) mapIW.findViewById(R.id.snippet));
	            tvSnippet.setText(m.getSnippet());
		        return mapIW;
			}
		});
		
		/* Call dialog to show detail info of the marker when user click InfoWindow */
		gmap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
			@Override
		    public void onInfoWindowClick(Marker m) {
				HashMap<String, String> marker_data = extraMarkerInfo.get(m.getId());
				
				DialogFragment dialog = InfoWindowDialog.newInstance(marker_data);
				dialog.show(getSupportFragmentManager(),"test");
			}
		});
		
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(25.033611, 121.565000), 13); // Taipei 101
		gmap.animateCamera(update);
	}
	
	private void initActionBar(){
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}
	
	/* Initialize the layout of navigation drawer */
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
	
	/* Initialize the list of navigation drawer */
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
	
	public void initProfile(){
		setContentView(R.layout.fragment_profile);
		/*個人資料*/
        /*傳FB_id，存取資料庫 USER的Name、Gender、BirthDay、Age */
		TextView name = (TextView)findViewById(R.id.UserName);
        TextView gender = (TextView)findViewById(R.id.Gender);
        TextView birthday = (TextView)findViewById(R.id.Birthday);
        TextView age = (TextView)findViewById(R.id.Age);
        
		try {
            String result = DBconnector.executeQuery("SELECT * FROM user WHERE fb_id=" + userid);
            
            /* When SQL results contain many data using JSONArray
               If only one data use JSONObject
               JSONObject jsonData = new JSONObject(result);*/ 
            JSONArray jsonArray = new JSONArray(result);
            
        	for(int i = 0; i < jsonArray.length(); i++){
        		JSONObject jsonData = jsonArray.getJSONObject(i);
        		
        		name.setText(jsonData.getString("user_name"));
        		gender.setText(jsonData.getString("gender"));
        		birthday.setText(jsonData.getString("birthday"));
        		age.setText(jsonData.getString("age"));
        	}
        } catch(Exception e) {
             Log.e("log_tag", e.toString());
        }
        
        /*建listview*/
        ExpandableListView elv = (ExpandableListView)findViewById(R.id.elview);
        
        /*第一層列表*/
        List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
        Map<String, String> group1 = new HashMap<String, String>();
        group1.put("group", "地點收藏");
        Map<String, String> group2 = new HashMap<String, String>();
        group2.put("group", "路線收藏");
        Map<String, String> group3 = new HashMap<String, String>();
        group3.put("group", "我的路線");
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        
        /*第二層列表_收藏地點*/
        List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
        try {
            String result = DBconnector.executeQuery("SELECT * FROM collect_s WHERE fb_id=" + userid);
            Log.e("log_tag", result);
            /* When SQL results contain many data using JSONArray
               If only one data use JSONObject
               JSONObject jsonData = new JSONObject(result);*/
            JSONArray jsonArray = new JSONArray(result);
            int array_size = jsonArray.length();
            int show_size = Math.min(array_size, 3);
            
        	for(int i = 0; i < show_size; i++){
        		JSONObject jsonData = jsonArray.getJSONObject(i);
            	String site_name = jsonData.getString("site_id");
            	
            	Map<String, String> child1Data = new HashMap<String, String>();
                child1Data.put("child", site_name);
                child1.add(child1Data);
        	}
        	
            Map<String, String> child1More = new HashMap<String, String>();
            child1More.put("child", "more");
            child1.add(child1More);
        } catch(Exception e) {
             Log.e("log_tag", e.toString());
             Map<String, String> child1Data = new HashMap<String, String>();
             child1Data.put("child", "您沒有收藏任何地點!");
             child1.add(child1Data);
        }
        
        /*第二層列表_收藏路線*/
        List<Map<String, String>> child2 = new ArrayList<Map<String, String>>();
        try {
            String result = DBconnector.executeQuery("SELECT travel_id FROM collect_t WHERE fb_id=" + userid);
            
            /* When SQL results contain many data using JSONArray
               If only one data use JSONObject
               JSONObject jsonData = new JSONObject(result);*/
            JSONArray jsonArray = new JSONArray(result);
            int array_size = jsonArray.length();
            int show_size = Math.min(array_size, 3);
            
        	for(int i = 0; i < show_size; i++){
        		JSONObject jsonData = jsonArray.getJSONObject(i);
            	String route_name = jsonData.getString("travel_id");
            	
            	Map<String, String> child2Data = new HashMap<String, String>();
                child2Data.put("child", route_name);
                child2.add(child2Data);
        	}
        	
        	Map<String, String> child2More = new HashMap<String, String>();
            child2More.put("child", "more");
            child2.add(child2More);
        } catch(Exception e) {
             Log.e("log_tag", e.toString());
             Map<String, String> child2Data = new HashMap<String, String>();
             child2Data.put("child", "您沒有收藏任何路線!");
             child2.add(child2Data);
        }       
        
        /*第二層列表_我的路線*/
        List<Map<String, String>> child3 = new ArrayList<Map<String, String>>();
        try {
            String result = DBconnector.executeQuery("SELECT travel_id FROM ownership WHERE owner=" + userid);
            
            /* When SQL results contain many data using JSONArray
               If only one data use JSONObject
               JSONObject jsonData = new JSONObject(result);*/
            JSONArray jsonArray = new JSONArray(result);
            int array_size = jsonArray.length();
            int show_size = Math.min(array_size, 3);
           
        	for(int i = 0; i < show_size; i++){
        		JSONObject jsonData = jsonArray.getJSONObject(i);
            	String route_name = jsonData.getString("travel_id");
            	
            	Map<String, String> child3Data = new HashMap<String, String>();
                child3Data.put("child", route_name);
                child3.add(child3Data);
        
        	}
        	
        	Map<String, String> child3More = new HashMap<String, String>();
            child3More.put("child", "more");
            child3.add(child3More);
        } catch(Exception e) {
             Log.e("log_tag", e.toString());
             Map<String, String> child3More = new HashMap<String, String>();
             child3More.put("child", "您還沒有建立任何路線");
             child3.add(child3More);
        }
        
        //用一個list物件保存所有的二級清單資料
        List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();
        childs.add(child1);
        childs.add(child2);
        childs.add(child3);
        
        final ExpandableAdapter viewAdapter = new ExpandableAdapter(this, groups, childs);
        elv.setAdapter(viewAdapter);
        
        /*子項按鈕*/
        elv.setOnChildClickListener(new OnChildClickListener()  
        {  
        	
        	@Override  
            public boolean onChildClick(ExpandableListView parent, View v,  
                    int groupPosition, int childPosition, long id)  
            {
        	
        		
        		String t = viewAdapter.getChild(groupPosition,childPosition).toString();
        		String title = t.substring(7,t.length()-1);
        		
        		//Toast.makeText(getBaseContext(), groupPosition,Toast.LENGTH_LONG ).show();
        		
        		if(title.equals("more")){
        			switch(groupPosition){
        				case 0:
        					selectItem(2);
        					break;
        				case 1:
        					selectItem(2);
        					break;
        				case 2:
        					break;
        				default:
        					break;
        			}
        		}
        		
				return false; 
            }  
        }); 
	}
	
	public void initFavorite(int position){
		setContentView(R.layout.fragment_favorite);
		pageadapter = new PageAdapter(this, userid);
        FavoritePager = (ViewPager) findViewById(R.id.favoritepager);
        FavoritePager.setAdapter(pageadapter);
        
        FavoritePager.setCurrentItem(position);
        FavoritePager.setOnPageChangeListener(new OnPageChangeListener() {

            public void onPageScrollStateChanged(int arg0) {
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageSelected(int currentPage) {
                //currentPage is the position that is currently displayed.
            	         	
            }

        });
	}
	
	public void initHelp(){
		setContentView(R.layout.fragment_help);
		
		/*建listview*/
        /*ExpandableListView elv = (ExpandableListView)findViewById(R.id.ELview);
		List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
        Map<String, String> group1 = new HashMap<String, String>();
        group1.put("group", "登入問題");
        Map<String, String> group2 = new HashMap<String, String>();
        group2.put("group", "地圖問題");
        groups.add(group1);
        groups.add(group2);
        
        List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
        Map<String, String> child1Data1 = new HashMap<String, String>();
        child1Data1.put("child", "FB");
        Map<String, String> child1Data2 = new HashMap<String, String>();
        child1Data2.put("child", "Can save your favorite");
        child1.add(child1Data1);
        child1.add(child1Data2);
        
        List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();
        childs.add(child1);*/
	}
	
	/* Change main content view when click items in the drawer */ 
	private void selectItem(int position){
		String[] drawer_menu = this.getResources().getStringArray(R.array.drawer_menu);
		
		if(view != position){
			switch(position){
				case 0:	// Home
					setContentView(map);
					view = 0;
					break;
				case 1:	// Profile
					initProfile(); 
					view = 1;
					break;
				case 2:	// Favorite
					initFavorite(0);
					view = 2;
					break;
				case 5: // Help
					initHelp();
					view = 5;
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
	
	/*public void showMyDiary(View view){
		TableLayout my_diary = (TableLayout)findViewById(R.id.my_diary);
        my_diary.setStretchAllColumns(true);
        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        try {
        	System.out.println("Send query");
            String result = DBconnector.executeQuery("SELECT * FROM user");
            
            /* When SQL results contain many data using JSONArray
               If only one data use JSONObject
               JSONObject jsonData = new JSONObject(result);*   /
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
	}*/
	
	public void showRestaurant(View view){
		DialogFragment RestaurantDialog = MapDialog.newInstance(R.string.restaurant, R.array.restaurant_menu);
		RestaurantDialog.show(getSupportFragmentManager(),"Restaurant");
	}
	
	public void showSpot(View view){
		DialogFragment SpotDialog = MapDialog.newInstance(R.string.spot, R.array.spot_menu);
		SpotDialog.show(getSupportFragmentManager(),"Spot");
		
	}
	
	public void showRoute(View view){
		PolylineOptions line = new PolylineOptions();
		line.add(new LatLng(25.016347, 121.533722), new LatLng(25.033611, 121.565000))
			.width(5)
			.color(Color.RED);
		
		gmap.addPolyline(line);
	}
	
	@Override
	public void MarkOnMap(int title, int which){
		String result;
		//remove all markers, polylines
		gmap.clear();
		extraMarkerInfo = new HashMap<String, HashMap>();
		
		
        try{
        	//connect to DB
    		if(title == R.string.spot){
    			result = DBconnector.executeQuery("SELECT * FROM site WHERE tag_s=" + which);
    		}else{
    			result = DBconnector.executeQuery("SELECT * FROM site WHERE tag_r=" + which);
    		}
    		
        	JSONArray jsonArray = new JSONArray(result);
        	for(int i = 0; i < jsonArray.length(); i++){
        		JSONObject jsonData = jsonArray.getJSONObject(i);
            	
            	HashMap<String, String> data = new HashMap<String, String>();
            	String name = jsonData.getString("site_name");
            	data.put("name", name);
            	String phone = jsonData.getString("phone");
            	data.put("phone", phone);
            	data.put("address", jsonData.getString("address"));
            	String lat = jsonData.getString("latitude");
            	String lnt = jsonData.getString("longitude");
            	data.put("score", jsonData.getString("score"));
            	data.put("content", jsonData.getString("content"));
            	//data.put("tagr", jsonData.getString("Tag_R"));
            	//data.put("tags", jsonData.getString("Tag_S"));
            	data.put("open", jsonData.getString("open"));
            	data.put("ticket", jsonData.getString("ticket"));
            	data.put("website", jsonData.getString("website"));
            	            	
            	// Mark on the map
            	MarkerOptions mark = CreateMarkerOpt(name, lat, lnt, phone);
            	Marker marker = gmap.addMarker(mark);
            	extraMarkerInfo.put(marker.getId(),data);
        	}
        }catch(JSONException e){
        	Log.e("log_tag", e.toString());
        }
		
		// Example for testing
        /*HashMap<String, String> data = new HashMap<String, String>();
        data.put("name", "NTU");
    	data.put("phone", "2222-3333");
    	data.put("address", "羅斯福路");
    	data.put("score", "5");
    	data.put("content", "國立台灣大學");
    	data.put("website", "www.ntu.edu.tw");
    	MarkerOptions m = CreateMarkerOpt("NTU", "25.016347", "121.533722", "2222-3333");
        Marker marker = gmap.addMarker(m);
        extraMarkerInfo.put(marker.getId(),data);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(25.016347, 121.533722), 13); // Taipei 101
		gmap.animateCamera(update);*/
	}
	
	public MarkerOptions CreateMarkerOpt(String name, String latitude, String longtitude, String phone){
		MarkerOptions marker = new MarkerOptions();
		marker.position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude)));
		marker.title(name);
		marker.snippet(phone);
		return marker;
	}
	
	public void SetUser(String id, String name){
		userid = id;
		username = name;
		initialize();
	}
}
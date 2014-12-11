package com.example.traveling;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultActivity extends FragmentActivity{
	private ActionBar actionBar;
	private FragmentManager fragmentManager;
	private String userid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String s = intent.getStringExtra("search_string");
		userid = intent.getStringExtra("userid");
		
		search_result(s);
		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b47e13")));
		
		fragmentManager = getSupportFragmentManager();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		finish();
	    		break;
	    }
	    return true;
	}
	
	public void search_result(String s){		
		LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableLayout t = (TableLayout)inflater.inflate(R.layout.favorite_site, null);
		
		// 行程
		try{	
			String result = DBconnector.executeQuery("SELECT * FROM `ownership`, `user` WHERE ownership.travel_name LIKE '%" + s + "%' and ownership.owner=user.fb_id");	
			JSONArray jsonArray = new JSONArray(result);
			
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject jsonData = jsonArray.getJSONObject(i);
				
				final String id = jsonData.getString("travel_id");
				final String route_name = jsonData.getString("travel_name");
				final String user_name = jsonData.getString("user_name");
        		
            	TableRow tr = new TableRow(this);
            	TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            	tableRowParams.setMargins(50, 10, 0, 10);
            	tr.setLayoutParams(tableRowParams);
            	tr.setOnClickListener(new OnClickListener(){
            	    public void onClick(View v){
            	    	DialogFragment dialog = RouteInfoDialog.newInstance(id, user_name, route_name);
        				dialog.show(fragmentManager,"test");
        				
            	    }
            	});
            	
            	View v = inflater.inflate(R.layout.block_route, null);
            	TextView routeName = (TextView) v.findViewById(R.id.RouteName);
            	routeName.setText(route_name);
            	Button creator = (Button) v.findViewById(R.id.Router);
            	creator.setText(user_name);
            	
            	tr.addView(v);
            	t.addView(tr);
            	
			} // end for
		}catch(JSONException e){
			Log.e("log_tag", e.toString());
        	
			View v = inflater.inflate(R.layout.block_route, null);
			TextView site_name = (TextView) v.findViewById(R.id.RouteName);
			site_name.setText("沒有與"+ s + "相關的行程");
			
			t.addView(v);
		}
		
		//景點.餐廳
		try{	
			String result = DBconnector.executeQuery("SELECT * FROM `site` WHERE site_name LIKE '%" + s + "%'");	
			JSONArray jsonArray = new JSONArray(result);
        	
			for(int i = 0; i < jsonArray.length(); i++){
				HashMap<String, String> data = new HashMap<String, String>();
				JSONObject jsonData = jsonArray.getJSONObject(i);        		
        		
				String site_id = jsonData.getString("site_id");
				data.put("siteid", site_id);
				String name = jsonData.getString("site_name");
            	data.put("name", name);
            	String phone = jsonData.getString("phone");
            	data.put("phone", phone);
            	String tag_r = jsonData.getString("tag_r");
            	String tag_s = jsonData.getString("tag_s");
            	final String latitude = jsonData.getString("latitude");
            	data.put("latitude", latitude);
            	final String longitude = jsonData.getString("longitude");
            	data.put("longitude", longitude);
            	data.put("address", jsonData.getString("address"));
            	data.put("score", jsonData.getString("score"));
            	data.put("content", jsonData.getString("content"));
            	data.put("open", jsonData.getString("open"));
            	data.put("ticket", jsonData.getString("ticket"));
            	data.put("website", jsonData.getString("website"));
            	
            	try{
            		String comment_result = DBconnector.executeQuery("SELECT * FROM comment WHERE user_id=" + userid + " and site_id=" + site_id);
            		JSONArray jArray = new JSONArray(comment_result);
            		if(jArray.length() > 0){
              	      data.put("comment", "1");
            		}	
            	}catch(JSONException e){
            		data.put("comment", "0");
                }
            	
            	final HashMap<String, String> site_data = data;
        		
            	TableRow tr = new TableRow(this);
            	TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            	tableRowParams.setMargins(50, 10, 0, 10);
            	tr.setLayoutParams(tableRowParams);
            	tr.setOnClickListener(new OnClickListener(){
            	    public void onClick(View v){
            	    	DialogFragment dialog = InfoWindowDialog.newInstance(site_data, userid, fragmentManager);
        				dialog.show(fragmentManager,"test");
            	    }
            	});
            	
            	View v = inflater.inflate(R.layout.block, null);
            	TextView site_name = (TextView) v.findViewById(R.id.SiteName);
            	site_name.setText(name);
            	
            	TextView site_phone = (TextView)v.findViewById(R.id.SitePhone);
            	site_phone.setText("電話: "+ phone);
            	
            	Button site_label = (Button) v.findViewById(R.id.SiteLabel);
            	if(tag_r.equals("")){
            		site_label.setText(tag_s);
            		data.put("tag", tag_s);
            	}else{
            		site_label.setText(tag_r);
            		data.put("tag", tag_r);
            	}
            	
            	/*Button showmap = (Button) v.findViewById(R.id.SiteOnMap);
            	showmap.setOnClickListener(new OnClickListener(){
            	    public void onClick(View v){
            	    	Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
	        			intent.putExtra("latitude", latitude);
	        			intent.putExtra("longitude", longitude);
	        			intent.putExtra("data", site_data);
            	    	setResult(0, intent);
            	    	finish();
            	    }
            	});*/
            	
            	tr.addView(v);
            	t.addView(tr);
            	
			} // end for
		}catch(JSONException e){
			Log.e("log_tag", e.toString());
			
			View v = inflater.inflate(R.layout.block, null);
			TextView site_name = (TextView) v.findViewById(R.id.SiteName);
			site_name.setText("沒有與"+ s + "相關的景點或餐廳");
			setContentView(v);
		}
		
		setContentView(t);
	}
}
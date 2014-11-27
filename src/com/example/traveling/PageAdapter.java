package com.example.traveling;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PageAdapter extends PagerAdapter{
	private static int Page_number = 2;
	private String userid;
	private FragmentManager fragmentManager;
	private String[] spot_menu;
	private String[] restaurant_menu;
	HashMap<String, HashMap> extraMarkerInfo;	// A data structure which is used to store detail information of markers.
	Context context;
	
	public PageAdapter(Context context, String id, FragmentManager fm){
		this.context = context;
		this.fragmentManager = fm;
		userid = id;
		
		Resources res = context.getResources();
		spot_menu = res.getStringArray(R.array.spot_menu);
		restaurant_menu = res.getStringArray(R.array.restaurant_menu);
	}
	
	@Override
	public int getCount(){
		return Page_number;
	}
	
	 /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate()}.
     *
     * @param container The containing View in which the page will be shown.
     * @param position The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */
	@Override
	public Object instantiateItem(View container, int position) {
		LayoutInflater inflater = (LayoutInflater) container.getContext()
			    		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableLayout t;
		
		switch (position){
			case 0:
				t = (TableLayout)inflater.inflate(R.layout.favorite_site, null);
				
				try {
	                String result = DBconnector.executeQuery("SELECT * FROM `collect_s`, `site` WHERE collect_s.fb_id=" + userid + " and site.site_id=collect_s.site_id");
	                Log.e("DB", result);
	                /* When SQL results contain many data using JSONArray
	                   If only one data use JSONObject
	                   JSONObject jsonData = new JSONObject(result);*/
	                JSONArray jsonArray = new JSONArray(result);
	               
	            	for(int i = 0; i < jsonArray.length(); i++){
	            		HashMap<String, String> data = new HashMap<String, String>();
	            		
	            		JSONObject jsonData = jsonArray.getJSONObject(i);
	                	String name = jsonData.getString("site_name");
	                	data.put("name", name);
	                	String phone = jsonData.getString("phone");
	                	data.put("phone", phone);
	                	String tag_r = jsonData.getString("tag_r");
	                	String tag_s = jsonData.getString("tag_s");
	                	data.put("address", jsonData.getString("address"));
	                	data.put("score", jsonData.getString("score"));
	                	data.put("content", jsonData.getString("content"));
	                	data.put("open", jsonData.getString("open"));
	                	data.put("ticket", jsonData.getString("ticket"));
	                	data.put("website", jsonData.getString("website"));
	                	
	                	final HashMap<String, String> site_data = data;
	                	TableRow tr = new TableRow(context);
	                	TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
	                	tableRowParams.setMargins(50, 10, 0, 10);
	                	tr.setLayoutParams(tableRowParams);
	                	tr.setOnClickListener(new OnClickListener(){
	                	    public void onClick(View v){
	                	    	
	                	    	DialogFragment dialog = InfoWindowDialog.newInstance(site_data, userid);
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
	                	}else{
	                		site_label.setText(tag_r);
	                	}

	                	tr.addView(v);
	                	t.addView(tr);
	            	}
	            	
	            } catch(Exception e) {
	                 Log.e("log_tag", e.toString());
	                 	TableRow tr = new TableRow(context);
	                	TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
	                	tableRowParams.setMargins(50, 10, 0, 10);
	                	tr.setLayoutParams(tableRowParams);
	                	View v = inflater.inflate(R.layout.block, null);
	                	TextView site_name = (TextView) v.findViewById(R.id.SiteName);
	                	site_name.setText("您沒有收藏任何景點");
	                	TextView site_phone = (TextView)v.findViewById(R.id.SitePhone);
	                	site_phone.setText("");
	                	Button site_label = (Button) v.findViewById(R.id.SiteLabel);
	                	site_label.setVisibility(View.INVISIBLE);
	                	tr.addView(v);
	                	t.addView(tr);
	            }
				
				break;
			case 1:
				t = (TableLayout)inflater.inflate(R.layout.favorite_route, null);
				
				try {
					String result = DBconnector.executeQuery("SELECT * FROM `collect_t`, `travel` WHERE collect_t.fb_id=" + userid + " and travel.travel_id=collect_t.travel_id");
	                
	                /* When SQL results contain many data using JSONArray
	                   If only one data use JSONObject
	                   JSONObject jsonData = new JSONObject(result);*/
	                JSONArray jsonArray = new JSONArray(result);
	               
	            	for(int i = 0; i < jsonArray.length(); i++){
	            		JSONObject jsonData = jsonArray.getJSONObject(i);
	                	String site_id = jsonData.getString("site_id");
	                	
	                	TableRow tr = new TableRow(context);
	                	TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
	                	tableRowParams.setMargins(50, 10, 0, 10);
	                	tr.setLayoutParams(tableRowParams);
	                	tr.setOnClickListener(new OnClickListener(){
	                	    public void onClick(View v){
	                	    	Toast.makeText(context, "Click",Toast.LENGTH_LONG ).show();
	                	    	//HashMap<String, String> site_data;
	                     	    //DialogFragment dialog = InfoWindowDialog.newInstance(site_data);
	            				//dialog.show(fragmentManager,"test");
	                	    }
	                	});
	                	
	                	View v = inflater.inflate(R.layout.block, null);
	                	TextView site_name = (TextView) v.findViewById(R.id.SiteName);
	                	site_name.setText(site_id);
	                	TextView site_phone = (TextView)v.findViewById(R.id.SitePhone);
	                	Button site_label = (Button) v.findViewById(R.id.SiteLabel);
	                	//site_label.setText(getLable(tag_r, tag_s));
	                	tr.addView(v);
	                	t.addView(tr);
	            	}
	            	
	            } catch(Exception e) {
	                 Log.e("log_tag", e.toString());
	                 TableRow tr = new TableRow(context);
	                	TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
	                	tableRowParams.setMargins(50, 10, 0, 10);
	                	tr.setLayoutParams(tableRowParams);
	                 View v = inflater.inflate(R.layout.block, null);
	                	TextView site_name = (TextView) v.findViewById(R.id.SiteName);
	                	site_name.setText("您沒有收藏任何路線");
	                	TextView site_phone = (TextView)v.findViewById(R.id.SitePhone);
	                	site_phone.setText("");
	                	Button site_label = (Button) v.findViewById(R.id.SiteLabel);
	                	site_label.setVisibility(View.INVISIBLE);
	                	tr.addView(v);
	                	t.addView(tr);
	            }
				
				break;
			default:
				t = (TableLayout)inflater.inflate(R.layout.favorite_site, null);
				break;
		}
		
		((ViewPager) container).addView(t, 0);
  	   
		return t;
	}
	
	private CharSequence getLable(String tag_r, String tag_s) {
		if(tag_r.equals(null)){
			return spot_menu[Integer.parseInt(tag_s)];
    	}else{
    		return restaurant_menu[Integer.parseInt(tag_r)];
    		
    	}
	}

	/**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate()}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position The page position to be removed.
     * @param object The same object that was returned by
     * {@link #instantiateItem(View, int)}.
     */
	@Override
	public void destroyItem(View container, int position, Object view) {
		((ViewPager) container).removeView((View) view);
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
	   return view==((View)object);
	}
	
	/**
     * Called when the a change in the shown pages has been completed.  At this
     * point you must ensure that all of the pages have actually been added or
     * removed from the container as appropriate.
     * @param container The containing View which is displaying this adapter's
     * page views.
     */
	@Override
	public void finishUpdate(View arg0) {}
 

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0){
    
   	}
}
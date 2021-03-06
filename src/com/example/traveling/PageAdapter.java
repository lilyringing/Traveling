package com.example.traveling;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PageAdapter extends PagerAdapter{
	private DownloadWebPicture loadPic;
	private Handler mHandler;
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
	                
	                /* When SQL results contain many data using JSONArray
	                   If only one data use JSONObject
	                   JSONObject jsonData = new JSONObject(result);*/
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
	                	TableRow tr = new TableRow(context);
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
	                	final ImageView site_photo = (ImageView) v.findViewById(R.id.SitePhoto);
	                	loadPic = new DownloadWebPicture();
	                	mHandler = new Handler(){
	                        @Override
	                        public void handleMessage(Message msg) {
	                            switch(msg.what){
	                                case 1:
	                                    site_photo.setImageBitmap(loadPic.getImg());
	                                    break;
	                            }
	                            super.handleMessage(msg);
	                        }
	                    };
	                    //url should be picture's url from database
	                    //loadPic.handleWebPic(url, mHandler);
	                	
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
					String result = DBconnector.executeQuery("SELECT * FROM `collect_t`, `ownership`, `user` WHERE collect_t.fb_id=" + userid + " and ownership.travel_id=collect_t.travel_id"
							+ " and ownership.owner=user.fb_id");
	                
	                /* When SQL results contain many data using JSONArray
	                   If only one data use JSONObject
	                   JSONObject jsonData = new JSONObject(result);*/
	                JSONArray jsonArray = new JSONArray(result);
	               
	            	for(int i = 0; i < jsonArray.length(); i++){
	            		JSONObject jsonData = jsonArray.getJSONObject(i);
	                	final String travelid = jsonData.getString("travel_id");
	            		final String travel_name = jsonData.getString("travel_name");
	            		final String creator = jsonData.getString("user_name");
	                	
	                	TableRow tr = new TableRow(context);
	                	TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
	                	tableRowParams.setMargins(150, 20, 0, 20);
	                	tr.setLayoutParams(tableRowParams);
	                	tr.setOnClickListener(new OnClickListener(){
	                	    public void onClick(View v){
	                	    	DialogFragment dialog = RouteInfoDialog.newInstance(userid, travelid, creator, travel_name);
	            				dialog.show(fragmentManager,"test");
	                	    }
	                	});
	                	
	                	View v = inflater.inflate(R.layout.block_route, null);
	                	
	                	TextView route_name = (TextView) v.findViewById(R.id.RouteName);
	                	route_name.setText(travel_name);
	                	
	                	Button route_creator = (Button) v.findViewById(R.id.Router);
	                	route_creator.setText(creator);
	                	
	                	tr.addView(v);
	                	t.addView(tr);
	            	}
	            	
	            } catch(Exception e) {
	                 Log.e("log_tag", e.toString());
	                 TableRow tr = new TableRow(context);
	                	TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
	                	tableRowParams.setMargins(150, 20, 0, 20);
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
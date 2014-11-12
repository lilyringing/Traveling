package com.example.traveling;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PageAdapter extends PagerAdapter{
	private static int Page_number = 2;
	private String userid;
	Context context;
	
	public PageAdapter(Context context, String id){
		this.context = context;
		userid = id;
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
	                String result = DBconnector.executeQuery("SELECT * FROM collect_s WHERE fb_id=" + userid);
	                
	                /* When SQL results contain many data using JSONArray
	                   If only one data use JSONObject
	                   JSONObject jsonData = new JSONObject(result);*/
	                JSONArray jsonArray = new JSONArray(result);
	               
	            	for(int i = 0; i < jsonArray.length(); i++){
	            		JSONObject jsonData = jsonArray.getJSONObject(i);
	                	String site_id = jsonData.getString("site_id");
	                	
	                	TableRow tr = new TableRow(context);
	                	tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
	                	TextView tx = new TextView(context);
	                	tx.setText(site_id);
	                	tr.addView(tx);
	                	t.addView(tr);
	            	}
	            	
	            } catch(Exception e) {
	                 Log.e("log_tag", e.toString());
	            }
				
				break;
			case 1:
				t = (TableLayout)inflater.inflate(R.layout.favorite_route, null);
				
				try {
	                String result = DBconnector.executeQuery("SELECT * FROM collect_s WHERE fb_id=" + userid);
	                
	                /* When SQL results contain many data using JSONArray
	                   If only one data use JSONObject
	                   JSONObject jsonData = new JSONObject(result);*/
	                JSONArray jsonArray = new JSONArray(result);
	               
	            	for(int i = 0; i < jsonArray.length(); i++){
	            		JSONObject jsonData = jsonArray.getJSONObject(i);
	                	String site_id = jsonData.getString("travel_id");
	                	
	                	TableRow tr = new TableRow(context);
	                	tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
	                	TextView tx = new TextView(context);
	                	tx.setText(site_id);
	                	tr.addView(tx);
	                	t.addView(tr);
	            	}
	            	
	            } catch(Exception e) {
	                 Log.e("log_tag", e.toString());
	            }
				
				break;
			default:
				t = (TableLayout)inflater.inflate(R.layout.favorite_site, null);
				break;
		}
		
		((ViewPager) container).addView(t, 0);
  	   
		return t;
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
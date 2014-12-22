package com.example.traveling;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RouteInfoDialog extends DialogFragment{
	public static RouteInfoDialog newInstance(String userid, String travelid, String creator, String travel_name){
		RouteInfoDialog rd = new RouteInfoDialog();
		
		Bundle bun = new Bundle();
		bun.putString("userid", userid);
        bun.putString("travelid", travelid);
        bun.putString("creator", creator);
        bun.putString("name", travel_name);
        rd.setArguments(bun);
		return rd;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.route_info, null);
		ArrayList<String> morning_list = new ArrayList<String>();
		ArrayList<String> afternoon_list = new ArrayList<String>();
		ArrayList<String> night_list = new ArrayList<String>();
			
		final String travel_id = getArguments().getString("travelid");
		final String userid = getArguments().getString("userid");
		
		// 取得行程
		try{
			String result = DBconnector.executeQuery("SELECT * FROM `travel`, `site` WHERE travel.travel_id=" + travel_id + " and travel.site_id=site.site_id");
			JSONArray jsonArray = new JSONArray(result);
			
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject jsonData = jsonArray.getJSONObject(i);
				
				String time = jsonData.getString("time");
				String site_name = jsonData.getString("site_name");
				if(time.equals("早")){
					morning_list.add(site_name);
				}else if(time.equals("午")){
					afternoon_list.add(site_name);
				}else{
					night_list.add(site_name);
				}
			}
		}catch(JSONException e){
			Log.e("log_tag", e.toString());
		}
		
		//Setting for add route to favorite
		CheckBox favorite = ((CheckBox) v.findViewById(R.id.favorite));
        try{
        	String result = DBconnector.executeQuery("SELECT * FROM collect_t WHERE fb_id=" + userid + " and travel_id=" + travel_id);
        	  
        	JSONArray jsonArray = new JSONArray(result);
        	if(jsonArray.length() > 0){
        	      favorite.setChecked(true);
        	}
        }catch(JSONException e){
        }

        favorite.setOnCheckedChangeListener(new OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        		if(isChecked){
        			// insert into DB
        			DBconnector.executeQuery("INSERT INTO `collect_t`(`fb_id`, `travel_id`) VALUES (" + userid + ", " + travel_id + ")");
        			buttonView.setChecked(true);
        		}else{
        			// remove from DB
        			DBconnector.executeQuery("DELETE FROM `collect_t` WHERE `collect_t`.`fb_id` = "+ userid + " AND `collect_t`.`travel_id` = "+ travel_id);
        			buttonView.setChecked(false);
        		}
        	}
        });
        
		TextView tvName = ((TextView) v.findViewById(R.id.name));
        tvName.setText(getArguments().getString("name"));
        
        TextView tvCreator = ((TextView) v.findViewById(R.id.router));
        tvCreator.setText("路線作者: " + getArguments().getString("creator"));
        
        ListView Mlistview = (ListView)v.findViewById(R.id.MListView);
        ArrayAdapter<String> Madapter = new ArrayAdapter<String>(getActivity(), R.layout.route_list_item, morning_list);
        Mlistview.setAdapter(Madapter);
		
        ListView Alistview = (ListView)v.findViewById(R.id.AListView);
        ArrayAdapter<String> Aadapter = new ArrayAdapter<String>(getActivity(), R.layout.route_list_item, afternoon_list);
        Alistview.setAdapter(Aadapter);
        
        ListView Nlistview = (ListView)v.findViewById(R.id.NListView);
        ArrayAdapter<String> Nadapter = new ArrayAdapter<String>(getActivity(), R.layout.route_list_item, night_list);
        Nlistview.setAdapter(Nadapter);
        
		builder.setView(v);
		return builder.create();
	}
}
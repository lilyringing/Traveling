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
import android.widget.ListView;
import android.widget.TextView;

public class RouteInfoDialog extends DialogFragment{
	public static RouteInfoDialog newInstance(String travelid, String creator, String travel_name){
		RouteInfoDialog rd = new RouteInfoDialog();
		
		Bundle bun = new Bundle();
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
		
		
		String travel_id = getArguments().getString("travelid");
		try{
			String result = DBconnector.executeQuery("SELECT * FROM `travel`, `site` WHERE travel.travel_id=" + travel_id + " and travel.site_id=site.site_id");
			JSONArray jsonArray = new JSONArray(result);
			Log.e("db_tag", result);
			
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject jsonData = jsonArray.getJSONObject(i);
				
				String time = jsonData.getString("time");
				String site_name = jsonData.getString("site_name");
				if(time.equals("жн")){
					morning_list.add(site_name);
				}else if(time.equals("д╚")){
					afternoon_list.add(site_name);
				}else{
					night_list.add(site_name);
				}
			}
		}catch(JSONException e){
			Log.e("log_tag", e.toString());
		}
		
		TextView tvName = ((TextView) v.findViewById(R.id.name));
        tvName.setText(getArguments().getString("name"));
        
        ListView Mlistview = (ListView)v.findViewById(R.id.MListView);
        ArrayAdapter<String> Madapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, morning_list);
        Mlistview.setAdapter(Madapter);
		
        ListView Alistview = (ListView)v.findViewById(R.id.AListView);
        ArrayAdapter<String> Aadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, afternoon_list);
        Alistview.setAdapter(Aadapter);
        
        ListView Nlistview = (ListView)v.findViewById(R.id.NListView);
        ArrayAdapter<String> Nadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, night_list);
        Nlistview.setAdapter(Nadapter);
        
		builder.setView(v);
		return builder.create();
	}
}
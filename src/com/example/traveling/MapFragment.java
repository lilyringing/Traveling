package com.example.traveling;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import android.location.*;
import android.content.Context;
import android.graphics.Color;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.SupportMapFragment;

public class MapFragment extends Fragment{
	MapView mapview;
	GoogleMap map;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.map, container, false);
		
		// Gets the MapView from the XML layout and creates it
		mapview = (MapView) v.findViewById(R.id.mapview);
		mapview.onCreate(savedInstanceState);
		
		// Gets to GoogleMap from the MapView and does initialization stuff
		map = mapview.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);
		 
		// Needs to call MapsInitializer before doing any CameraUpdateFactory calls
		try {
			MapsInitializer.initialize(this.getActivity());
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		// Updates the location and zoom of the MapView
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(53.558, 9.927), 10);
		map.animateCamera(cameraUpdate);
		return v;
	}

	@Override
	public void onResume() {
		mapview.onResume();
		super.onResume();
	}
 
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapview.onDestroy();
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapview.onLowMemory();
	}
}
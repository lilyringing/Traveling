package com.example.traveling;

import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.app.Fragment;

public class ProfileFragment extends Fragment{
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                 Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.fragment_profile, container,false);
		 
		 return v;
	 }
}
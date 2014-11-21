package com.example.traveling;

import java.util.HashMap;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class InfoWindowDialog extends DialogFragment{
	private String userid;
	
	public static InfoWindowDialog newInstance(HashMap<String, String> data, String id){
        InfoWindowDialog iw = new InfoWindowDialog();
        
        Bundle bun = new Bundle();
        bun.putString("siteid", data.get("siteid"));
        bun.putString("name", data.get("name"));
        bun.putString("phone", data.get("phone"));
        bun.putString("address", data.get("address"));
        bun.putString("score", data.get("score"));
        bun.putString("content", data.get("content"));
        bun.putString("open", data.get("open"));
        bun.putString("ticket", data.get("ticket"));
        bun.putString("website", data.get("website"));
        bun.putString("userid", id);
        iw.setArguments(bun);
        return iw;
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_info, null);
		
		this.userid = getArguments().getString("userid");
		String siteid = getArguments().getString("siteid");
		
		TextView tvName = ((TextView) v.findViewById(R.id.name));
        tvName.setText(getArguments().getString("name"));
        
        TextView tvPhone = ((TextView) v.findViewById(R.id.phone));
        tvPhone.setText(getArguments().getString("phone"));
        
        CheckBox favorite = ((CheckBox) v.findViewById(R.id.favorite));
        String result = DBconnector.executeQuery("SELECT site_id FROM collect_s WHERE fb_id=" + userid + " and site_id=" + siteid);
        Log.e("log_site", result);
        if(!result.isEmpty()){
        	favorite.setChecked(true);
        }
        favorite.setOnCheckedChangeListener(new OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        		if(isChecked){
        			buttonView.setChecked(true);
        		}else{
        			buttonView.setChecked(false);
        		}
        	}
        });
        
        TextView tvAddress = ((TextView) v.findViewById(R.id.address));
        tvAddress.setText(getArguments().getString("address"));
        
        RatingBar score = ((RatingBar) v.findViewById(R.id.score));
        score.setRating(Integer.parseInt(getArguments().getString("score")));
        score.setOnTouchListener(new OnTouchListener() { 
        	public boolean onTouch(View v, MotionEvent event) { 
        		return true; 
        		}
        		});

        TextView tvContent = ((TextView) v.findViewById(R.id.content));
        tvContent.setText(getArguments().getString("content"));
        
        TextView tvOpen = ((TextView) v.findViewById(R.id.open));
        tvOpen.setText(getArguments().getString("open"));
        
        TextView tvTicket = ((TextView) v.findViewById(R.id.ticket));
        tvTicket.setText(getArguments().getString("ticket"));
        
        TextView tvWebsite = ((TextView) v.findViewById(R.id.website));
        tvWebsite.setText(getArguments().getString("website"));
        
        Linkify.addLinks(tvWebsite, Linkify.ALL);
		
		builder.setView(v);
		
		// Create the AlertDialog object and return it
        return builder.create();
	}
}
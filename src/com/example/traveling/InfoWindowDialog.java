package com.example.traveling;

import java.util.HashMap;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class InfoWindowDialog extends DialogFragment{
	private String userid;
	private FragmentManager fragmentManager;
	
	public static InfoWindowDialog newInstance(HashMap<String, String> data, String id, FragmentManager fm){
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
        bun.putString("tag", data.get("tag"));
        bun.putString("userid", id);
        iw.setArguments(bun);
        iw.setFM(fm);
        return iw;
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_info, null);
		
		this.userid = getArguments().getString("userid");
		final String siteid = getArguments().getString("siteid");
		
		TextView tvName = ((TextView) v.findViewById(R.id.name));
        tvName.setText(getArguments().getString("name"));
        
        TextView tvPhone = ((TextView) v.findViewById(R.id.phone));
        tvPhone.setText("電話: " + getArguments().getString("phone"));
        
        CheckBox favorite = ((CheckBox) v.findViewById(R.id.favorite));
        String result = DBconnector.executeQuery("SELECT * FROM collect_s WHERE fb_id=" + userid + " and site_id=" + siteid);
        Log.e("log_site", result);
        if(!result.isEmpty()){
        	favorite.setChecked(true);
        }
        favorite.setOnCheckedChangeListener(new OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        		if(isChecked){
        			// insert into DB
        			//DBconnector.executeQuery("INSERT " + userid + " " + siteid + " INTO collect_s");
        			buttonView.setChecked(true);
        		}else{
        			// remove from DB
        			buttonView.setChecked(false);
        		}
        	}
        });
        
        TextView tvAddress = ((TextView) v.findViewById(R.id.address));
        tvAddress.setText("地址: " + getArguments().getString("address"));
        
        RatingBar score = ((RatingBar) v.findViewById(R.id.score));
        score.setRating(Integer.parseInt(getArguments().getString("score")));
        score.setOnTouchListener(new OnTouchListener() { 
        	public boolean onTouch(View v, MotionEvent event) { 
        		return true; 
        		}
        		});

        Button bTag = ((Button) v.findViewById(R.id.tag));
        bTag.setText(getArguments().getString("tag"));
        
        TextView tvContent = ((TextView) v.findViewById(R.id.content));
        tvContent.setText("簡介: " + getArguments().getString("content"));
        
        TextView tvOpen = ((TextView) v.findViewById(R.id.open));
        tvOpen.setText("營業時間: " + getArguments().getString("open"));
        
        TextView tvTicket = ((TextView) v.findViewById(R.id.ticket));
        int price = Integer.parseInt(getArguments().getString("ticket"));
        if(price == 0){
        	tvTicket.setText("門票: 免費");
        }else{
        	tvTicket.setText("門票: " + price);
        }
        
        TextView tvWebsite = ((TextView) v.findViewById(R.id.website));
        tvWebsite.setText("網址: " + getArguments().getString("website"));
        Linkify.addLinks(tvWebsite, Linkify.ALL);
        
        Button bComment = ((Button) v.findViewById(R.id.comment));
        bComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment comment = CommentDialog.newInstance(userid, siteid);
        		comment.show(fragmentManager,"Restaurant");
			}
		});
       
		builder.setView(v);
		
		// Create the AlertDialog object and return it
        return builder.create();
	}
	
	public void setFM(FragmentManager fm){
		fragmentManager = fm;
	}
}
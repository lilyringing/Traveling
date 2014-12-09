package com.example.traveling;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentsDialog extends DialogFragment{
	public static CommentsDialog newInstance(String siteid){
		CommentsDialog cd = new CommentsDialog();
		Bundle bun = new Bundle();
		bun.putString("siteid", siteid);
        cd.setArguments(bun);
        
		return cd;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String siteid = getArguments().getString("siteid");
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.blank, null);
		LinearLayout ll = (LinearLayout) v.findViewById(R.id.blank); 	
		
		try{
			String result = DBconnector.executeQuery("SELECT * FROM comment, user WHERE user.fb_id=comment.user_id and comment.site_id=" + siteid );
				
			JSONArray jsonArray = new JSONArray(result);
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject jsonData = jsonArray.getJSONObject(i);
				View commentView = inflater.inflate(R.layout.comments, null);

				TextView commenter = (TextView) commentView.findViewById(R.id.commenter);
				commenter.setText(jsonData.getString("user_name"));

				RatingBar score = (RatingBar) commentView.findViewById(R.id.comment_score);
				score.setRating(Integer.parseInt(jsonData.getString("rate")));
				score.setOnTouchListener(new OnTouchListener() { 
		        	public boolean onTouch(View v, MotionEvent event) { 
		        		return true; 
		        		}
		        });

				TextView content = (TextView) commentView.findViewById(R.id.content);
				content.setText(jsonData.getString("content"));
				
				ll.addView(commentView);	
			}
		}catch(JSONException e){
			Log.e("log_tag", e.toString());
		}
		
		builder.setView(v);	
		return builder.create();
	}
}
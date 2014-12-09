package com.example.traveling;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

public class CommentDialog extends DialogFragment{
	public static CommentDialog newInstance(String userid, String site_id){
		CommentDialog cd = new CommentDialog();
		
		Bundle bun = new Bundle();
        bun.putString("siteid", site_id);
        bun.putString("userid", userid);
        cd.setArguments(bun);
        return cd;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.comment, null);
		
		RatingBar score = (RatingBar)v.findViewById(R.id.score);
		score.setStepSize(1);
		final RatingBar scoreBar = score;
		final EditText editTextComment = (EditText)v.findViewById(R.id.input_comment);
		
		builder.setView(v);
		builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				// insert into DB
				String siteid = getArguments().getString("siteid");
				//testing use
				String userid = getArguments().getString("userid");
				float rate = scoreBar.getRating();
				String comment = editTextComment.getText().toString();
				//String query = "INSERT INTO `comment`(`user_id`, `site_id`, `rate`, `content`) VALUES ('"
				//		                              +userid+"', '"+siteid+"', '"+rate+"', '"+comment+"')";
				//DBconnector.executeQuery(query);
				DBconnector.insertComment(userid, siteid, rate, comment);
				
				
				View siteView = inflater.inflate(R.layout.dialog_info, null);
				RatingBar new_score = (RatingBar)siteView.findViewById(R.id.score);
				
				// update score after user make a comment
        		try{
                	String new_score_result = DBconnector.executeQuery("SELECT score FROM site WHERE site_id=" + siteid);
                	  
                	JSONArray jsonArray = new JSONArray(new_score_result);
                	JSONObject jsonData = jsonArray.getJSONObject(0);
                	float nscore = Float.parseFloat(jsonData.getString("score"));
                	new_score.setRating(nscore);
                }catch(JSONException e){
                	Log.e("log_tag", e.toString());
                }

			}
		});
		
		return builder.create();
	}
}
package com.example.traveling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class CommentDialog extends DialogFragment{
	public static CommentDialog newInstance(String site_id){
		CommentDialog cd = new CommentDialog();
		
		Bundle bun = new Bundle();
        bun.putString("siteid", site_id);
        cd.setArguments(bun);
        return cd;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.comment, null);
		
		builder.setView(v);
		builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				// insert into DB
			}
		});
		
		return builder.create();
	}
}
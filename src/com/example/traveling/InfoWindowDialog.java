package com.example.traveling;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class InfoWindowDialog extends DialogFragment{
	
	public static InfoWindowDialog newInstance(HashMap<String, String> data){
        InfoWindowDialog iw = new InfoWindowDialog();
        
        Bundle bun = new Bundle();
        bun.putString("name", data.get("name"));
        bun.putString("phone", data.get("phone"));
        bun.putString("address", data.get("address"));
        bun.putString("score", data.get("score"));
        bun.putString("content", data.get("content"));
        bun.putString("open", data.get("open"));
        bun.putString("ticket", data.get("ticket"));
        bun.putString("website", data.get("website"));
        iw.setArguments(bun);
        return iw;
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_info, null);
		
		TextView tvName = ((TextView) v.findViewById(R.id.name));
        tvName.setText(getArguments().getString("name"));
        TextView tvPhone = ((TextView) v.findViewById(R.id.phone));
        tvPhone.setText(getArguments().getString("phone"));
        TextView tvAddress = ((TextView) v.findViewById(R.id.address));
        tvAddress.setText(getArguments().getString("address"));
        RatingBar score = ((RatingBar) v.findViewById(R.id.score));
        score.setRating(Integer.parseInt(getArguments().getString("score")));
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
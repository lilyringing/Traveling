package com.example.traveling;

import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;

public class MapDialog extends DialogFragment {
    public static MapDialog newInstance(String title, String list){
        MapDialog mp = new MapDialog();
        Bundle bun = new Bundle();
        bun.putString("title", title);
        bun.putString("list", list);
        mp.setArguments(bun);
        return mp;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments.getString("title");
        String list = getArguments.getString("list");
        
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
        	   .setItems(list, new DialogInterface.OnClickListener(){
        		   public void onClick(DialogInterface dialog, int which){
        			   
        		   }
        	   });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

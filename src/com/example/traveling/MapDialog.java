package com.example.traveling;

import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;

public class MapDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.spot)
        	   .setItems(R.array.spot_menu, new DialogInterface.OnClickListener(){
        		   public void onClick(DialogInterface dialog, int which){
        			   
        		   }
        	   });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
    /*public static MapDialog newInstance(String title, String message){
    	
    }*/
}
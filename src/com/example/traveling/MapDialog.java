package com.example.traveling;

import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;

public class MapDialog extends DialogFragment {
	public interface DialogFragmentListener {
        public void onReturnValue(int title, int choice);
    }
	
	public static MapDialog newInstance(int title, int list){
        MapDialog mp = new MapDialog();
        Bundle bun = new Bundle();
        bun.putInt("title", title);
        bun.putInt("list", list);
        mp.setArguments(bun);
        return mp;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        int list = getArguments().getInt("list");
        final int t = title;
        
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
        	   .setItems(list, new DialogInterface.OnClickListener(){
        		// which is the item is the list, start from 0
        		   public void onClick(DialogInterface dialog, int which){
        			   // Pass the value back to MainActivity
        			   DialogFragmentListener activity = (DialogFragmentListener)getActivity();
        			   activity.onReturnValue(t, which);
        		   }
        	   });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

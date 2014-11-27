package com.example.traveling;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MapDialog extends DialogFragment {
	int mSelectedIndex;
	
	public interface DialogFragmentListener {
        public void MarkOnMap(int title, int choice);
    }
	
	public static MapDialog newInstance(int title, String[] list){
        MapDialog mp = new MapDialog();
        Bundle bun = new Bundle();
        ArrayList<String> array_list = new ArrayList<String>();
        int i = 0;
        
        for(i = 0; i < list.length; i++){
        	array_list.add(list[i]);
        }
        
        bun.putInt("title", title);
        bun.putStringArrayList("list", array_list);
        mp.setArguments(bun);
        return mp;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        ArrayList list = getArguments().getStringArrayList("list");
        final int t = title;
        ArrayAdapter<String> adapter;
        
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dia = inflater.inflate(R.layout.listview_dialog, null);
        ListView listview = (ListView)dia.findViewById(R.id.SiteListView);
       
        // Defining the ArrayAdapter to set items to ListView
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        		DialogFragmentListener activity = (DialogFragmentListener)getActivity();
 			    activity.MarkOnMap(t, position);
 			    dismiss();
 			    //Log.e("item", );
        		//view.findViewById();
            	//mSelectedIndex = (int)id;
            	//Toast.makeText(getActivity(), parent.getItemAtPosition(position),Toast.LENGTH_LONG ).show();
            }
        });
        
        /*Button ok = (Button)dia.findViewById(R.id.OkButton);
        ok.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v){
        		DialogFragmentListener activity = (DialogFragmentListener)getActivity();
 			    activity.MarkOnMap(t, mSelectedIndex);
        	}
        });*/
        
        builder.setView(dia);
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
  //builder.setView();
    
	/*.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener(){
		// which is the item is the list, start from 0
		   public void onClick(DialogInterface dialog, int which){
			   Toast.makeText(getActivity(), which,Toast.LENGTH_LONG ).show();
			   mSelectedIndex = which;
		   }
	   })
	   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which){
			   Toast.makeText(getActivity(), which,Toast.LENGTH_LONG ).show();
			   DialogFragmentListener activity = (DialogFragmentListener)getActivity();
			   activity.MarkOnMap(t, mSelectedIndex);
		   }
	   });*/
}

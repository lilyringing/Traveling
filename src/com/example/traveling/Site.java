package com.example.traveling;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class Site extends Activity {
	
    private ListView listView01;
    /*��a�Ior���u�W�٩�J(��ﶵABC�K)*/
    private String[] show_text = {"�ﶵA","�ﶵB","�ﶵC","�ﶵD","�ﶵE"};
    private ArrayAdapter listAdapter;
    private TextView textView01;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.site);
        
        listView01 = (ListView)findViewById(R.id.listView1);
        
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,show_text);
        listView01.setAdapter(listAdapter);
       
        
        listView01.setOnItemClickListener(new OnItemClickListener(){
               @Override
               public void onItemClick(AdapterView parent, View view, int position, long id){
            	  
            	// TODO Auto-generated method stub 
          		 new AlertDialog.Builder(Site.this) 
          		 
          		 /*�u�X���������D*/ 
          		 /*��J���Ior���u���W��(��Title)*/
          		 .setTitle(show_text[position]) 
          		 
          		 /*�]�w�u�X�������T��*/ 
          		 /*�o�J���I�θ��u�������T��(��Content)*/
          		 .setMessage("Content") 
          		 .setPositiveButton("Close", new DialogInterface.OnClickListener() {public void onClick (DialogInterface dialoginterface, int i) { } } )
          		 /*.setNegativeButton("NO",new DialogInterface.OnClickListener(){ //�]�w���X��������^�ƥ�public void onClick(DialogInterface dialoginterface, int i) {} })*/
          		 .show();
                  //Toast.makeText(getApplicationContext(), "�I�諸�O"+show_text[position], Toast.LENGTH_SHORT).show();
                  //listView01.setVisibility(view.INVISIBLE); //����ListView
               } 
        }); 
       
   }	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);

        return true;

    }
}

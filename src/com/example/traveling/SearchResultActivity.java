package com.example.traveling;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;

public class SearchResultActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/*public
	void doSearchQuery(){
	final Intent intent = getIntent();
	//獲得搜索框裡值
	String query=intent.getStringExtra(SearchManager.QUERY);
	tvquery.setText(query);
	//保存搜索記錄
	SearchRecentSuggestions suggestions=new SearchRecentSuggestions(this,
	SearchSuggestionSampleProvider.AUTHORITY, SearchSuggestionSampleProvider.MODE);
	suggestions.saveRecentQuery(query, null);
	if(Intent.ACTION_SEARCH.equals(intent.getAction())){
	//獲取傳遞的資料
	Bundle bundled=intent.getBundleExtra(SearchManager.APP_DATA);
	if(bundled!=null){
	String ttdata=bundled.getString("data");
	tvdata.setText(ttdata);
	}else{
	tvdata.setText("no data");
	}
	}
	}*/
}
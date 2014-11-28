package com.example.traveling;

import java.util.Arrays;

import com.example.traveling.MapDialog.DialogFragmentListener;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Toast;

public class FBFragment extends Fragment{
	private static final String TAG = "FBLogin";
	private UiLifecycleHelper uiHelper;
	private String name;
	private String uid;
	private LoginButton authButton;
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	public interface SetUserData {
        public void SetUser(String id, String name);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		//View v = inflater.inflate(R.layout.main, container, false);
		View v = inflater.inflate(R.layout.fb_login, container, false);
		
		authButton = (LoginButton) v.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("public_profile"));
		
		Session session = Session.getActiveSession();
		if(session == null){
			authButton.setVisibility(View.VISIBLE);
		}else{
			authButton.setVisibility(View.INVISIBLE);
		}
			
		//To allow fragment receiving the onActivityResult()
		return v;
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	public String GetUserName(){
		return name;
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        //Toast.makeText(getActivity(), "Logged in",Toast.LENGTH_LONG ).show();
	        
	        Request.newMeRequest(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        // Display the parsed user info
                        uid = GetUserId(user);
                    	name = GetUserName(user);
                        //Toast.makeText(getActivity(), uid, Toast.LENGTH_LONG ).show();
                        
                        SetUserData activity = (SetUserData)getActivity();
         			   	activity.SetUser(uid, name);
                        
                    }
                }
            }).executeAsync();
	                
	    } else if (state.isClosed()) {
	    	authButton.setVisibility(View.VISIBLE);
	        Log.i(TAG, "Logged out...");			
	        //Toast.makeText(getActivity(), "Logged out",Toast.LENGTH_LONG ).show();
	    }
	}
	
	private String GetUserId(GraphUser user){
		return user.getId();
		//user.getBirthday();
	}
	
	private String GetUserName(GraphUser user){
		return user.getName();
		//user.getBirthday();
	}
	 
	 public String GetName(){
		 return name;
	 }
}
package com.userside;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity{
	private Context context;
	private MenuInflater menuPopOut;
	private ConnectionThread connctionThread;
	private Button connectButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init(this);
	}
	
	private void init(Context context){
		connctionThread = new ConnectionThread();
		connectButton = (Button) findViewById(R.id.connectButton);
		
		connectButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				connctionThread.connect();
			}
		});
	}

	private void updatePreferences(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		StaticValues.SERVER_IP = preferences.getString("ip", "10.0.2.2");
		StaticValues.PORT = Integer.parseInt(preferences.getString("port", "4444"));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menuPopOut = getMenuInflater();
		menuPopOut.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.preferences){
			Intent intent;
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
				intent = new Intent("android.intent.action.PREFENCES");
			else
				intent = new Intent("android.intent.action.NEW_VERSION_PREFENCES");
			startActivity(intent);
		}
		return true;
	}
	
	public void onPause(){
		super.onPause();
		connctionThread.onPause();
	}
	
	public void onResume(){
		updatePreferences();
		super.onResume();
		connctionThread = new ConnectionThread();
		connctionThread.start();
	}
}
package com.robotside;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


public class MainActivity extends Activity {
	private final static String TAG = "MainActivity";
	private RelativeLayout layout;
	private ControlThread controlThread;
	private StreamingThread streamingThread;
	private ConnectionThread connectionThread ;
	private MySurfaceView surfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		layout = new RelativeLayout(this);
		
		init(this);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		
		layout.addView(surfaceView, params);
		
		init(this);
		setContentView(layout);
	}
	
	private void init(Context context){
		surfaceView = new MySurfaceView(context);
	}
	
	public void onResume(){
		super.onResume();
		controlThread= new ControlThread();
		streamingThread = new StreamingThread();
		
		connectionThread= new ConnectionThread(surfaceView, controlThread, streamingThread);
		connectionThread.start();
	}
	
	public void onPause(){
		super.onPause();
		surfaceView.onPause();
	}
}

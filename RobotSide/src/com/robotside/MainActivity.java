package com.robotside;

import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspServer;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;


public class MainActivity extends Activity {
	private final static String TAG = "MainActivity";
	private net.majorkernelpanic.streaming.gl.SurfaceView mSurfaceView;	
//	private SensorManager mSensorManager = null;
	private StremingThread videoStrem;
	private TelemetryThread sensorsThread;
	private ControlThread ioioThread;
	private String ip_address;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Sets the port of the RTSP server to 1234
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
				.edit();
		editor.putString(RtspServer.KEY_PORT, String.valueOf(1234));
		editor.commit();
		
		init(this);
	}
	
	private void init(Context context){
		mSurfaceView = (SurfaceView) findViewById(R.id.surface);
		videoStrem = new StremingThread(context, mSurfaceView);
		sensorsThread = new TelemetryThread(context);
		ioioThread = new ControlThread();
	}
	
	public void onResume(){
		super.onResume();
//		ioioThread.start();
		videoStrem.start();
	}
	
	public void onPause(){
		super.onPause();
		videoStrem.stopStream();
		sensorsThread.stop_thread();
//		ioioThread.abort();
	}
}

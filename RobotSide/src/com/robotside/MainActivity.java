package com.robotside;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.rtsp.RtspServer;
import net.majorkernelpanic.streaming.video.VideoQuality;
import ioio.lib.util.android.IOIOActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


public class MainActivity extends Activity {
	private final static String TAG = "MainActivity";
	private net.majorkernelpanic.streaming.gl.SurfaceView mSurfaceView;
	MainThread mainThread;
	
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
		 
		mainThread= new MainThread(this);
		mainThread.run();
	}

	private void enableUi(final boolean enable) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// mPanSeekBar.setEnabled(enable);
				// mTiltSeekBar.setEnabled(enable);
			}
		});
	}

}

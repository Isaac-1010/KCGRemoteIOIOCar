package com.robotside;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;

public class StreamingThread extends Thread implements PreviewCallback {
	private final static String TAG = "Streaming Thread";
	private MySurfaceView surfaceView;

	public StreamingThread() {

	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Log.e(TAG,"New Frame");
		
	}
	
	public void onPause(){

	}
}
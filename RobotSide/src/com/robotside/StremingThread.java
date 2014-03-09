package com.robotside;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspServer;
import net.majorkernelpanic.streaming.video.VideoQuality;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.SurfaceHolder;

public class StremingThread extends Thread  implements Session.Callback , SurfaceHolder.Callback {
	private final static String TAG = "Streaming Thread";
	private SurfaceView mSurfaceView;
	public Session session;
	private Context context;

	public StremingThread(Context context, SurfaceView surfaceView) {
		
		mSurfaceView = surfaceView;
		this.context = context;
		session = SessionBuilder.getInstance()
				.setCallback(this)
				.setSurfaceView(mSurfaceView)
				.setPreviewOrientation(90)
				.setContext(context)
				.setAudioEncoder(SessionBuilder.AUDIO_NONE)
				.setVideoEncoder(SessionBuilder.VIDEO_H264)
				.setVideoQuality(new VideoQuality(320, 240, 15, 500000))
				.build();

		
		mSurfaceView.getHolder().addCallback(this);

	}




	public void stopStraming() {
		session.release();
	}



	@Override
	public void onBitrareUpdate(long bitrate) {
		Log.d(TAG,"Bitrate: "+bitrate);
	}

	@Override
	public void onSessionError(int message, int streamType, Exception e) {
		if (e != null) {
			logError(e.getMessage());
		}
	}

	@Override
	
	public void onPreviewStarted() {
		Log.d(TAG,"Preview started.");
	}

	@Override
	public void onSessionConfigured() {
		Log.d(TAG,"Preview configured.");
		// Once the stream is configured, you can get a SDP formated session description
		// that you can send to the receiver of the stream.
		// For example, to receive the stream in VLC, store the session description in a .sdp file
		// and open it with VLC while streming.
		Log.d(TAG, session.getSessionDescription());
		session.start();
	}

	@Override
	public void onSessionStarted() {
		Log.d(TAG,"Session started.");

	}

	@Override
	public void onSessionStopped() {
		Log.d(TAG,"Session stopped.");
	}	
	
	/** Displays a popup to report the eror to the user */
	private void logError(final String msg) {
		final String error = (msg == null) ? "Error unknown" : msg; 
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(error).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		session.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		session.stop();
	}

}

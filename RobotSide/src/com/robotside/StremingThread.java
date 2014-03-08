package com.robotside;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspServer;
import net.majorkernelpanic.streaming.video.VideoQuality;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;

public class StremingThread extends Thread {

	private Activity _app;
	private SurfaceView mSurfaceView;
	public Session session;

	public StremingThread(Activity app) {

		mSurfaceView = (SurfaceView) app.findViewById(R.id.surface);
		_app = app;
		// Configures the SessionBuilder
		session = SessionBuilder.getInstance().setSurfaceView(mSurfaceView)
				.setPreviewOrientation(90)
				.setContext(app.getApplicationContext())
				.setAudioEncoder(SessionBuilder.AUDIO_NONE)
				.setVideoEncoder(SessionBuilder.VIDEO_H264)
				.setVideoQuality(new VideoQuality(320, 240, 15, 500000))
				.build();

	}

	@Override
	public void run() {
		super.run();
		// Starts the RTSP server
		_app.startService(new Intent(_app, RtspServer.class));

	}

	public void stopStream() {
		// Stops the streaming session
		session.stop();
	}

}

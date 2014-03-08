package com.userside;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

public class MainActivity extends Activity implements SurfaceHolder.Callback,
		OnPreparedListener {

	MediaPlayer mediaPlayer;
	SurfaceHolder surfaceHolder;
	SurfaceView playerSurfaceView;
	String videoSrc = "rtsp://192.168.3.109:1234";

	private final int PAN_PIN = 3;
	private final int TILT_PIN = 6;

	private final int PWM_FREQ = 100;

	private SeekBar mPanSeekBar;
	private SeekBar mTiltSeekBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPanSeekBar = (SeekBar) findViewById(R.id.panSeekBar);
		mTiltSeekBar = (SeekBar) findViewById(R.id.tiltSeekBar);
		playerSurfaceView = (SurfaceView) findViewById(R.id.playersurface);

		surfaceHolder = playerSurfaceView.getHolder();
		surfaceHolder.addCallback(this);

	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {

		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.setDataSource(videoSrc);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mediaPlayer.start();
	}

}

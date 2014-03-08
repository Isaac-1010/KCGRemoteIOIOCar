package com.robotside;

import net.majorkernelpanic.streaming.gl.SurfaceView;
import android.app.Activity;
import android.hardware.SensorManager;

public class MainThread extends Thread {

	SensorManager mSensorManager = null;
	StremingThread videoStrem;
	TelemetryThread sensorsThread;
	ControlThread ioioThread;
	String ip_address;
	Activity the_app;

	public MainThread(Activity app) {
		super();
		
		the_app = app;
		videoStrem = new StremingThread(app);

		sensorsThread = new TelemetryThread(app);
		ioioThread = new ControlThread();

	}

	public void run() {
		ioioThread.start();
		videoStrem.run();
	}

	public void stop_simu() {
		videoStrem.stopStream();
		sensorsThread.stop_thread();
		ioioThread.abort();
	}
}

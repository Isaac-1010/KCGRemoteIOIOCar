package com.robotside;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.content.Context;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class TelemetryThread extends Thread implements SensorEventListener{
	final String tag = "Sensors";
    Activity parent_context;	
	InetAddress serverAddr;
	DatagramSocket socket;	
	
    SensorManager mSensorManager;
    Sensor accSensor;
    Sensor magnetSensor;

    float gravity[];
    float geoMagnetic[];
    float azimut;
    float pitch;
    float roll;

   
	public TelemetryThread(Context context)
    {
    

		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void stop_thread()
    {
    	
    	socket.close();
        mSensorManager.unregisterListener((SensorEventListener) this, accSensor);
        mSensorManager.unregisterListener((SensorEventListener)this, magnetSensor);
    }

   
    
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravity = event.values.clone();
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geoMagnetic = event.values.clone();

        if (gravity != null && geoMagnetic != null) {

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geoMagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = 57.29578F * orientation[0];
                pitch = 57.29578F * orientation[1];
                roll = 57.29578F * orientation[2];

                float dist = Math.abs((float) (1.4f * Math.tan(pitch * Math.PI / 180)));

                Log.d("log", "orientation values: " + azimut + " / " + pitch + " / " + roll + " dist = " + dist);
            }
        }
    }

    protected void onResume() {
     
        //mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
      
       // mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
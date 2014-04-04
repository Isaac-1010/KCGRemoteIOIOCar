package com.robotside;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;

public class StreamingThread extends Thread implements PreviewCallback {
	private final static String TAG = "Streaming Thread";
	private MySurfaceView surfaceView;
	private DatagramSocket socket;
	private boolean finished;
	private int port;
	private DatagramPacket packet;
	private InetAddress clientIp;

	public StreamingThread() {
		
	}
	
	public void run(){
		while(!finished){
			
		}
	}
	
	public void setSocket(DatagramSocket socket, InetAddress clientIp, int port){
		this.socket = socket;
		this.clientIp = clientIp;
		this.port = port;
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		packet = new DatagramPacket(data, data.length, clientIp, port);
		if (socket != null){
			try {
				socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void onPause(){
		finished = true;
	}
}
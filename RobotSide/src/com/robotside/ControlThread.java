package com.robotside;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Activity;
import android.util.Log;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.IOIOFactory;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;

public class ControlThread extends Thread {

	/** Subclasses should use this field for controlling the IOIO. */
	protected IOIO ioio_;
	private boolean abort_ = false;
	private PwmOutput servo, motor;
	int size_p;
	int servo_val = 1500, motor_val = 1500;

	final String tag = "Sensors";
	InetAddress serverAddr;
	DatagramSocket socket;
	String ip_address;

	boolean START = true;
	int a_nb = 0;

	public ControlThread() {

		try {
			serverAddr = InetAddress.getByName(ip_address);
			socket = new DatagramSocket();
		} catch (Exception exception) {
			Log.e(tag, "Error: ", exception);
		}
	}

	/** Not relevant to subclasses. */
	@Override
	public final void run() {
		super.run();
		while (true) {
			try {
				synchronized (this) {
					if (abort_) {
						break;
					}
					ioio_ = IOIOFactory.create();
				}
				ioio_.waitForConnect();
				setup();
				while (true) {
					loop();
				}
			} catch (ConnectionLostException e) {
				if (abort_) {
					break;
				}
			} catch (Exception e) {
				Log.e("AbstractIOIOActivity", "Unexpected exception caught", e);
				ioio_.disconnect();
				break;
			} finally {
				try {
					ioio_.waitForDisconnect();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	protected void setup() throws ConnectionLostException {
		motor = ioio_.openPwmOutput(new DigitalOutput.Spec(5,
				DigitalOutput.Spec.Mode.OPEN_DRAIN), 50);
		servo = ioio_.openPwmOutput(new DigitalOutput.Spec(7,
				DigitalOutput.Spec.Mode.OPEN_DRAIN), 50);

		servo.setPulseWidth(1500); // pulse is between 1000 and 2000
		motor.setPulseWidth(1500);

		/********************* send phone IP address to computer *********************/
		byte[] data = new byte[1];
		data[0] = (byte) (1);
		size_p = data.length;
		DatagramPacket packet = new DatagramPacket(data, size_p, serverAddr,
				9002);
		try {
			socket.send(packet);
		} catch (IOException e) {
			Log.e("IOIO_thread", "error: ", e);
		}
	}

	protected void loop() throws ConnectionLostException {
		try {
			byte[] data2 = new byte[4];
			DatagramPacket receivePacket = new DatagramPacket(data2,
					data2.length);
			try {
				socket.receive(receivePacket);
			} catch (Exception e) {
			}

			byte[] data3 = receivePacket.getData();
			servo_val = (int) ((data3[0] & 0xff) << 8 | (data3[1] & 0xff));
			motor_val = (int) ((data3[2] & 0xff) << 8 | (data3[3] & 0xff));
			servo.setPulseWidth(servo_val); // pulse is between 1000 and 2000
			motor.setPulseWidth(motor_val);
		} catch (Exception e) {
			Log.e("IOIO_thread", "error: ", e);
		}
	}

	/** Not relevant to subclasses. */
	public synchronized final void abort() {
		abort_ = true;
		servo.close();
		motor.close();
		socket.close();
		if (ioio_ != null) {
			ioio_.disconnect();
		}
	}
}

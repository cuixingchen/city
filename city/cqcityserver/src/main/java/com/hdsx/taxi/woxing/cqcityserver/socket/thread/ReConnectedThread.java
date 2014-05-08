package com.hdsx.taxi.woxing.cqcityserver.socket.thread;

import java.util.Timer;
import java.util.TimerTask;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;

public class ReConnectedThread extends AbsThread {

	static ReConnectedThread obj;

	public static ReConnectedThread getInstance() {
		if (obj == null)
			obj = new ReConnectedThread();
		return obj;

	}

	private Timer timer = new Timer();

	/**
	 * 重新连接
	 * 
	 * @param dalay
	 * @param period
	 */

	@Override
	public void runThread(long delay, long period) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				TcpClient.getInstance().reconnect();
			}
		}, delay, period);

	}
}

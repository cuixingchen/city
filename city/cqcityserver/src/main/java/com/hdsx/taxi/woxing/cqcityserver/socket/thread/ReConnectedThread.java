package com.hdsx.taxi.woxing.cqcityserver.socket.thread;

import java.util.Timer;
import java.util.TimerTask;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;

public class ReConnectedThread {
	private Timer timer = new Timer();

	/**
	 * 重新连接
	 * 
	 * @param dalay
	 * @param period
	 */
	public void run(long dalay, long period) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				TcpClient.getInstance().reconnect();
			}
		}, dalay, period);
	}
}

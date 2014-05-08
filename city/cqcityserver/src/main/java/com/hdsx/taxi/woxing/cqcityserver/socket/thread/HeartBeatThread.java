package com.hdsx.taxi.woxing.cqcityserver.socket.thread;

import java.util.Timer;
import java.util.TimerTask;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0002;

public class HeartBeatThread extends AbsThread {
	static HeartBeatThread obj;

	public static HeartBeatThread getInstance() {
		if (obj == null)
			obj = new HeartBeatThread();
		return obj;
	}

	private Timer timer = new Timer();

	/**
	 * 重新发送消息
	 * 
	 * @param dalay
	 * @param period
	 */

	@Override
	public void runThread(long delay, long period) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				TcpClient.getInstance().sendWithoutCache(new Msg0002());
			}
		}, delay, period);

	}
}

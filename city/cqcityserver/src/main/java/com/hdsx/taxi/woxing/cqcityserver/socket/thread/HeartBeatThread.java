package com.hdsx.taxi.woxing.cqcityserver.socket.thread;

import java.util.Timer;
import java.util.TimerTask;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0002;

public class HeartBeatThread {
	private Timer timer = new Timer();

	/**
	 * 重新发送消息
	 * 
	 * @param dalay
	 * @param period
	 */
	public void run(long dalay, long period) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				TcpClient.getInstance().sendWithoutCache(new Msg0002());
			}
		}, dalay, period);
	}
}

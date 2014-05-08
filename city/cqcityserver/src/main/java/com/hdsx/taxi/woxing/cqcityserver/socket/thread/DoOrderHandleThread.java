package com.hdsx.taxi.woxing.cqcityserver.socket.thread;

import java.util.Timer;
import java.util.TimerTask;

import com.hdsx.taxi.woxing.cqcityserver.order.OrderService;

/**
 * 处理订单线程
 * 
 * @author Steven
 * 
 */
public class DoOrderHandleThread extends AbsThread {
	static DoOrderHandleThread obj;

	public static DoOrderHandleThread getInstance() {
		if (obj == null)
			obj = new DoOrderHandleThread();
		return obj;
	}

	private Timer timer = new Timer();

	@Override
	public void runThread(long delay, long period) {
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				OrderService.getInstance().doOrderHandle();
			}

		}, delay, period);

	}

}

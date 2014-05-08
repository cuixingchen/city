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
public class DoOrderHandleThread {
	private Timer timer = new Timer();

	public void run(long dalay, long period) {
		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				OrderService.getInstance().doOrderHandle();
			}

		}, dalay, period);
	}

}

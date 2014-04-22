package com.hdsx.taxi.woxing.cqcityserver.socket.thread;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.location.distributeservice.TaxiDistrbuteService;
import com.hdsx.taxi.woxing.location.util.Config;

/**
 * 打车指数重新计算线程
 * 
 * @author Steven
 * 
 */

public class CalTaxiIndexThread {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CalTaxiIndexThread.class);

	private Timer timer = new Timer();

	public void run() {
		if (!Config.TAXI_DISTRIBUTE_ENABLED)
			return;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				logger.info("开始重新计算打车指数");
				TaxiDistrbuteService.getInstance().recal(Config.ISGENERATETIFF);
				logger.info("计算打车指数成功");
			}
		}, Config.TAXI_DISTRIBUTE_DELAY, Config.TAXI_DISTRIBUTE_DELAY);
	}
}

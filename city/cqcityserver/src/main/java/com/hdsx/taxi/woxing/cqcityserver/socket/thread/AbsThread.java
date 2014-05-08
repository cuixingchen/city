package com.hdsx.taxi.woxing.cqcityserver.socket.thread;

/**
 * 线程抽象类
 * 
 * @author Steven
 * 
 */
public abstract class AbsThread {

	public boolean isRun = false;

	public void run(long delay, long period) {
		if (isRun)
			return;
		isRun = true;
		runThread(delay, period);
	}

	public abstract void runThread(long delay, long period);

}

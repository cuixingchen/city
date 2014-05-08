package com.hdsx.taxi.woxing.cqcityserver.socket.thread;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.hdsx.taxi.driver.cq.tcp.cache.MsgObj;
import com.hdsx.taxi.woxing.cqcityserver.socket.MsgCache;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;

public class ReSendMsgThread extends AbsThread {

	static ReSendMsgThread obj;

	public static ReSendMsgThread getInstance() {
		if (obj == null)
			obj = new ReSendMsgThread();
		return obj;
	}

	private Timer timer = new Timer();

	/**
	 * 
	 * executeWork:(重发消息，每10秒重发一次消息，重发5次后删除，24小时).
	 * 
	 * @author sid
	 */
	public void executeWork() {
		List<MsgObj> cleanAndgetResendMsg = MsgCache.getInstance()
				.cleanAndgetResendMsg(10, 5, 24);
		for (MsgObj m : cleanAndgetResendMsg) {
			int count = m.getSendedcount();
			Date now = new Date();
			m.setSendedcount(++count);
			m.setSendtime(now);
			TcpClient.getInstance().sendWithoutCache(m.getMsg());
		}
	}

	@Override
	public void runThread(long delay, long period) {
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				executeWork();

			}
		}, delay, period);

	}
}

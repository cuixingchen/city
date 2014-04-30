package com.hdsx.taxi.woxing.cqcityserver.socket.thread;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.hdsx.taxi.driver.cq.tcp.cache.MsgObj;
import com.hdsx.taxi.woxing.cqcityserver.socket.MsgCache;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;


public class ReSendMsgThread {

	private Timer timer = new Timer();

	public void run(long dalay, long period) {
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				executeWork();
				
			}
		}, dalay, period);
	}

	/**
	 * 
	 * executeWork:(重发消息，每10秒重发一次消息，重发5次后删除，24小时). 
	 *
	 * @author sid
	 */
	public void executeWork() {
		List<MsgObj> cleanAndgetResendMsg = MsgCache.getInstance().cleanAndgetResendMsg(10, 5,24);
		for (MsgObj m : cleanAndgetResendMsg) {
			int count = m.getSendedcount();
			Date now = new Date();
			m.setSendedcount(++count);
			m.setSendtime(now);
			TcpClient.getInstance().sendWithoutCache(m.getMsg());
		}
	}
}

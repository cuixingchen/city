package com.hdsx.taxi.woxing.cqcityserver.order;

import java.io.IOException;

import javax.jms.JMSException;

import com.hdsx.taxi.woxing.bean.util.CacheManagerUtil;
import com.hdsx.taxi.woxing.cqcityserver.mq.CQMQMessageListener;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.mqutil.MQService;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TcpClient.getInstance().run();
//		CacheManagerUtil.path=System.getProperty("user.dir")+"/target/classes/ehcache.xml";
		
		OrderService.getInstance();
		try {
			MQService.getInstance().initcity(new CQMQMessageListener());
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}

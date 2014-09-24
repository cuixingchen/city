package com.hdsx.taxi.woxing.cqcityserver.order;

import java.io.IOException;

import javax.jms.JMSException;

import com.hdsx.taxi.woxing.cqcityserver.mq.CQMQMessageListener;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.mqutil.MQService;

public class Main {

	public static void main(String[] args) {
		try {
			MQService.getInstance().initcity(new CQMQMessageListener());
		} catch (JMSException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		TcpClient.getInstance().run();		
		OrderService.getInstance();
		

	}

}

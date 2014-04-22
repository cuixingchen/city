package com.hdsx.taxi.woxing.cqcityserver;

import java.net.URL;

import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.mq.CQMQMessageListener;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;

/**
 * 运行程序的住
 * 
 * @author Steven
 * 
 */
public class Launcher {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Launcher.class);

	public static void main(String[] args) {

		String taxi_home = System.getProperty("taxi.home",
				System.getProperty("user.dir"));

		System.out.println(taxi_home);

		URL r = TcpClient.class.getResource("/tcp.properties");
		logger.info("main(String[]) - URL r={}", r); //$NON-NLS-1$

	

		MessageListener listener = new CQMQMessageListener();

//		try {
//
//			// 连接TcpClient
			TcpClient.getInstance().start();
//			// 连接ActiveMQ
//			MQService.getInstance().initcity(listener);
//
//		} catch (JMSException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}

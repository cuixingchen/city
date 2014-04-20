package com.hdsx.taxi.woxing.cqcityserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.MessageListener;

import com.hdsx.taxi.woxing.mqutil.MQService;

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
	private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

	public static void main(String[] args) {
		
		MessageListener listener = null;
		//连接ActiveMQ
		try {
			MQService.getInstance().init(listener);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

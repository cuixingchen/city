package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;

public class CommonHandler implements IMQMsgHanlder {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CommonHandler.class);

	@Override
	public void dohandle(MQAbsMsg msg) {
		logger.info(msg.toString());
	}

}

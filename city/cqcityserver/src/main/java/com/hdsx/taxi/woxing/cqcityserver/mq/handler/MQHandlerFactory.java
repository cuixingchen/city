package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;

public class MQHandlerFactory  {

	public static IMQMsgHanlder getHandlder(MQAbsMsg mqmsg) {
		
		return new CommonHandler();
		
	}

}

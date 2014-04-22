package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1001;

public class MQMsgHandler0001 implements IMQMsgHanlder {

	@Override
	public void dohandle(MQAbsMsg mqmsg) {
		MQMsg1001  msg=new MQMsg1001();
		
		
	}

}

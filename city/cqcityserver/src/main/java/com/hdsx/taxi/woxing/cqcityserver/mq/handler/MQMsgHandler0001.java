package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.cqmsg.msg.Msg0x1001;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;

public class MQMsgHandler0001 implements IMQMsgHanlder {

	@Override
	public void dohandle(MQAbsMsg mqmsg) {
		Msg0x1001  msg=new Msg0x1001();
		
		
	}

}

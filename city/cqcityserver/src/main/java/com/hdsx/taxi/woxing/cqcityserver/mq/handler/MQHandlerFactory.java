package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import java.util.HashMap;

import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;

public class MQHandlerFactory {

	static HashMap<Short, IMQMsgHanlder> map=new HashMap<Short, IMQMsgHanlder>();
	static {
		map.put((short)0x0001, new MQMsgHandler0001());
		map.put((short)0x0003, new MQMsgHandler0003());
		map.put((short)0x2001, new MQMsgHandler2001());
		map.put((short)0x2002, new MQMsgHandler2002());
		map.put((short)0x2004, new MQMsgHandler2004());
		map.put((short)0x2005, new MQMsgHandler2005());
	}

	public static IMQMsgHanlder getHandlder(MQAbsMsg mqmsg) {

		return map.get(mqmsg.getHead().getMsgId());

	}

}

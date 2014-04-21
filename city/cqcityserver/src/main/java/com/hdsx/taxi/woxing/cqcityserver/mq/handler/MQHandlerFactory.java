package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import java.util.HashMap;

import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;

public class MQHandlerFactory {

	static HashMap<Integer, IMQMsgHanlder> map;
	static {
		map.put(0x0001, new MQMsgHandler0001());

	}

	public static IMQMsgHanlder getHandlder(MQAbsMsg mqmsg) {

		return map.get(mqmsg.getHead().getMsgId());

	}

}

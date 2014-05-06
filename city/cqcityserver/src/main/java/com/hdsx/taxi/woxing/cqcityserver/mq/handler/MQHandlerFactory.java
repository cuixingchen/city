package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;

public class MQHandlerFactory {

//	static HashMap<Short, IMQMsgHanlder> map=new HashMap<Short, IMQMsgHanlder>();
//	static {
//		map.put((short)0x0001, new MQMsgHandler0001());
//		map.put((short)0x0003, new MQMsgHandler0003());
//		map.put((short)0x2001, new MQMsgHandler2001());
//		map.put((short)0x2002, new MQMsgHandler2002());
//		map.put((short)0x2004, new MQMsgHandler2004());
//		map.put((short)0x2005, new MQMsgHandler2005());
//	}

	public static IMQMsgHanlder getHandlder(MQAbsMsg mqmsg) {

		IMQMsgHanlder h=null;
		
		switch (mqmsg.getHead().getMsgId()) {
		case 0x0001:
			h=new MQMsgHandler0001();
			break;

		case 0x0003:
			h=new MQMsgHandler0003();
			break;
		case 0x2001:
			h=new MQMsgHandler2001();
			break;
		case 0x2002:
			h=new MQMsgHandler2002();
			break;
		case 0x2004:
			h=new MQMsgHandler2004();
			break;
		case 0x2005:
			h=new MQMsgHandler2005();
			break;
		}
		return h;

	}

}

package com.hdsx.taxi.woxing.cqcityserver.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler2001;
import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler2005;
import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler2010;
import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler2011;
import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler2012;
import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler2013;
import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler2015;
import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler2016;
import com.hdsx.taxi.woxing.cqcityserver.socket.hanlder.Handler3003;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

public class HandlerFactory {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TcpHandler.class);

//	static HashMap<Integer, IHandler> map = new HashMap<>();
//	static {
//		map.put(0x2001, new Handler2001());
//		map.put(0x2005, new Handler2005());
//		map.put(0x2010, new Handler2010());
//		map.put(0x2011, new Handler2011());
//		map.put(0x2012, new Handler2012());
//		map.put(0x2013, new Handler2013());
//		map.put(0x2015, new Handler2015());
//		map.put(0x2016, new Handler2016());
//		map.put(0x3003, new Handler3003());
//
//	}

//	public static IHandler getHandler1(AbsMsg m) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("getHandler(AbsMsg) - start:msgid :" + m.getHeader().getMsgid()); //$NON-NLS-1$
//		}
//
//		Integer msgid = (int) m.getHeader().getMsgid();
//		IHandler returnIHandler = map.get(msgid);
//		if (logger.isDebugEnabled()) {
//			logger.debug("getHandler(AbsMsg) - end"); //$NON-NLS-1$
//		}
//		return returnIHandler;
//	}

	public static IHandler getHandler(AbsMsg m) {
		if (logger.isDebugEnabled()) {
			logger.debug("getHandler(AbsMsg) - start:msgid :" + m.getHeader().getMsgid()); //$NON-NLS-1$
		}
		Integer msgid = (int) m.getHeader().getMsgid();
		IHandler h = null;
		switch (msgid) {
		case 0x2001:
			h = new Handler2001();
			break;
			
		case 0x2005:
			h = new Handler2005();
			break;
		case 0x2010:
			h = new Handler2010();
			break;
		case 0x2011:
			h = new Handler2011();
			break;
		case 0x2012:
			h = new Handler2012();
			break;
		case 0x2013:
			h = new Handler2013();
			break;
		case 0x2015:
			h = new Handler2015();
			break;
		case 0x2016:
			h = new Handler2016();
			break;
		case 0x3003:
			h = new Handler3003();
			break;
		}
		return h;
	}
}

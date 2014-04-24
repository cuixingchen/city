package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqmsg.msg.Msg3003;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

public class Handler3003 implements IHandler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Handler3003.class);

	@Override
	public void doHandle(IMsg m) {
		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - start"); //$NON-NLS-1$
		}

		if (m.getClass().isInstance(Msg3003.class)) {
			Msg3003 msg = (Msg3003) m;

//			MQMsg1001 mqmsg = new MQMsg1001();
//
//			mqmsg.setNumber(msg.getCarNumber());
//			mqmsg.setPhone(msg.getPhone());
//			mqmsg.setDriverid(msg.getCertificate());
//			mqmsg.setTime(msg.getBcdtime());
//			mqmsg.setLat(CoordinateCodec.Coor2Float(msg.getLng()));
//			mqmsg.setLon(CoordinateCodec.Coor2Float(msg.getLat()));
//			MQService.getInstance().sendMsg(mqmsg);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - end"); //$NON-NLS-1$
		}
	}
}

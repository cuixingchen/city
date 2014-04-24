package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2016;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

/**
 * 乘客上车通知
 * 
 * @author Steven
 * 
 */
public class Handler2016 implements IHandler {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Handler2016.class);

	@Override
	public void doHandle(IMsg m) {
		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - start"); //$NON-NLS-1$
		}

		if (m.getClass().isInstance(Msg2016.class)) {
			Msg2016 msg = (Msg2016) m;
			TcpClient.getInstance().sendAnsworMsg(msg);
//			MQMsg1007 mqmsg = new MQMsg1007();
//			mqmsg.setOrderid(msg.getHeader().getOrderid());
//			mqmsg.setLat(CoordinateCodec.Coor2Float(msg.getLat()));
//			mqmsg.setLon(CoordinateCodec.Coor2Float(msg.getLng()));
//			mqmsg.setTime(msg.getBcdtime());
//			MQService.getInstance().sendMsg(mqmsg);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - end"); //$NON-NLS-1$
		}
	}

}

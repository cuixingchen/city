package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.util.coor.CoordinateCodec;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2011;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1005;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

/**
 * 预约订单执行通知
 * 
 * @author Steven
 * 
 */
public class Handler2011 implements IHandler {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Handler2011.class);

	@Override
	public void doHandle(IMsg m) {
		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - start"); //$NON-NLS-1$
		}

		if (m instanceof Msg2011) {
			Msg2011 msg = (Msg2011) m;
			TcpClient.getInstance().sendAnsworMsg(msg);
			MQMsg1005 mqmsg = new MQMsg1005();
			mqmsg.getHead().setCustomId("customid");
			mqmsg.setOrderid(msg.getHeader().getOrderid());
			mqmsg.setCarLicensenumber(msg.getCarNumber());
			mqmsg.setLat(msg.getLat());
			mqmsg.setLon(msg.getLng());
			mqmsg.setTime(msg.getBcdtime());
			MQService.getInstance().sendMsg(mqmsg);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - end"); //$NON-NLS-1$
		}
	}

}

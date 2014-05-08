package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2012;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1006;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

/**
 *付款通知
 * 
 * @author Steven
 * 
 */
public class Handler2012 implements IHandler {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Handler2012.class);

	@Override
	public void doHandle(IMsg m) {
		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - start"); //$NON-NLS-1$
		}

		if (m instanceof Msg2012) {
			Msg2012 msg = (Msg2012) m;
//			TcpClient.getInstance().sendAnsworMsg(msg);
			MQMsg1006 mqmsg = new MQMsg1006();
			mqmsg.getHead().setCustomId("customid");
			mqmsg.setOrderid(msg.getHeader().getOrderid());
			mqmsg.setCarlicensenumber(msg.getCarNumber());

			mqmsg.setDriverid(msg.getCertificate());
			mqmsg.setFee(msg.getSum());
			mqmsg.setFee2(msg.getCost());
						
			mqmsg.setTime(msg.getBcdtime());
			MQService.getInstance().sendMsg(mqmsg);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - end"); //$NON-NLS-1$
		}
	}

}

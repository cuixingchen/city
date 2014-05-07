package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2015;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1002;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

/**
 * 订单状态查询结果
 * @author cuipengfei
 *
 */
public class Handler2015 implements IHandler {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Handler2015.class);

	@Override
	public void doHandle(IMsg m) {
		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - start"); //$NON-NLS-1$
		}

		if (m instanceof Msg2015) {
			Msg2015 msg = (Msg2015) m;
			TcpClient.getInstance().sendAnsworMsg(msg);
			MQMsg1002 mqmsg = new MQMsg1002();
			mqmsg.setOrderId(msg.getHeader().getOrderid());
			mqmsg.setState(msg.getState());
			MQService.getInstance().sendMsg(mqmsg);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - end"); //$NON-NLS-1$
		}
	}

}

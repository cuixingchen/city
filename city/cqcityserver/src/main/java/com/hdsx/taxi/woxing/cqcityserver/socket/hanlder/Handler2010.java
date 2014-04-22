package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.util.coor.CoordinateCodec;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2005;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1004;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

/**
 * 车辆同步信息
 * 
 * @author Steven
 * 
 */
public class Handler2010 implements IHandler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Handler2010.class);

	@Override
	public void doHandle(IMsg m) {
		if (m.getClass().isInstance(Msg2005.class)) {
			Msg2005 msg = (Msg2005) m;
			TcpClient.getInstance().sendAnsworMsg(msg);

			MQMsg1004 mqmsg = new MQMsg1004();

			mqmsg.setCarNumber(msg.getCarNumber());
			mqmsg.setDriverid(msg.getCertificate());
			mqmsg.setPhone(msg.getPhone());
			mqmsg.setReasoncode(msg.getCause());
			mqmsg.setTime(msg.getBcdtime());

			MQService.getInstance().sendMsg(mqmsg);
		}

	}
}

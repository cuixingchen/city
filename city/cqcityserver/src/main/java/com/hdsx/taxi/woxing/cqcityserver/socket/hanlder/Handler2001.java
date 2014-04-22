package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.util.coor.CoordinateCodec;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2001;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1001;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

public class Handler2001 implements IHandler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Handler2001.class);

	@Override
	public void doHandle(IMsg m) {
		if (m.getClass().isInstance(Msg2001.class)) {
			Msg2001 msg = (Msg2001) m;
			TcpClient.getInstance().sendAnsworMsg(msg);

			MQMsg1001 mqmsg = new MQMsg1001();

			mqmsg.setNumber(msg.getCarNumber());
			mqmsg.setPhone(msg.getPhone());
			mqmsg.setDriverid(msg.getCertificate());
			mqmsg.setTime(msg.getBcdtime());
			mqmsg.setLat(CoordinateCodec.Coor2Float(msg.getLng()));
			mqmsg.setLon(CoordinateCodec.Coor2Float(msg.getLat()));
			MQService.getInstance().sendMsg(mqmsg);
		}

	}
}

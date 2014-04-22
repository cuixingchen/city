package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0002;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0003;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

/**
 * 心跳应答，回复通用信息
 * 
 * @author Steven
 * 
 */
public class Handler0002 implements IHandler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Handler0002.class);

	/**
	 * 
	 */
	@Override
	public void doHandle(IMsg m) {
		if (m.getClass().isInstance(Msg0002.class)) {
			Msg0002 msg = (Msg0002) m;
			Msg0003 mout = new Msg0003();
			mout.setMsgid(msg.getHeader().getMsgid());

			TcpClient.getInstance().sendWithoutCache(mout);

		}

	}
}

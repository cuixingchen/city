package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.order.OrderService;
import com.hdsx.taxi.woxing.cqcityserver.socket.MsgCache;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0003;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

public class Handler0003 implements IHandler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Handler0003.class);

	@Override
	public void doHandle(IMsg m) {
		if (m.getClass().isInstance(Msg0003.class)) {
			Msg0003 msg = (Msg0003) m;
			// 收到失败的消息应答时
			if (msg.getError() != 0) {
				logger.error("收到应答错误消息Msg0003：" + m.toString());
				return;
			}
			if (TcpClient.getInstance().isLogined()) {
//				if (msg.getMsgid() == MessageID.msg0x1001) { // 发送订单的回复需要获取返回的订单号
//					OrderService.getInstance().updateOrderId(msg);
//
//				}

				MsgCache.getInstance().remove(msg);
			} else {
				TcpClient.getInstance().setLogined(true);
				TcpClient.getInstance().startThreads();

			}

		}

	}
}

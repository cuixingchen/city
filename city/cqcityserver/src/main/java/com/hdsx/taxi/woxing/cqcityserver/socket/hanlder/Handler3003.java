package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.order.OrderService;
import com.hdsx.taxi.woxing.cqcityserver.socket.MsgCache;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
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

		if (m instanceof Msg3003) {
			Msg3003 msg = (Msg3003) m;

//			String key =  msg.getHeader().getSn()+";"+msg.getMsgid();
			
			// 收到失败的消息应答时
			if (msg.getError() != 0) {
				logger.error("收到应答错误消息Msg3003：" + m.toString());
				return;
			}

			if (msg.getMsgid()==MessageID.msg0x0002) {
				return ;
			}else if (msg.getMsgid()==MessageID.msg0x0001) {
				TcpClient.getInstance().loginOK(true);
			}else if (TcpClient.getInstance().isLogined()) {
				if (msg.getMsgid() == MessageID.msg0x1001) { // 发送订单的回复需要获取返回的订单号
					OrderService.getInstance().updateOrderId(msg);
				}
				MsgCache.getInstance().remove(String.valueOf(msg.getHeader().getSn()));
			}
			
			MsgCache.getInstance().remove(String.valueOf(msg.getHeader().getSn()));

			if (logger.isDebugEnabled()) {
				logger.debug("doHandle(AbsMsg) - msgId:"+msg.getMsgid()); //$NON-NLS-1$
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - end"); //$NON-NLS-1$
		}
	}
}

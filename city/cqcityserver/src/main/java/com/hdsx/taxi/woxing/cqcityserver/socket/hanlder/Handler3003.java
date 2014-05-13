package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.order.OrderService;
import com.hdsx.taxi.woxing.cqcityserver.socket.MsgCache;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg3003;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg0007;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1003;
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
			logger.debug("【0x3003】doHandle(IMsg) - start"); //$NON-NLS-1$
		}

		if (m instanceof Msg3003) {
			Msg3003 msg = (Msg3003) m;

			// String key = msg.getHeader().getSn()+";"+msg.getMsgid();

			// 收到失败的消息应答时
//			if (msg.getError() != 0) {
//				logger.error("收到应答错误消息Msg3003：" + msg.getErrorDesc()
//						+ " msgid:0x" +Integer.toHexString(msg.getMsgid()));
//				return;
//			}
			if (logger.isDebugEnabled()) {
				logger.debug("【0x3003】解析开始 - msgId:0x" + Integer.toHexString(msg.getMsgid())); //$NON-NLS-1$
			}
			if (msg.getMsgid() == MessageID.msg0x0002) {
				return;
			} else if (msg.getMsgid() == MessageID.msg0x0001) {
				TcpClient.getInstance().loginOK(true);
			} else if (TcpClient.getInstance().isLogined()) {
				if (msg.getMsgid() == MessageID.msg0x1001) { // 发送订单的回复需要获取返回的订单号
					logger.debug("1001发送订单收到通用应答：更新订单开始");
					OrderService.getInstance().updateOrderId(msg);
				}
				else if(msg.getMsgid()==MessageID.msg0x1002)  //乘客取消订单返回结果					
				{
					// 通知中心订单已经取消
					MQMsg1003 mqmsg = new MQMsg1003();
					mqmsg.setOrderId(msg.getHeader().getOrderid());
					mqmsg.setCancle(msg.getError());// 0:取消成,1:取消失败
//					mqmsg.setExplain("取消");
					MQService.getInstance().sendMsg(mqmsg);
					
				}
				else if(msg.getMsgid()==MessageID.msg0x1011)  //乘客上车返回结果					
				{
					// 乘客上车返回结果	
					MQMsg0007 mqmsg = new MQMsg0007();
					mqmsg.setOrderId(msg.getHeader().getOrderid());
					mqmsg.setCancle(msg.getError());// 0:取消成,1:取消失败
//					mqmsg.setExplain("取消");
					MQService.getInstance().sendMsg(mqmsg);
					
				}

			}

			MsgCache.getInstance().remove(msg);

		}

		if (logger.isDebugEnabled()) {
			logger.debug("【0x3003】doHandle(IMsg) - end"); //$NON-NLS-1$
		}
	}
}

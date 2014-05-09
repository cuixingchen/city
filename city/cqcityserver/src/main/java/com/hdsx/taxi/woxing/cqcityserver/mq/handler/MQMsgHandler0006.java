package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1014;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg0006;

/**
 * 乘客付款通知
 * @author cuipengfei
 *
 */
public class MQMsgHandler0006 implements IMQMsgHanlder {
	
	@Override
	public void dohandle(MQAbsMsg mqmsg) {

		MQMsg0006 msg = (MQMsg0006) mqmsg;
		Msg1014 out = new Msg1014();
		out.setPayment(msg.getCancle());
		out.setDesc(msg.getExplain());
		out.getHeader().setOrderid(msg.getOrderId());
		TcpClient.getInstance().send(out);
	}
}

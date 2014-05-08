package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1005;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1011;

/**
 * 乘客反馈
 * @author cuipengfei
 *
 */
public class MQMsgHandler1011 implements IMQMsgHanlder {
	
	@Override
	public void dohandle(MQAbsMsg mqmsg) {

		MQMsg1011 msg = (MQMsg1011) mqmsg;
		Msg1005 out = new Msg1005();
		out.setType(msg.getType());
		out.setContext(msg.getDesc());
		out.getHeader().setOrderid(msg.getOrderid());
		TcpClient.getInstance().send(out);
	}
}

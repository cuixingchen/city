package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1011;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1007;

/**
 * 乘客上车通知
 * @author cuipengfei
 *
 */
public class MQMsgHandler1007 implements IMQMsgHanlder {
	
	@Override
	public void dohandle(MQAbsMsg mqmsg) {

		MQMsg1007 msg = (MQMsg1007) mqmsg;
		Msg1011 out = new Msg1011();
		out.setBcdtime(msg.getTime());
		out.setLng(msg.getLon());
		out.setLng(msg.getLat());
		out.getHeader().setOrderid(msg.getOrderid());
		TcpClient.getInstance().send(out);
	}
}

package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1011;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1012;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1007;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1010;

/**
 * 乘客位置上传
 * @author cuipengfei
 *
 */
public class MQMsgHandler1010 implements IMQMsgHanlder {
	
	@Override
	public void dohandle(MQAbsMsg mqmsg) {

		MQMsg1010 msg = (MQMsg1010) mqmsg;
		Msg1012 out = new Msg1012();
		out.setBcdtime(msg.getTime());
		out.setLng(msg.getLon());
		out.setLng(msg.getLat());
		out.getHeader().setOrderid(msg.getOrderid());
//		out.setDesc("说明");
		TcpClient.getInstance().send(out);
	}
}

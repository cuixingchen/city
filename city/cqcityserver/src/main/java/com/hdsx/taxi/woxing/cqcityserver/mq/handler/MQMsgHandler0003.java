package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.cqcityserver.order.OrderService;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1002;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.PassengerInfo;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg0003;

/**
 * 乘客取消订单
 * 
 * @author Steven
 * 
 */
public class MQMsgHandler0003 implements IMQMsgHanlder {

	@Override
	public void dohandle(MQAbsMsg mqmsg) {
		MQMsg0003 msg = (MQMsg0003) mqmsg;

		Msg1002 m = new Msg1002();
		m.getHeader().setOrderid(msg.getOrderId());
		m.setCause(msg.getCausecode());

		PassengerInfo p = new PassengerInfo();
		p.setPassengerName(msg.getPassengerName());
		p.setPassengerPhone(msg.getPassengerPhone());
		p.setPassengerSex(msg.getPassengerSex());
		m.setPassenger(p);

		TcpClient.getInstance().send(m);

		OrderService.getInstance().cancel(msg.getOrderId());

	}
}

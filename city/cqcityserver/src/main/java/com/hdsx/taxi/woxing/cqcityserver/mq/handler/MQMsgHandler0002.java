package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.cqcityserver.order.OrderService;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1002;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.PassengerInfo;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg0002;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg0003;

/**
 * 查询订单状态
 * @author cuipengfei
 *
 */
public class MQMsgHandler0002 implements IMQMsgHanlder {

	@Override
	public void dohandle(MQAbsMsg mqmsg) {
		MQMsg0002 msg = (MQMsg0002) mqmsg;
		OrderService.getInstance().getorderState(msg);

		
	}
}

package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import java.text.SimpleDateFormat;

import com.hdsx.taxi.woxing.bean.util.coor.CoordinateCodec;
import com.hdsx.taxi.woxing.cqcityserver.order.OrderContants;
import com.hdsx.taxi.woxing.cqcityserver.order.OrderService;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1001;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.OrderInfo;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.PassengerInfo;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg0001;

public class MQMsgHandler0001 implements IMQMsgHanlder {

	@Override
	public void dohandle(MQAbsMsg mqmsg) {
		MQMsg0001 msg = (MQMsg0001) mqmsg;

		// 第一步生成订单号

		Msg1001 out = new Msg1001();
		OrderInfo oi = new OrderInfo();

		oi.setOrderType(msg.getTakeTaxiType());
		oi.setTips(msg.getTips());
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		oi.setUseTime(df.format(msg.getGetOnTime()));
		oi.setAddress(msg.getGetOnPlaceName());
		oi.setUseLat(msg.getGetOnLat());
		oi.setUseLng(msg.getGetOnLon());

		oi.setDestination(msg.getGetOffPlaceName());
		oi.setDestLat(msg.getGetOffLat());
		oi.setDestLng(msg.getGetOffLon());
		oi.setRemark(msg.getNotes());

		PassengerInfo pi = new PassengerInfo();
		pi.setPassengerName(msg.getNickName());
		pi.setPassengerPhone(msg.getUserphone());
		pi.setPassengerSex(msg.getSex());

		oi.setPassengerInfo(pi);
		out.setOrder(oi);
		out.setRadius(OrderContants.CALLTAXI_NOTSELF_RAD);

		TcpClient.getInstance().send(out);

		OrderService.getInstance().put(out);

	}

}

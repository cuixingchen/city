package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import com.hdsx.taxi.woxing.bean.CarInfo;
import com.hdsx.taxi.woxing.location.LocationService;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.location.MQMsg2002;
import com.hdsx.taxi.woxing.mqutil.message.location.MQMsg3002;

public class MQMsgHandler2002 implements IMQMsgHanlder {
	
	@Override
	public void dohandle(MQAbsMsg mqmsg) {
		MQMsg2002 msg = (MQMsg2002) mqmsg;
		MQMsg3002 m2=new MQMsg3002(msg.getHead().getCustomId());
		CarInfo car = LocationService.getInstance()
				.getCarInfoByCarNum(msg.getCarnum());
		if (car == null) {
			// TODO 没有车辆的情况，需要和中心平台确定接口
		} else {
			m2.setLat(car.getLat());
			m2.setLon(car.getLon());
			m2.setCar_number(msg.getCarnum());
		}
		
		MQService.getInstance().sendMsg(m2);
	}

}

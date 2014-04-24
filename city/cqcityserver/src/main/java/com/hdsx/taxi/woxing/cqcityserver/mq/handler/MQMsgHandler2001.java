package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import java.util.List;

import com.hdsx.taxi.woxing.bean.CarInfo;
import com.hdsx.taxi.woxing.location.LocationService;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.location.MQMsg2001;
import com.hdsx.taxi.woxing.mqutil.message.location.MQMsg3001;

public class MQMsgHandler2001 implements IMQMsgHanlder {
	
	@Override
	public void dohandle(MQAbsMsg mqmsg) {
		MQMsg2001 msg = (MQMsg2001) mqmsg;
		List<CarInfo> list = LocationService.getInstance().getEmptycarByDistance(msg.getLon(),msg.getLat(),msg.getRange());
		MQMsg3001 m1=new MQMsg3001(msg.getHead().getCustomId());
		m1.setCars(list);
		MQService.getInstance().sendMsg(m1);
	}

}

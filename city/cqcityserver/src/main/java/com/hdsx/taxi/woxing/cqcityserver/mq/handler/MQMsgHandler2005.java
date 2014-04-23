package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.TaxiIndex;
import com.hdsx.taxi.woxing.location.distributeservice.TaxiDistrbuteService;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.location.MQMsg2005;
import com.hdsx.taxi.woxing.mqutil.message.location.MQMsg3005;

public class MQMsgHandler2005 implements IMQMsgHanlder {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MQMsgHandler2005.class);
	
	
	
	@Override
	public void dohandle(MQAbsMsg mqmsg) {
		if(mqmsg instanceof MQMsg2005){
			MQMsg2005 msg = (MQMsg2005) mqmsg;
			List<TaxiIndex> rl = TaxiDistrbuteService.getInstance().getTaxiIndex(msg.getLat(),msg.getLon(),msg.getDlat(),msg.getDlon());
			
			MQMsg3005 m2 = new MQMsg3005(msg.getHead().getCustomId());
			m2.setList(rl);
			
			MQService.getInstance().sendMsg(m2);
			
		}else{
			logger.warn("打车热点区域转换失败!!未知原因");
		}
		
	}

}

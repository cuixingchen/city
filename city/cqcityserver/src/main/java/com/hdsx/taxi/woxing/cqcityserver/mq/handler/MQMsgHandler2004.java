package com.hdsx.taxi.woxing.cqcityserver.mq.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.location.distributeservice.TaxiDistrbuteService;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.MQAbsMsg;
import com.hdsx.taxi.woxing.mqutil.message.handle.IMQMsgHanlder;
import com.hdsx.taxi.woxing.mqutil.message.location.MQMsg2004;
import com.hdsx.taxi.woxing.mqutil.message.location.MQMsg3004;

public class MQMsgHandler2004 implements IMQMsgHanlder {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MQMsgHandler2004.class);
	
	
	
	@Override
	public void dohandle(MQAbsMsg mqmsg) {
		if(mqmsg instanceof MQMsg2004){
			MQMsg2004 msg = (MQMsg2004) mqmsg;
			if(msg.getLat()>0&&msg.getLon()>0){
				int result = TaxiDistrbuteService.getInstance().getCurLocationIndex(msg.getLon(), msg.getLat());
				MQMsg3004 m2 = new MQMsg3004(msg.getHead().getCustomId());
				m2.setResult(result);
				MQService.getInstance().sendMsg(m2);
			}else{
				logger.warn("打车指数计算失败!!经纬度参数不正确,其中获取到的参数为:经度lon[" + msg.getLon()
						+ "],纬度lat[" + msg.getLat() + "].请检查");
			}
			
		}else{
			logger.warn("打车指数消息转换失败!!未知原因");
		}
		
	}

}

package com.hdsx.taxi.woxing.cityservice.service;

import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg4001;

/*******************************************************************************
 * <b>类名:IComplaintsService</b> <br/>
 * 功能：投诉内容<br/>
 * 日期: 2013年8月30日<br/>
 * 
 * @author 谢广泉 xiegqooo@gmail.com
 * @version 1.0.0
 * 
 ******************************************************************************/
public interface IComplaintsService {

	/**
	 * 
	 * 方法名：doComplaints <br/>
	 * 编写人：谢广泉<br/>
	 * 日期：2013年8月31日<br/>
	 * 功能描述：<br/>
	 * 		发送投诉信息给奇华
	 * @param mqmsg
	 */
	void doComplaints(MQMsg4001 mqmsg);

}

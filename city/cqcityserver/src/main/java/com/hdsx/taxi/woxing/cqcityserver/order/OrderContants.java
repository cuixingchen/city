package com.hdsx.taxi.woxing.cqcityserver.order;

import java.io.IOException;
import java.util.Properties;

/**
 * 订单相关的参数 #是否自主叫车 calltaxi.self=true #非自主叫车时的用车半径(米) calltaxi.notself.rad=5000
 * #确定中标车辆策略配置 #订单下发最短响应时间 calltaxi.minwaitingtime=60 #最小叫车范围
 * calltaxi.rad.min=1000 #最大叫车范围 calltaxi.rad.max=5000 #每次增加的距离
 * calltaxi.rad.step=1000
 * TODO 完成读取calltaxi.properties文件
 * @author Steven
 * 
 */
public class OrderContants {
	
	public static boolean IS_SELF ="true".equals(getInstance().getProperty("calltaxi.self"));
	public static int CALLTAXI_NOTSELF_RAD = Integer.parseInt(getInstance().getProperty("calltaxi.notself.rad"));
	public static int CALLTAXI_MINWAITINGTIME = Integer.parseInt(getInstance().getProperty("calltaxi.minwaitingtime"));
	public static int CALLTAXI_RAD_MIN = Integer.parseInt(getInstance().getProperty("calltaxi.rad.min"));
	public static int CALLTAXI_RAD_MAX = Integer.parseInt(getInstance().getProperty("calltaxi.rad.max"));
	public static int CALLTAXI_RAD_STEP = Integer.parseInt(getInstance().getProperty("calltaxi.rad.step"));
	public static int CALLTAXI_ORDER_MAXCARS = Integer.parseInt(getInstance().getProperty("calltaxi.order.maxcars"));
//	public static int CAL_TAXI_INDEX_DELAY = Integer.parseInt(getInstance().getProperty("CAL_TAXI_INDEX_DELAY"));
	public static int CALLTAXI_ORDER_DRIVERS = Integer.parseInt(getInstance().getProperty("calltaxi.order.drivers"));
	
//	public static boolean IS_SELF = true;
//	public static int CALLTAXI_NOTSELF_RAD = 5000;
//	public static int CALLTAXI_MINWAITINGTIME = 120;
//	public static int CALLTAXI_RAD_MIN = 1000;
//	public static int CALLTAXI_RAD_MAX = 1000;
//	public static int CALLTAXI_RAD_STEP = 1000;
//	public static int CALLTAXI_ORDER_MAXCARS = 50;
//	public static int CAL_TAXI_INDEX_DELAY = 600;

	static Properties pro;
	static Properties getInstance(){
		if(pro==null){
			pro = new Properties();
			try {
				pro.load(OrderContants.class.getResourceAsStream("/calltaxi.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pro;
		
	}
	
}

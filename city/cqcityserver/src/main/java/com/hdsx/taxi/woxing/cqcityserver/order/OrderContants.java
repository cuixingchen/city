package com.hdsx.taxi.woxing.cqcityserver.order;

/**
 * 订单相关的参数 #是否自主叫车 calltaxi.self=true #非自主叫车时的用车半径(米) calltaxi.notself.rad=5000
 * #确定中标车辆策略配置 #订单下发最短响应时间 calltaxi.minwaitingtime=60 #最小叫车范围
 * calltaxi.rad.min=1000 #最大叫车范围 calltaxi.rad.max=5000 #每次增加的距离
 * calltaxi.rad.step=1000
 * 
 * @author Steven
 * 
 */
public class OrderContants {
	public static boolean IS_SELF = true;
	public static int CALLTAXI_NOTSELF_RAD = 5000;
	public static int CALLTAXI_MINWAITINGTIME = 60;
	public static int CALLTAXI_RAD_MIN = 1000;
	public static int CALLTAXI_RAD_MAX = 1000;
	public static int CALLTAXI_RAD_STEP = 1000;
	
	public static int CALLTAXI_ORDER_MAXCARS =50;

}

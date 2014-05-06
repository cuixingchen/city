package com.hdsx.taxi.woxing.cqcityserver.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.CarInfo;
import com.hdsx.taxi.woxing.bean.util.CacheManagerUtil;
import com.hdsx.taxi.woxing.cqcityserver.order.util.BidComparator;
import com.hdsx.taxi.woxing.cqcityserver.order.util.CarDistanceComparator;
import com.hdsx.taxi.woxing.cqcityserver.socket.MsgCache;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1002;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1003;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1004;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1015;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1101;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2005;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg3003;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.OrderInfo;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.PassengerInfo;
import com.hdsx.taxi.woxing.location.LocationService;
import com.hdsx.taxi.woxing.mqutil.MQService;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg0002;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg0003;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1001;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1004;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1008;
import com.hdsx.taxi.woxing.mqutil.message.order.MQMsg1009;

/**
 * 订单服务
 * 
 * @author Steven
 * 
 */
public class OrderService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(OrderService.class);

	static OrderService obj;
	Ehcache orderpool; // 订单缓存

	private Timer timer;


	public static OrderService getInstance() {
		if (obj == null)
			obj = new OrderService();
		return obj;
	}

	public OrderService() {

		this.orderpool = CacheManagerUtil.getInstance().getCm()
				.getEhcache("orderpoolcache");

	}

	public void put(Msg1001 msg) {

		 OrderObject o = new OrderObject();
		 o.setOrder(msg.getOrder());
		 this.orderpool.put(new Element(msg.getOrder().getOrderid(), o));

	}

	/**
	 * 根据通用应答更新订单号
	 * 
	 * @param msg
	 */
	public void updateOrderId(Msg3003 msg) {

		/**
		 * 在历史消息中找到订单信息
		 */
		AbsMsg m = MsgCache.getInstance().getMsg(msg.getHeader().getSn()+";"+msg.getMsgid());
		if (m == null)
			return;
		Msg1001 m1 = (Msg1001) m;
		OrderObject o = new OrderObject();
		OrderInfo oi = m1.getOrder();
		long orderid_old = oi.getOrderid();
		long orderid_new = msg.getHeader().getOrderid();
		logger.info("new order id"+orderid_new);

		oi.setOrderid(orderid_new);
		o.setOrder(oi);
		this.orderpool.remove(orderid_old);
		this.orderpool.put(new Element(oi.getOrderid(), o)); // 将订单信息存入缓存

		// 发送订单号变更消息到总中心
		MQMsg1008 mqmsg = new MQMsg1008();
		mqmsg.setOldid(orderid_old);
		mqmsg.setNewid(orderid_new);
//		mqmsg.setNewid(new Date().getTime());
		mqmsg.getHead().setCustomId("cuipengfei");
		logger.info("mq更新订单号发送开始");
		MQService.getInstance().sendMsg(mqmsg);
		logger.info("mq更新订单发送结束");
		

		// 开始执行订单流程
		if (OrderContants.IS_SELF) {
			doWithOrder(oi);
		} else {
			doWithorder2(oi);
		}
	}

	/**
	 * 找车流程,平台招车
	 * 
	 * @param oi
	 */
	private void doWithorder2(OrderInfo oi) {

		OrderObject o = new OrderObject();
		o.setOrder(oi);
		this.orderpool.put(new Element(oi.getOrderid(), o));
	}

	/**
	 * 找车流程,自主招车
	 * 
	 * @param oi
	 */
	void doWithOrder(OrderInfo oi) {

		// 第一步找到合适的车辆
		List<CarInfo> l = getTaxi(oi);

		// 找不到车的情况
		if (l.size() == 0) {
			MQMsg1009 m = new MQMsg1009();
			m.getHead().setCustomId("customId");
			m.setOrderid(oi.getOrderid());
			m.setReasoncode((byte) 0);
			m.setDescribtion("没有找到空车");
			MQService.getInstance().sendMsg(m);
			orderpool.remove(oi.getOrderid());
			return;
		}

		// 向目标车辆发送抢单信息
		Msg1101 m = new Msg1101();
		m.setOrder(oi);
		m.getHeader().setOrderid(oi.getOrderid());
		m.setCount((short) l.size());
		List<String> cars = new ArrayList<String>();
		for (CarInfo c : l) {
//			cars.add(c.getLisencenumber());
			cars.add(c.getId());
		}
		m.setCarNumbers(cars);
		TcpClient.getInstance().send(m);

		/**
		 * 等待一会看看是否有接收
		 */
		try {
//			logger.info("doWithOrder(sleep) - =" + Thread.class); //$NON-NLS-1$
			Thread.sleep(OrderContants.CALLTAXI_MINWAITINGTIME * 1000l/4);
		} catch (InterruptedException e) {
			logger.error("等待出错：" + e);
		}
		Element e = orderpool.get(oi.getOrderid());
		if (e == null) // 表示已经被处理
			return;
		OrderObject o = (OrderObject) e.getObjectValue();
		/**
		 * 无车应答时
		 */
		if (o.getDrivers().size() == 0) {
			MQMsg1009 mq = new MQMsg1009();
			mq.setOrderid(oi.getOrderid());
			mq.getHead().setCustomId("customId");
			mq.setReasoncode((byte) 1);
			mq.setDescribtion("没有司机抢单");
			MQService.getInstance().sendMsg(mq);
			orderpool.remove(oi.getOrderid());
			return;
		}
		doSucess(o);
//		timer = new Timer();
//		logger.info("Timer开始");
//		timer.schedule(new DoResult(orderpool, oi), OrderContants.CALLTAXI_MINWAITINGTIME * 1000l/2, OrderContants.CALLTAXI_MINWAITINGTIME * 1000l*2);//在1秒后执行此任务,每次间隔2秒,如果传递一个Data参数,就可以在某个固定的时间执行这个任务.
	}

	class DoResult extends  java.util.TimerTask{

		private Ehcache orderpool;
		private OrderInfo oi;

		public DoResult(Ehcache orderpool, OrderInfo oi) {
			super();
			this.orderpool = orderpool;
			this.oi = oi;
		}

		@Override
		public void run() {
//			try {
//				Thread.sleep(OrderContants.CALLTAXI_MINWAITINGTIME * 1000l);
//			} catch (InterruptedException e) {
//				logger.error("等待出错：" + e);
//			}
			logger.info("DoResult:run");
			Element e = orderpool.get(oi.getOrderid());
			if (e == null) // 表示已经被处理
				return;
			OrderObject o = (OrderObject) e.getObjectValue();
//			Ehcache orderpool1=CacheManagerUtil.getInstance().getCm()
//			.getEhcache("orderpoolcache");
//
//			Element e1 = orderpool1.get(oi.getOrderid());
//			if (e1 == null) // 表示已经被处理
//				return;
//			OrderObject o1 = (OrderObject) e1.getObjectValue();
			
			/**
			 * 无车应答时
			 */
			if (o.getDrivers().size() == 0) {
				MQMsg1009 mq = new MQMsg1009();
				mq.setOrderid(oi.getOrderid());
				mq.getHead().setCustomId("customId");
				mq.setReasoncode((byte) 1);
				mq.setDescribtion("没有司机抢单");
				MQService.getInstance().sendMsg(mq);
				orderpool.remove(oi.getOrderid());
				return;
			}

			doSucess(o);
			timer.cancel();
		}
		
	}
	
	
	
	/**
	 * 处理成功的订单对象，包括想司机发送消息和通知乘客
	 * 
	 * @param o
	 */
	private void doSucess(OrderObject o) {

		if (o.getState() != 0) // 订单已经被乘客取消的情况
		{
			this.orderpool.remove(o.getOrder().getOrderid());
			return;
		}

		List<Msg2001> list = o.getDrivers();

		Collections.sort(list, new BidComparator(o.getOrder()));

		Msg2001 m = list.get(0);
		// //向中标司机发送成功消息
		Msg1003 msg_sucess = new Msg1003();
		msg_sucess.getHeader().setOrderid(o.getOrder().getOrderid());
		msg_sucess.setCarNumber(m.getCarNumber());
		TcpClient.getInstance().send(msg_sucess);

		// 通知乘客
		MQMsg1001 msg_p = new MQMsg1001();

		msg_p.setOrderId(o.getOrder().getOrderid());
		msg_p.setNumber(m.getCarNumber());
		msg_p.setPhone(m.getPhone());

		MQService.getInstance().sendMsg(msg_p);
		// msg_p.setName(m.get);

		// 向未中标司机发送未中标消息

		for (int i = 1; i < list.size(); i++) {
			m = list.get(i);
			Msg1004 msg_fa = new Msg1004();
			msg_fa.getHeader().setOrderid(o.getOrder().getOrderid());
			msg_fa.setCarNumber(m.getCarNumber());
			msg_fa.setError((byte) 1);
			msg_fa.setErrorDesc("该订单已经被抢");
			TcpClient.getInstance().send(m);
		}
		this.orderpool.remove(o.getOrder().getOrderid());

	}

	/**
	 * 根据订单找到合适的车辆
	 * 
	 * @param oi
	 * @return
	 */

	List<CarInfo> getTaxi(OrderInfo oi) {
		int rad = OrderContants.CALLTAXI_RAD_MIN;

		List<CarInfo> l = LocationService.getInstance()
				.getEmptycarByDistance(oi.getUseLng(), oi.getUseLat(),
						OrderContants.CALLTAXI_RAD_MIN);
		/**
		 * 循环找车，直到找到车
		 */
		while (l.size() <= OrderContants.CALLTAXI_ORDER_MAXCARS
				&& rad <= OrderContants.CALLTAXI_RAD_MAX) {
			rad += OrderContants.CALLTAXI_RAD_STEP;
			l = LocationService.getInstance().getEmptycarByDistance(
					oi.getUseLng(), oi.getUseLat(),
					OrderContants.CALLTAXI_RAD_MIN);
		}

		if (l == null)
			return new ArrayList<CarInfo>();

		if (l.size() <= OrderContants.CALLTAXI_ORDER_MAXCARS) // 小于设定的最大值，就直接返回
			return l;

		/**
		 * 排序，找最近的车辆发送
		 */

		Collections.sort(l, new CarDistanceComparator(oi));

		return l.subList(0, OrderContants.CALLTAXI_ORDER_MAXCARS - 1);
	}

	/**
	 * 司机抢单
	 * 
	 * @param msg
	 */
	public void onDriverAnswer(Msg2001 msg) {

		long orderid = msg.getHeader().getOrderid();
		Element e = this.orderpool.get(orderid);
		// 找不到，表示已经被抢
		if (e == null) {
			Msg1004 m = new Msg1004();
			m.getHeader().setOrderid(orderid);
			m.setCarNumber(msg.getCarNumber());
			m.setError((byte) 1);
			m.setErrorDesc("该订单已经被抢");
			TcpClient.getInstance().send(m);
			return;
		}else{
			OrderObject o = (OrderObject) e.getObjectValue();
			o.getDrivers().add(msg);
			if (o.getDrivers().size() > OrderContants.CALLTAXI_ORDER_MAXCARS){
				Msg1004 m = new Msg1004();
				m.getHeader().setOrderid(orderid);
				m.setCarNumber(msg.getCarNumber());
				m.setError((byte) 1);
				m.setErrorDesc("该订单已经被抢");
				TcpClient.getInstance().send(m);
				return;
			}
			
			this.orderpool.replace(e);
			
//			this.orderpool.remove(orderid);
//			this.orderpool.put(new Element(orderid, o));

			if (o.getDrivers().size() >= OrderContants.CALLTAXI_ORDER_MAXCARS) // 当抢单司机比较多时直接处理
			{
				doSucess(o);
			}
		}
	}

	/**
	 * 乘客取消订单
	 * 
	 * @param msg
	 */
	public void cancelByPasssenger(MQMsg0003 msg) {

		long oid = msg.getOrderId();

		Element e = this.orderpool.get(oid);

		if (e != null) // 表示订单正在处理过程中
		{

			OrderObject o = (OrderObject) e.getObjectValue();
			o.setState((byte) 1);
			this.orderpool.put(e);
		} else { // 表示订单处理完成，提交到电招中心取消订单

			Msg1002 m = new Msg1002();
			m.getHeader().setOrderid(msg.getOrderId());
			m.setCause(msg.getCausecode());

			PassengerInfo p = new PassengerInfo();
			p.setPassengerName(msg.getPassengerName());
			p.setPassengerPhone(msg.getPassengerPhone());
			p.setPassengerSex(msg.getPassengerSex());
			m.setPassenger(p);

			TcpClient.getInstance().send(m);

		}

	}

	/**
	 * 取消订单
	 * 
	 * @param orderId
	 */
	public void cancel(long orderId) {
		this.orderpool.remove(orderId);
	}

	/**
	 * 订单被驾驶员取消
	 * 
	 * @param msg
	 */
	public void cancelByDriver(Msg2005 msg) {
		long oid = msg.getHeader().getOrderid();

		Element e = this.orderpool.get(oid);

		if (e != null) // 表示订单正在处理过程中
		{

			OrderObject o = (OrderObject) e.getObjectValue();
			o.setState((byte) 2);
			this.orderpool.put(e);
		}

		else {
			/**
			 * 表示订单已经处理完成
			 */

			MQMsg1004 mqmsg = new MQMsg1004();

			mqmsg.setCarNumber(msg.getCarNumber());
			mqmsg.setDriverid(msg.getCertificate());
			mqmsg.setPhone(msg.getPhone());
			mqmsg.setReasoncode(msg.getCause());
			mqmsg.setTime(msg.getBcdtime());
			mqmsg.setOrderid(msg.getHeader().getOrderid());
			MQService.getInstance().sendMsg(mqmsg);
		}
	}
	
	/**
	 * 查询订单状态
	 * @param msg
	 */
	public void getorderState(MQMsg0002 mqmsg){
		long orderId=mqmsg.getOrderId();
//		Element e = this.orderpool.get(orderId);
//		if (e != null) // 表示订单正在处理过程中
//		{
//
//			OrderObject o = (OrderObject) e.getObjectValue();
//			o.setState((byte) 2);
//			this.orderpool.put(e);
//		}else{
//			
//		}
		Msg1015 msg=new Msg1015();
		msg.getHeader().setOrderid(orderId);
		TcpClient.getInstance().send(msg);
	}

	/**
	 * 订单对象实体，加入了司机抢单存储
	 * 
	 * @author Steven
	 * 
	 */
	class OrderObject {
		OrderInfo order;
		List<Msg2001> drivers = new ArrayList<>();
		byte state = 0; // 状态 0为正常，1为被乘客取消，2为被驾驶员取消

		public byte getState() {
			return state;
		}

		public void setState(byte state) {
			this.state = state;
		}

		public OrderInfo getOrder() {
			return order;
		}

		public void setOrder(OrderInfo order) {
			this.order = order;
		}

		public List<Msg2001> getDrivers() {
			return drivers;
		}

		public void setDrivers(List<Msg2001> drivers) {
			this.drivers = drivers;
		}

	}

}

package com.hdsx.taxi.woxing.cqcityserver;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.MessageID;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0002;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg0003;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1001;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1002;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1003;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1004;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1005;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1006;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1007;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1010;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1011;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1012;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1013;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1014;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1015;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1016;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg1101;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.OrderInfo;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.PassengerInfo;

public class TcpClientTest {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TcpClientTest.class);
	private static String address = "address";
	private static String destination = "destination";
	private static String remark = "remark";
	private static String passengerName = "张大少";
	private static String passengerPhone = "15811276372";
	private static String carNumber = "33333333";
	private static String errorDesc = "errorDesc";
	private static String context = "context";
	private static String sex = "1";
	private static String bcdtime = "20140507094022";//用车时间 BCD[7] yyyymmddHHmmss
	private static long orderid = 0x12121245;
	private static double destLat = 39.967020;
	private static double destLng = 116.358650;
	private static double useLat = 39.967020;
	private static double useLng = 116.358650;
	private static int lat = 39967020;
	private static int lng = 116358650;
	private static int radius = 1000;
	private static byte orderType=1;// 订车类型 byte 0 即时订单、1预约订单;
	private static byte passengerSex = 1;
	private static byte cause = 1;
	private static byte error = 1;
	private static byte type = 1;
	private static byte payment = 1;
	private static byte state = 1;
	private static List<String> carNumbers = new ArrayList<>();
	private static short tips = 23;
	private static short count = 23;
	private static short errorNumber = 1;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}
		
		PassengerInfo passengerInfo = new PassengerInfo();
		passengerInfo.setPassengerName(passengerName);
		passengerInfo.setPassengerPhone(passengerPhone);
		passengerInfo.setPassengerSex(passengerSex);

		OrderInfo order = new OrderInfo();
		order.setAddress(address);
		order.setDestination(destination);
		order.setDestLat(destLat);
		order.setDestLng(destLng);
		order.setOrderid(orderid);
		order.setOrderType(orderType);
		order.setPassengerInfo(passengerInfo);
		order.setRemark(remark);
		order.setTips(tips);
		order.setUseLat(useLat);
		order.setUseLng(useLng);
		order.setUseTime(bcdtime);

		carNumbers.add("00000000");
		carNumbers.add("00000001");
		carNumbers.add("00000002");
		carNumbers.add("00000003");

		TcpClient.getInstance().run();
		int i = 0;
		while (true) {
			i++;
			if (logger.isDebugEnabled()) {
				logger.debug("while(true) - i:"+i); //$NON-NLS-1$
			}
			logger.info("请输入消息id：");
			logger.info("msg0x0001：1");
			logger.info("msg0x0002：2");
			logger.info("msg0x0003：3");
			logger.info("msg0x1001：4097");
			logger.info("msg0x1002：4098");
			logger.info("msg0x1003：4099");
			logger.info("msg0x1004：4100");
			logger.info("msg0x1005：4101");
			logger.info("msg0x1006：4102");
			logger.info("msg0x1007：4103");
			logger.info("msg0x1010：4112");
			logger.info("msg0x1011：4113");
			logger.info("msg0x1012：4114");
			logger.info("msg0x1013：4115");
			logger.info("msg0x1014：4116");
			logger.info("msg0x1015：4117");
			logger.info("msg0x1016：4118");
			logger.info("msg0x1101：4353");
			Scanner in = new Scanner(System.in);
			int msgID = in.nextInt();
			AbsMsg m = null;
			switch (msgID) {
			case MessageID.msg0x0001:
				m = new Msg0001();
				break;
			case MessageID.msg0x0002:
				m = new Msg0002();
				break;
			case MessageID.msg0x0003:
				m = new Msg0003();
				break;
			case MessageID.msg0x1001:
				Msg1001 msg1001 = new Msg1001();
				msg1001.setOrder(order);
				msg1001.setRadius(radius);
				m = msg1001;
				break;
			case MessageID.msg0x1002:
				Msg1002 msg1002 = new Msg1002();
				msg1002.setCarNubmer(carNumber);
				msg1002.setCause(cause);
				msg1002.setPassenger(passengerInfo);
				m = msg1002;
				break;
			case MessageID.msg0x1003:
				Msg1003 msg1003 = new Msg1003();
				msg1003.setCarNumber(carNumber);
				m = msg1003;
				break;
			case MessageID.msg0x1004:
				Msg1004 msg1004 = new Msg1004();
				msg1004.setCarNumber(carNumber);
				msg1004.setError(error);
				msg1004.setErrorDesc(errorDesc);
				m = msg1004;
				break;
			case MessageID.msg0x1005:
				Msg1005 msg1005 = new Msg1005();
				msg1005.setContext(context);
				msg1005.setType(type);
				m = msg1005;
				break;
			case MessageID.msg0x1006:
				Msg1006 msg1006 = new Msg1006();
				msg1006.setName(passengerName);
				msg1006.setPhone(passengerPhone);
				msg1006.setSex(sex);
				m = msg1006;
				break;
			case MessageID.msg0x1007:
				Msg1007 msg1007 = new Msg1007();
				msg1007.setErrorDesc(errorDesc);
				msg1007.setErrorNumber(errorNumber);
				m = msg1007;
				break;
			case MessageID.msg0x1010:
				Msg1010 msg1010 = new Msg1010();
				m = msg1010;
				break;
			case MessageID.msg0x1011:
				Msg1011 msg1011 = new Msg1011();
				msg1011.setBcdtime(bcdtime);
				msg1011.setLat(lat);
				msg1011.setLng(lng);
				m = msg1011;
				break;
			case MessageID.msg0x1012:
				Msg1012 msg1012 = new Msg1012();
				msg1012.setBcdtime(bcdtime);
				msg1012.setDesc(errorDesc);
				msg1012.setLat(lat);
				msg1012.setLng(lng);
				m = msg1012;
				break;
			case MessageID.msg0x1013:
				Msg1013 msg1013 = new Msg1013();
				msg1013.setContext(context);
				m = msg1013;
				break;
			case MessageID.msg0x1014:
				Msg1014 msg1014 = new Msg1014();
				msg1014.setDesc(errorDesc);
				msg1014.setPayment(payment);
				m = msg1014;
				break;
			case MessageID.msg0x1015:
				Msg1015 msg1015 = new Msg1015();
				m = msg1015;
				m.getHeader().setOrderid(66);
				break;
			case MessageID.msg0x1016:
				Msg1016 msg1016 = new Msg1016();
				msg1016.setLat(lat);
				msg1016.setLng(lng);
				msg1016.setRadius((short)radius);
				msg1016.setState(state);
				m = msg1016;
				break;
			case MessageID.msg0x1101:
				Msg1101 msg1101 = new Msg1101();
				msg1101.setCarNumbers(carNumbers);
				msg1101.setCount(count);
				msg1101.setOrder(order);
				m = msg1101;
				break;
			default:
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("main(String[])", e); //$NON-NLS-1$

				e.printStackTrace();
			}
			TcpClient.getInstance().send(m);
			
		}
	}
}

package com.hdsx.taxi.woxing.cqmsg;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.hdsx.taxi.woxing.bean.util.coor.CoordinateCodec;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2010;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.DriverInfo;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.TaxiPointInfo;

public class Msg2010Test extends TestCase {

	public void testToBytes() {
		Msg2010 msg = new Msg2010();

		List<TaxiPointInfo> lst = new ArrayList<>();

		for (int i = 0; i < 1; i++) {
			TaxiPointInfo ti = new TaxiPointInfo();
			DriverInfo d = new DriverInfo();
			d.setCompany("重庆某某公司");
			d.setCreditLevel((byte) 3);
			d.setDriverName("司机AXX");
			d.setDriverPhone("13810195316");
			d.setDriverSerial("510129198001232511");
			d.setLicenseNumber("渝A1234" + i);
			ti.setDriver(d);
			double lat = 116.343242;
			ti.setLat(lat);
			double lon = 39.234234;
			ti.setLon(lon);
			ti.setState((byte) 0);
			lst.add(ti);
		}

		msg.setList(lst);
		byte[] b = msg.toBytes();
		System.out.println(b.length);
		byte[] bytes = new byte[b.length - 2];
		System.arraycopy(b, 1, bytes, 0, b.length - 2);
		Msg2010 msg1 = new Msg2010();
		msg1.fromBytes(bytes);
		System.out.println(msg1.toString());

	}

}

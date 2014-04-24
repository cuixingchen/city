package com.hdsx.taxi.woxing.cqcityserver.order.util;

import java.util.Comparator;

import com.hdsx.taxi.woxing.bean.CarInfo;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.OrderInfo;


/**
 * 根据距离排序
 * @author Steven
 *
 */
public class CarDistanceComparator implements Comparator<CarInfo> {

	private OrderInfo oi;

	public CarDistanceComparator(OrderInfo oi) {
		this.oi = oi;
	}

	@Override
	public int compare(CarInfo c1, CarInfo c2) {

		double d1 = dis(c1, oi);
		double d2 = dis(c2, oi);

		if (d1 < d2)
			return -1;
		else if (d1 > d2)
			return 1;
		else
			return 0;

	}

	double dis(CarInfo c, OrderInfo o) {
		double dx = c.getLat() - o.getUseLat();
		double dy = c.getLon() - o.getUseLng();
		return Math.sqrt(dx * dx + dy * dy);
	}
}

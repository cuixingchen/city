package com.hdsx.taxi.woxing.cqcityserver.order.util;

import java.util.Comparator;

import com.hdsx.taxi.woxing.cqmsg.msg.Msg2001;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.OrderInfo;


/**
 * 司机应答的抢单规则
 * @author Steven
 *
 */
public class BidComparator implements Comparator<Msg2001> {

	private OrderInfo oi;

	public BidComparator(OrderInfo oi) {
		this.oi = oi;
	}

	@Override
	public int compare(Msg2001 c1, Msg2001 c2) {
		double d1 = dis(c1, oi);
		double d2 = dis(c2, oi);

		if (d1 < d2)
			return -1;
		else if (d1 > d2)
			return 1;
		else
			return 0;

	}

	private double dis(Msg2001 c, OrderInfo o) {
		double dx = c.getLat() - o.getUseLat();
		double dy = c.getLng() - o.getUseLng();
		return Math.sqrt(dx * dx + dy * dy);

	}

}

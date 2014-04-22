package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.CarInfo;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2010;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2010.TaxiInfo;
import com.hdsx.taxi.woxing.location.LocationService;
import com.hdsx.taxi.woxing.location.distributeservice.TaxiDistrbuteService;
import com.hdsx.taxi.woxing.nettyutil.msg.IMsg;
import com.hdsx.taxi.woxing.nettyutil.msghandler.IHandler;

/**
 * 车辆同步信息
 * 
 * @author Steven
 * 
 */
public class Handler2010 implements IHandler {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(Handler2010.class);

	@Override
	public void doHandle(IMsg m) {
		if (m.getClass().isInstance(Msg2010.class)) {
			Msg2010 msg = (Msg2010) m;
			TcpClient.getInstance().sendAnsworMsg(msg);
			List<TaxiInfo> l = msg.getList();
			for (TaxiInfo o : l) {
				CarInfo c = new CarInfo();
				c.setDriverName(o.getDrirver().getDriverName());
				c.setCompany(o.getDrirver().getCompany());
				c.setEmpty(o.getCarstate() == 0x00);
				c.setLat(o.getLat());
				c.setLon(o.getLon());
				c.setLisencenumber(o.getDrirver().getLicenseNumber());
				c.setId(o.getDrirver().getDriverSerial());
				c.setDriverphone(o.getDrirver().getDriverPhone());
				c.setDriverid(o.getDrirver().getDriverSerial());

				c.setCreditLevel(getCreditLeveString(o.getDrirver()
						.getCreditLevel()));
				LocationService.getInstance().update(c);				
				TaxiDistrbuteService.getInstance().update(c);
			}

		}

	}

	String getCreditLeveString(byte v) {
		if (v == 1)
			return "A";
		else if (v == 2)
			return "AA";
		else if (v == 3)
			return "AAA";
		return "";
	}
}

package com.hdsx.taxi.woxing.cqcityserver.socket.hanlder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.CarInfo;
import com.hdsx.taxi.woxing.cqcityserver.socket.TcpClient;
import com.hdsx.taxi.woxing.cqmsg.msg.Msg2010;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.TaxiPointInfo;
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
		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - start"); //$NON-NLS-1$
		}

		if (m instanceof Msg2010) {
			Msg2010 msg = (Msg2010) m;
//			TcpClient.getInstance().sendAnsworMsg(msg);//位置同步不需要应答
			List<TaxiPointInfo> l = msg.getList();
			for (TaxiPointInfo o : l) {
				CarInfo c = new CarInfo();
				c.setDriverName(o.getDriver().getDriverName());
				c.setCompany(o.getDriver().getCompany());
				c.setEmpty(o.getState() == 0x00);
				c.setLat(o.getLat());
				c.setLon(o.getLon());
				c.setLisencenumber(o.getDriver().getLicenseNumber());
				c.setId(o.getDriver().getLicenseNumber());
				c.setDriverphone(o.getDriver().getDriverPhone());
				c.setDriverid(o.getDriver().getDriverSerial());

				c.setCreditLevel(getCreditLeveString(o.getDriver()
						.getCreditLevel()));
				LocationService.getInstance().update(c);
				TaxiDistrbuteService.getInstance().update(c);
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("doHandle(IMsg) - end"); //$NON-NLS-1$
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

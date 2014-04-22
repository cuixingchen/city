package com.hdsx.taxi.woxing.cqmsg.msg;

import java.util.ArrayList;
import java.util.List;

import com.hdsx.taxi.woxing.cqmsg.AbsMsg;
import com.hdsx.taxi.woxing.cqmsg.msg.pojo.DriverInfo;

/**
 * 车辆位置同步信息
 * 
 * @author Steven
 * 
 */
public class Msg2010 extends AbsMsg {

	List<TaxiInfo> list = new ArrayList<>();

	@Override
	protected int getMsgID() {

		return 0x2010;
	}

	@Override
	protected byte[] bodytoBytes() {
		// TODO 完成车辆位置同步信息
		return null;
	}

	public List<TaxiInfo> getList() {
		return list;
	}

	public void setList(List<TaxiInfo> list) {
		this.list = list;
	}

	@Override
	protected boolean bodyfrombytes(byte[] b) {
		// TODO 完成车辆位置同步信息
		return false;
	}

	public class TaxiInfo {
		DriverInfo drirver = new DriverInfo();
		long lon, lat;
		byte carstate;

		public byte getCarstate() {
			return carstate;
		}

		public void setCarstate(byte carstate) {
			this.carstate = carstate;
		}

		public DriverInfo getDrirver() {
			return drirver;
		}

		public void setDrirver(DriverInfo drirver) {
			this.drirver = drirver;
		}

		public long getLon() {
			return lon;
		}

		public void setLon(long lon) {
			this.lon = lon;
		}

		public long getLat() {
			return lat;
		}

		public void setLat(long lat) {
			this.lat = lat;
		}
	}
}

package com.hdsx.taxi.woxing.cqmsg.msg.pojo;


public class TaxiPointInfo {
	@Override
	public String toString() {
		return "TaxiPointInfo [driver=" + driver + ", lon=" + lon + ", lat="
				+ lat + ", state=" + state + "]";
	}

	DriverInfo driver = new DriverInfo();
	long lon, lat;
	byte state;

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public DriverInfo getDriver() {
		return driver;
	}

	public void setDriver(DriverInfo driver) {
		this.driver = driver;
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
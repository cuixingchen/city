package com.hdsx.taxi.woxing.cqmsg.msg.pojo;


public class TaxiPointInfo {
	@Override
	public String toString() {
		return "TaxiPointInfo [driver=" + driver + ", lon=" + lon + ", lat="
				+ lat + ", state=" + state + "]";
	}

	DriverInfo driver = new DriverInfo();
	double lon, lat;
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

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	
}
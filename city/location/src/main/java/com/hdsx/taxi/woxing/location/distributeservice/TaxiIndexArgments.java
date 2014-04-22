package com.hdsx.taxi.woxing.location.distributeservice;


/**
 * 打车指数
 * @author Steven
 *
 */
public class TaxiIndexArgments {
	double minvalue, maxvalue;
	int waittime;
	public double getMinvalue() {
		return minvalue;
	}
	public void setMinvalue(double minvalue) {
		this.minvalue = minvalue;
	}
	public double getMaxvalue() {
		return maxvalue;
	}
	public void setMaxvalue(double maxvalue) {
		this.maxvalue = maxvalue;
	}
	public int getWaittime() {
		return waittime;
	}
	public void setWaittime(int waittime) {
		this.waittime = waittime;
	}

}

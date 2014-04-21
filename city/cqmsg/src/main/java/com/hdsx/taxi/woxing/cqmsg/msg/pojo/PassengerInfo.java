package com.hdsx.taxi.woxing.cqmsg.msg.pojo;

import java.io.Serializable;

/**
 * *****************************************************************************
 * <br/><b>类名:PassengerInfo</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：7.2	PassengerInfo乘客数据结构<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class PassengerInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String passengerName;//	乘客名称	String	
	private byte passengerSex;//	乘客性别	byte	1：男，2：女
	private String passengerPhone;//	乘客电话	String	
	public String getPassengerName() {
		return passengerName;
	}
	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	public byte getPassengerSex() {
		return passengerSex;
	}
	public void setPassengerSex(byte passengerSex) {
		this.passengerSex = passengerSex;
	}
	public String getPassengerPhone() {
		return passengerPhone;
	}
	public void setPassengerPhone(String passengerPhone) {
		this.passengerPhone = passengerPhone;
	}
	
}

package com.hdsx.taxi.woxing.cqmsg.msg.pojo;

import java.io.Serializable;

/**
 * *****************************************************************************
 * <br/><b>类名:OrderInfo</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：7.3	OrderInfo 订单数据结构<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 *****************************************************************************
 */
public class OrderInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PassengerInfo passengerInfo;//	乘客	Passenger	
	private byte orderType;//	订车类型	byte	0 即时订单、1预约订单
	private short tips;//	电召服务费	short	单位：角
	private String useTime;//	用车时间	BCD[7]	yyyymmddHHmmss
	private String address;//	用车地址	String	
	private int useLng;//	用车经度	UINT32	1/10000分
	private int useLat;//	用车纬度	UINT32	1/10000分
	private String destination;//	目的地地址	String	
	private int destLng;//	目的地经度	UINT32	1/10000分
	private int destLat;//	目的地纬度	UINT32	1/10000分
	private String remark;//	备注信息	String	
	public PassengerInfo getPassengerInfo() {
		return passengerInfo;
	}
	public void setPassengerInfo(PassengerInfo passengerInfo) {
		this.passengerInfo = passengerInfo;
	}
	public byte getOrderType() {
		return orderType;
	}
	public void setOrderType(byte orderType) {
		this.orderType = orderType;
	}
	public short getTips() {
		return tips;
	}
	public void setTips(short tips) {
		this.tips = tips;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getUseLng() {
		return useLng;
	}
	public void setUseLng(int useLng) {
		this.useLng = useLng;
	}
	public int getUseLat() {
		return useLat;
	}
	public void setUseLat(int useLat) {
		this.useLat = useLat;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getDestLng() {
		return destLng;
	}
	public void setDestLng(int destLng) {
		this.destLng = destLng;
	}
	public int getDestLat() {
		return destLat;
	}
	public void setDestLat(int destLat) {
		this.destLat = destLat;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}

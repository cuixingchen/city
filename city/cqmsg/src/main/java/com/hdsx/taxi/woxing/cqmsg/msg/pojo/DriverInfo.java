package com.hdsx.taxi.woxing.cqmsg.msg.pojo;

import java.io.Serializable;

/**
 * ***************************************************************************** <br/>
 * <b>类名:DriverInfo</b> <br/>
 * 编写人: 谢广泉 <br/>
 * 日期: 2014年4月14日<br/>
 * 功能：7.1 DriverInfo驾驶员数据结构<br/>
 * 
 * @author gq
 * @version 1.0.0
 * 
 ***************************************************************************** 
 */
public class DriverInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String driverName;// 驾驶员姓名 string[16]
	@Override
	public String toString() {
		return "DriverInfo [driverName=" + driverName + ", company=" + company
				+ ", driverPhone=" + driverPhone + ", driverSerial="
				+ driverSerial + ", creditLevel=" + creditLevel
				+ ", licenseNumber=" + licenseNumber + "]";
	}
	private String company;// 驾驶员公司 string[16] 公司简称
	private String driverPhone;// 驾驶员电话 String[11] 车载终端电话
	private String driverSerial;// 从业人员id号 String[19] 驾驶员id号，驾驶员监督卡号
	private byte creditLevel;// 信誉考核等级 byte 1:A,2:AA,3:AAA
	private String licenseNumber;// 车牌号 String[8]
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	public String getDriverSerial() {
		return driverSerial;
	}
	public void setDriverSerial(String driverSerial) {
		this.driverSerial = driverSerial;
	}
	public byte getCreditLevel() {
		return creditLevel;
	}
	public void setCreditLevel(byte creditLevel) {
		this.creditLevel = creditLevel;
	}
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

}

package com.hdsx.taxi.woxing.cityservice;

import com.hdsx.taxi.woxing.cityservice.service.IComplaintsService;
import com.hdsx.taxi.woxing.cityservice.service.ILocationService;
import com.hdsx.taxi.woxing.cityservice.service.IOrderService;
import com.hdsx.taxi.woxing.cityservice.service.ITaxiDistrbuteService;
import com.hdsx.taxi.woxing.cityservice.service.ITaxiStationService;

/**
 * 保存系统变量
 * 
 * @author Steven
 * 
 */

public class Constants {

	static Constants obj;

	public static Constants getInstance() {
		if (obj == null)
			obj = new Constants();
		return obj;

	}

	String mqhosturl; // mq 主机地址
	String mquser; // mq 用户名
	String mqpasswd; // mq 密码
	String queuename; // 管道名
	ILocationService locationservice; // 车辆服务
	IOrderService orderservice; // 订票服务
	IComplaintsService complainetsservice; // 投诉服务
	ITaxiDistrbuteService taxidistrbuteservice; // 打车服务
	ITaxiStationService taxistationservice; // 扬招站服务

	boolean isdebug = true;

	boolean isToRealCar = false;

	public boolean isIsdebug() {
		return isdebug;
	}

	public void setIsdebug(boolean isdebug) {
		this.isdebug = isdebug;
	}

	public boolean isToRealCar() {
		return isToRealCar;
	}

	public void setToRealCar(boolean isToRealCar) {
		this.isToRealCar = isToRealCar;
	}

	public ILocationService getLocationservice() {
		return locationservice;
	}

	public String getMqhosturl() {
		return mqhosturl;
	}

	public void setMqhosturl(String mqhosturl) {
		this.mqhosturl = mqhosturl;
	}

	public String getMquser() {
		return mquser;
	}

	public void setMquser(String mquser) {
		this.mquser = mquser;
	}

	public String getMqpasswd() {
		return mqpasswd;
	}

	public void setMqpasswd(String mqpasswd) {
		this.mqpasswd = mqpasswd;
	}

	public String getQueuenameIn() {
		return queuename + ".tocity";
	}

	public String getQueuenameOut() {
		return queuename + ".fromcity";
	}

	public void setQueuename(String queuename) {
		this.queuename = queuename;
	}

	public void setLocationservice(ILocationService locationservice) {
		this.locationservice = locationservice;
	}

	public IOrderService getOrderservice() {
		return orderservice;
	}

	public void setOrderservice(IOrderService orderservice) {
		this.orderservice = orderservice;
	}

	public IComplaintsService getComplainetsservice() {
		return complainetsservice;
	}

	public void setComplainetsservice(IComplaintsService complainetsservice) {
		this.complainetsservice = complainetsservice;
	}

	public ITaxiDistrbuteService getTaxiDistrbuteService() {
		return this.taxidistrbuteservice;
	}

	public void setTaxiDistrbuteService(
			ITaxiDistrbuteService taxidistrbuteservice) {
		this.taxidistrbuteservice = taxidistrbuteservice;
	}

	public ITaxiStationService getTaxistationservice() {
		return taxistationservice;
	}

	public void setTaxistationservice(ITaxiStationService taxistationservice) {
		this.taxistationservice = taxistationservice;
	}

}

package com.hdsx.taxi.woxing.cityservice.service;

import java.util.List;

import com.hdsx.taxi.woxing.bean.TaxiStation;

/*******************************************************************************
 * <b>类名:ITaxiStationService</b> <br/>
 * 功能：扬招站<br/>
 * 日期: 2013年9月14日<br/>
 * 
 * @author 谢广泉 xiegqooo@gmail.com
 * @version 1.0.0
 * 
 ******************************************************************************/
public interface ITaxiStationService {
	/**
	 * 根据范围查询出租车扬招站
	 * 
	 * @param xmin
	 * @param xmax
	 * @param ymin
	 * @param ymax
	 * @return
	 */
	public List<TaxiStation> query(double xmin, double xmax, double ymin,
			double ymax);
	/**
	 * 根据当前点查询出租车扬招站
	 * 
	 * @param x
	 * @param y
	 * @param distance
	 * @return
	 */
	public List<TaxiStation> query(double x, double y, int distance);
}

package com.hdsx.taxi.woxing.cityservice.service;

import java.util.List;

import com.hdsx.taxi.woxing.bean.TaxiIndex;

/**
 * 计算车辆分布，以达到指导打车的目的
 * 
 * @author Steven
 * 
 */
public interface ITaxiDistrbuteService {

	/**
	 * 计算打车指数结果
	 * 
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 * @return
	 */
	public List<TaxiIndex> getTaxiIndex(double xmin, double ymin, double xmax,
			double ymax);

	/**
	 * 查询当前位置打车指数
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getCurLocationIndex(double x, double y);
}

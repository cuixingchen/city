package com.hdsx.taxi.woxing.cityservice.service;

import java.util.List;

import com.hdsx.taxi.woxing.bean.CarInfo;


/**
 * 位置服务接口
 * 
 * @author Steven
 * 
 */
public interface ILocationService {

	/**
	 * 根据车辆id查询车辆信息
	 * 
	 * @param carnum
	 *            车辆id
	 * @return
	 */
	public CarInfo getCarInfoByCarNum(String carnum);

	/**
	 * 获取在线车辆数
	 * 
	 * @return
	 */
	public int getOnlineCarCount();

	/**
	 * 获取在线空车数量
	 * 
	 * @return
	 */
	public int getOnlineEmptyCarCount();

	/**
	 * 城市id
	 * 
	 * @return
	 */
	public String getCityID();

	/**
	 * 更新车辆信息
	 * 
	 * @param car
	 *            车辆信息
	 */
	public void update(CarInfo car);

	/**
	 * 获取周围范围车辆信息
	 * 
	 * @param lon
	 *            经度
	 * @param lat
	 *            纬度
	 * @param distance
	 * @return
	 */
	public List<CarInfo> getEmptycarByDistance(double lon, double lat,
			int distance);

	/**
	 * 获取周围范围车辆数量
	 * 
	 * @param lon
	 *            经度
	 * @param lat
	 *            纬度
	 * @param distance
	 * @return
	 */
	public int getEmptycarNumByDistance(double lon, double lat, int disntance);

}

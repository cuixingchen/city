package com.hdsx.taxi.woxing.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.hdsx.taxi.woxing.bean.CarInfo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import com.vividsolutions.jts.util.Debug;

public class CarContainer {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(CarContainer.class);

	/**
	 * Logger for this class
	 */

	boolean isQuerying = false;
	static CarContainer obj;
	// Quadtree emptytree;
	SpatialIndex emptytree;
	SpatialIndex loadedtree;
	HashMap<String, CarInfo> map;
	Queue<CarInfo> todo_queue = new LinkedList<CarInfo>();
	List<CarInfo> list_todo = new ArrayList<CarInfo>();// 查询时候不能更新车辆信息，将更新的车辆信息暂存与此
	int carCount = 0;
	int emptycarcount = 0, loadedcarcount = 0;
	static final double minExtend = 10E-6;

	int oldsize = 0;

	// static final double minExtend = 10E-6;

	public static CarContainer getInstance() {
		if (obj == null)
			obj = new CarContainer();
		return obj;
	}

	public CarContainer() {
		emptytree = new Quadtree();
		// emptytree=new STRtree();
		loadedtree = new Quadtree();
		map = new HashMap<String, CarInfo>();
	}

	/**
	 * 更新车辆位置
	 * 
	 * @param car
	 *            车辆位置
	 */
	public void updateCar(CarInfo car) {
		if (!car.isGpsavailabe())
			return;// gps未定位车辆直接忽略
		if (car.getLat() == 0 || car.getLon() == 0)
			return;
		if (isQuerying) {
			todo_queue.offer(car);
			return;
		}

		CarInfo oldcar = map.get(car.getId());
		Envelope oldenv;

		if (oldcar != null) {// 已有车辆处理

			oldenv = oldcar.toEnv();
			oldenv.expandBy(minExtend);
			if (oldcar.isEmpty()) {
				emptytree.remove(oldenv, oldcar);
				emptycarcount--;
			} else {
				loadedtree.remove(oldenv, oldcar);
				loadedcarcount--;
			}
		}
		Envelope newenv = car.toEnv();
		newenv.expandBy(minExtend);
		if (car.isEmpty()) {
			emptytree.insert(newenv, car);
			emptycarcount++;
		} else {
			loadedtree.insert(newenv, car);
			loadedcarcount++;
		}
		map.put(car.getId(), car);
		if (todo_queue.size() > 0) {
			updateCar(todo_queue.poll());
		}

		if (logger.isDebugEnabled()) {
			int size = map.size();
			if (size % 10 == 0) {
				int newsize = size / 10;
				if (oldsize != newsize) {

					this.oldsize = newsize;
					//					logger.debug("updateCar(CarInfo) - 总车辆数 :" + map.size() + "  空车数:" + emptytree.queryAll().size() + " 重车数：" + loadedtree.queryAll().size()); //$NON-NLS-1$
					logger.debug("updateCar(CarInfo) - 总车辆数 :" + map.size() + "  空车数2:" + emptycarcount + " 重车数2：" + loadedcarcount); //$NON-NLS-1$
				}
			}
		}
	}

	/**
	 * 更新车辆位置
	 * 
	 * @param car
	 * @deprecated
	 */

	public void updateCar2(CarInfo car) {
		if (car.getLat() == 0 || car.getLon() == 0)
			return;
		if (isQuerying) {
			todo_queue.offer(car);
			return;
		}

		CarInfo oldcar = map.get(car.getId());
		Envelope oldenv;

		if (oldcar != null) {// 已有车辆处理

			oldenv = oldcar.toEnv();
			oldenv.expandBy(minExtend);
			if (emptytree.remove(oldenv, oldcar)) {
				Debug.println("XXX");
			} else {
				Debug.println("YYY");

			}
			map.remove(car.getId());
		}

		if (car.isEmpty() && car.isGpsavailabe()) {// 空车加入
			Envelope newenv = car.toEnv();
			newenv.expandBy(minExtend);
			map.put(car.getId(), car);
			emptytree.insert(newenv, car);
		}

		if (todo_queue.size() > 0) {
			updateCar(todo_queue.poll());
		}

		int size = map.size();

		if (size % 100 == 0) {
			int newsize = size / 100;
			if (oldsize != newsize) {
				this.oldsize = newsize;
				if (logger.isDebugEnabled()) {
					//					logger.debug("updateCar(CarInfo) - 总车辆数 :" + map.size() + "  空车数:" + emptytree.queryAll().size() + " 重车数：" + loadedtree.queryAll().size()); //$NON-NLS-1$
					logger.debug("updateCar(CarInfo) - 总车辆数 :" + map.size() + "  空车数2:" + emptycarcount + " 重车数2：" + loadedcarcount); //$NON-NLS-1$
				}
			}
		}

	}

	/**
	 * 根据范围查询空车辆
	 * 
	 * @param x
	 * @param y
	 * @param dis
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CarInfo> QueryByDistance(double x, double y, double dis) {
		isQuerying = true;
		Envelope env = GeometryUtil.getInstance().getBufferEnv(x, y, dis);
		if (logger.isDebugEnabled()) {
			logger.debug("QueryByDistance(double, double, double) - 查询空车  x:" + x + "  y:" + y + "  distance:" + dis + " Env:" + env); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		List<CarInfo> lst = emptytree.query(env);

		isQuerying = false;

		List<CarInfo> lst2 = new ArrayList<>();
		for (CarInfo car : lst) {
			if (env.contains(new Coordinate(car.getLon(), car.getLat()))) {
				lst2.add(car);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("QueryByDistance(double, double, double) - 查询空车结果数量:" + lst2.size()); //$NON-NLS-1$
		}
		return lst2;
	}

	/**
	 * 根据车ID查询车辆信息
	 * 
	 * @param id
	 * @return
	 */
	public CarInfo queryCar(String id) {
		return map.get(id);
	}

	/**
	 * 获取空车数量
	 * 
	 * @return
	 */
	public int getEmptyCount() {
		return emptycarcount;

	}

	/**
	 * 获取所有车辆数量
	 * 
	 * @return
	 */
	public int getOnlineCarCount() {
		return map.size();
	}

	/**
	 * 获取重车车辆数量
	 * 
	 * @return
	 */
	public int getLoadedCarCount() {
		return loadedcarcount;
	}
}

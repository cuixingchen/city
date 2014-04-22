package com.hdsx.taxi.woxing.location;

import java.util.ArrayList;
import java.util.List;

import com.hdsx.taxi.woxing.bean.CarInfo;
import com.hdsx.taxi.woxing.cityservice.service.ILocationService;
import com.vividsolutions.jts.geom.Coordinate;

public class LocationService implements ILocationService {

	static LocationService obj;

	public static LocationService getInstance() {
		if (obj == null)
			obj = new LocationService();
		return obj;
	}

	@Override
	public CarInfo getCarInfoByCarNum(String carnum) {
		return CarContainer.getInstance().queryCar(carnum);
	}

	@Override
	public int getOnlineCarCount() {
		return CarContainer.getInstance().getOnlineCarCount();

	}

	@Override
	public int getOnlineEmptyCarCount() {
		return CarContainer.getInstance().getEmptyCount();
	}

	@Override
	public String getCityID() {

		return "023";
	}

	@Override
	public void update(CarInfo car) {
		CarContainer.getInstance().updateCar(car);
	}

	@Override
	public List<CarInfo> getEmptycarByDistance(double lon, double lat,
			int distance) {
		Coordinate cor = new Coordinate(lon, lat);
		List<CarInfo> result = CarContainer.getInstance().QueryByDistance(
				cor.x, cor.y, distance);
		List<CarInfo> result1 = new ArrayList<CarInfo>();

		for (CarInfo car : result) {

			CarInfo c = car.copy();
			result1.add(c);
		}

		return result1;
	}

	@Override
	public int getEmptycarNumByDistance(double lon, double lat, int distance) {
		List<CarInfo> list = getEmptycarByDistance(lon, lat, distance);
		return list.size();

	}

}

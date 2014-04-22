package com.hdsx.taxi.woxing.location;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

public class GeometryUtil {

	static GeometryUtil obj;
	GeometryFactory geofac;
	static int SRID = 4326;

	// final static float EARTH_EQUATORIAL_CIRUMFERENCE = 6378200;
	final static double c = 360d / (Math.PI * 6378200d);

	public static GeometryUtil getInstance() {
		if (obj == null)
			obj = new GeometryUtil();
		return obj;
	}

	public GeometryUtil() {

		PrecisionModel precisionModel = new PrecisionModel(100000);
		geofac = new GeometryFactory(precisionModel, SRID);
	}

	/**
	 * 几何对象工厂
	 * 
	 * @return
	 */
	public GeometryFactory getGeometryFactory() {
		return geofac;
	}

	/**
	 * 根据经纬度和距离计算范围
	 * 
	 * @param x
	 * @param y
	 * @param b
	 *            范围，以米为单位
	 * @return
	 */
	public Envelope getBufferEnv(double x, double y, double b) {

		double ybuffer = b * c / 2;
		double xbuffer = b * c / Math.cos(Math.PI * y / 180) / 2;
	
		Envelope env = new Envelope(x - xbuffer, x + xbuffer, y - ybuffer, y
				+ ybuffer);
		return env;
	}
}

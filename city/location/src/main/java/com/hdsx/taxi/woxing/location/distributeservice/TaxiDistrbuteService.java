package com.hdsx.taxi.woxing.location.distributeservice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.CarInfo;
import com.hdsx.taxi.woxing.bean.TaxiIndex;
import com.hdsx.taxi.woxing.cityservice.service.ITaxiDistrbuteService;
import com.hdsx.taxi.woxing.location.util.Config;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;

/**
 * 计算车辆分布，以达到指导打车的目的
 * 
 * @author Steven
 * 
 */
public class TaxiDistrbuteService implements ITaxiDistrbuteService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TaxiDistrbuteService.class);

	static TaxiDistrbuteService obj;
	double xmin, ymin, xmax, ymax, cell;
	STRtree tree;
	STRtree curtree;

	boolean isIniting = false;
	private Timer timer = null;

	public static TaxiDistrbuteService getInstance() {
		if (obj == null) {
			obj = new TaxiDistrbuteService();
		}
		return obj;
	}

	/**
	 * 重新开始获取车辆位置信息，同时将已经搞定的数据变为历史数据
	 */
	public void recal(boolean istiff) {
		curtree = tree;
		initGrid();
		if (Config.ISGENERATETIFF)
			genTiff();
	}

	void genTiff() {
		try {

			Date dt = new Date();

			String filename = "img" + dt.getYear() + "-" + dt.getMonth() + "-"
					+ dt.getDay() + "-" + dt.getHours() + "-" + dt.getMinutes()
					+ "-" + dt.getSeconds() + ".tiff";

			TaxiIndexZoneArgments arg = Config.TAXI_DISTRIBUTE_ZONES.get(0);
			Envelope2D env = new Envelope2D(DefaultGeographicCRS.WGS84,
					arg.getXmin(), arg.getYmin(),
					arg.getXmax() - arg.getXmin(), arg.getYmax()
							- arg.getYmin());
			GridCoverageFactory gcf = new GridCoverageFactory();

			GridCoverage2D grid = gcf.create(
					"TAXI_CLUSTER",
					TaxiDistrbuteService.getInstance().getmatrix(arg.getXmax(),
							arg.getYmin(), arg.getXmax(), arg.getYmax()), env);
			String outfilepath = Config.TIFF_FILE_PATH + "//img" + filename
					+ ".tiff";
			File f1 = new File(outfilepath);
			GeoTiffWriter awriter;

			awriter = new GeoTiffWriter(f1);
			awriter.write(grid, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 循环
	 */
	void time() {
		timer = new Timer(true);
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				curtree = tree;
				initGrid();
			}
		};
		long delay = Config.TAXI_DISTRIBUTE_DELAY;
		long period = Config.TAXI_DISTRIBUTE_DELAY;
		timer.schedule(task, delay, period);
	}

	/**
	 * 初始话网格
	 * 
	 */
	void initGrid() {
		isIniting = true;
		tree = new STRtree();
		for (TaxiIndexZoneArgments arg : Config.TAXI_DISTRIBUTE_ZONES) {
			RasterGrids grid = new RasterGrids(arg.getXmin(), arg.getYmin(),
					arg.getXmax(), arg.getYmax(), arg.getCell());
			Envelope env = new Envelope(arg.getXmin(), arg.getXmax(),
					arg.getYmin(), arg.getYmax());
			tree.insert(env, grid);
			if (logger.isInfoEnabled()) {
				logger.info("TaxiDistrbuteService() - 初始网格范围成功:" + env); //$NON-NLS-1$
			}

		}
		isIniting = false;
	}

	public TaxiDistrbuteService() {
		initGrid();
		// time();
	}

	/**
	 * 更新车辆信息
	 * 
	 * @param car
	 */

	public void update(CarInfo car) {
		if (!Config.TAXI_DISTRIBUTE_ENABLED)
			return;
		if (isIniting)
			return;
		// Thread thread = new Thread() {
		//
		// @Override
		// public void run() {

		// if (car1.getLat() == 0)
		// return;
		// if (car1.getLon() == 0)
		// return;
		// CarInfo car = car1.copy();
		// double[] a = new double[2];
		// ReOffset.getOffsetObj().offsetCoord(car1.getLon(), car1.getLat(), a);

		// Coordinate c = ReOffset.offset2Gaode(car.getLon(), car.getLat());
		// car.setLon(c.x);
		// car.setLat(c.y);
		Envelope env = car.toEnv();
		List<RasterGrids> lst = tree.query(env);
		if (lst == null)
			return;
		if (lst.size() == 0)
			return;
		RasterGrids grid = lst.get(0);
		if (grid == null)
			return;
		grid.putCar(car.getLon(), car.getLat(), car.getDirection());

		// }
		//
		// };
		// thread.start();

	}

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
			double ymax) {
		Envelope env = new Envelope(xmin, xmax, ymin, ymax);
		if (curtree == null) {
			return null;
		}
		List<RasterGrids> lst = curtree.query(env);

		if (lst == null)
			return null;
		List<TaxiIndex> result = new ArrayList<TaxiIndex>();

		for (RasterGrids grid : lst) {
			float[][] matrix = grid.getMatrix(xmin, ymin, xmax, ymax);

			xmin = xmin > grid.getXmin() ? xmin : grid.getXmin();
			ymin = ymin > grid.getYmin() ? ymin : grid.getYmin();
			TaxiIndexCalculator cal = new TaxiIndexCalculator(matrix, xmin,
					ymin, grid.getCell());
			result.addAll(cal.getAggreationArea());

			// if (logger.isDebugEnabled())
			// for (TaxiIndex ti : result) {
			// logger.debug(ti.toString());
			// }

		}

		return result;

	}

	public float[][] getmatrix(double xmin, double ymin, double xmax,
			double ymax) {
		Envelope env = new Envelope(xmin, xmax, ymin, ymax);
		if (curtree == null)
			return null;
		List<RasterGrids> lst = curtree.query(env);

		if (lst == null)
			return null;
		if (lst.size() < 1)
			return null;

		RasterGrids grid = lst.get(0);
		return grid.getMatrix(xmin, ymin, xmax, ymax);

	}

	/**
	 * 查询当前位置打车指数
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getCurLocationIndex(double x, double y) {
		Envelope env = new Envelope(x, x, y, y);
		if (null == curtree) {
			return 0;
		}
		@SuppressWarnings("unchecked")
		List<RasterGrids> lst = curtree.query(env);

		if (lst == null)
			return -1;
		if (lst.size() < 1)
			return -1;

		RasterGrids grid = lst.get(0);

		float value = grid.getValueByXY(x, y);
		if (logger.isDebugEnabled()) {
			logger.debug("getCurLocationIndex(double, double) - 查询当前点值:" + value); //$NON-NLS-1$
		}
		return CalIndex2(value);
	}

	/**
	 * 计算通过空车的数量
	 * 
	 * @param value
	 * @return
	 */
	private int CalIndex2(float value) {
		float v = value / RasterGrids.FIRST_ADD_VALUE;

		return (int) Math.floor(v);

	}

	private int CalIndex(float value) {

		if (value <= 0) // 没有值时候的值为-1
			return -1;

		for (TaxiIndexArgments a : Config.TAXI_INDEX_ARGS) {
			if (value >= a.getMinvalue() && value <= a.getMaxvalue()) {
				return a.getWaittime();
			}
		}
		return -1;
	}
}

package com.hdsx.taxi.woxing.location.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.cityservice.Constants;
import com.hdsx.taxi.woxing.location.distributeservice.TaxiIndexArgments;
import com.hdsx.taxi.woxing.location.distributeservice.TaxiIndexZoneArgments;

public class Config {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Config.class);

	public static boolean TAXI_DISTRIBUTE_ENABLED = false;
	public static boolean TAXI_DISTRIBUTE_REDUCED = false;

	public static boolean ISGENERATETIFF = false; // 是否生成tiff

	public static long TAXI_DISTRIBUTE_DELAY = 0;

	//

	private static Config instance;

	public static synchronized Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}

	public Config() {
		init();
	}

	public static List<TaxiIndexZoneArgments> TAXI_DISTRIBUTE_ZONES = new ArrayList<TaxiIndexZoneArgments>();

	public static List<TaxiIndexArgments> TAXI_INDEX_ARGS = new ArrayList<TaxiIndexArgments>();

	public static String TIFF_FILE_PATH;

	static void init() {
		try {
			Properties prop = new Properties();
			prop.load(Config.class
					.getResourceAsStream("/distributeservice.properties"));

			String zonelist = prop.getProperty("cardistribute.zones");
			if (zonelist != null) {
				String[] zones = zonelist.split(",");
				for (String zone : zones) {
					TaxiIndexZoneArgments arg = new TaxiIndexZoneArgments();
					arg.setId(zone);
					arg.setXmin(Double.parseDouble(prop
							.getProperty("cardistribute." + zone + ".xmin")));
					arg.setXmax(Double.parseDouble(prop
							.getProperty("cardistribute." + zone + ".xmax")));
					arg.setYmin(Double.parseDouble(prop
							.getProperty("cardistribute." + zone + ".ymin")));
					arg.setYmax(Double.parseDouble(prop
							.getProperty("cardistribute." + zone + ".ymax")));
					arg.setCell(Double.parseDouble(prop
							.getProperty("cardistribute." + zone + ".cell")));
					TAXI_DISTRIBUTE_ZONES.add(arg);

				}
				TAXI_DISTRIBUTE_DELAY = Long.parseLong(prop
						.getProperty("cardistribute.delaytime"));
			}

			String indexargscount = prop.getProperty("taxiindex.count");
			if (indexargscount != null) {

				int indexcount = Integer.parseInt(indexargscount);
				for (int i = 1; i <= indexcount; i++) {
					TaxiIndexArgments tia = new TaxiIndexArgments();
					tia.setMinvalue(Double.parseDouble(prop
							.getProperty("taxiindex." + i + ".min")));
					tia.setMaxvalue(Double.parseDouble(prop
							.getProperty("taxiindex." + i + ".max")));
					tia.setWaittime(Integer.parseInt(prop
							.getProperty("taxiindex." + i + ".waitetime")));
					TAXI_INDEX_ARGS.add(tia);
				}

			}
			TIFF_FILE_PATH = prop.getProperty("cardistribute.tifffpath");
			ISGENERATETIFF = Boolean.parseBoolean(prop
					.getProperty("cardistribute.istiff"));
			TAXI_DISTRIBUTE_REDUCED = Boolean.parseBoolean(prop
					.getProperty("cardistribute.isreduce"));

			TAXI_DISTRIBUTE_ENABLED = Boolean.parseBoolean(prop
					.getProperty("cardistribute.enabled"));
			Constants.getInstance().setIsdebug(
					Boolean.parseBoolean(prop.getProperty("debug.isdebug")));
			Constants.getInstance()
					.setToRealCar(
							Boolean.parseBoolean(prop
									.getProperty("debug.istorealcar")));

			logger.info("初始化系统参数成功");
		} catch (IOException e) {
			logger.error("初始化系统参数失败", e);
		}
	}
}

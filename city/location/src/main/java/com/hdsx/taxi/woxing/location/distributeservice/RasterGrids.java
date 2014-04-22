package com.hdsx.taxi.woxing.location.distributeservice;

/**
 * 存储车辆位置的网格信息
 * 
 * @author Steven
 * 
 */
public class RasterGrids {
	static String GRID_NAME = "TAXI_CLUSTER";
	public static float FIRST_ADD_VALUE = 100f;
	static float REDUCE_FACTOR = 0.9f;
	static int RANGE_CELL_NUM = 20;

	double xmin, ymin, xmax, ymax;
	double width, height;
	double cell;
	int col, row;

	float[][] matrix;

	int distanceSquar;

	/**
	 * 构造函数
	 * 
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 * @param cell
	 */
	public RasterGrids(double xmin, double ymin, double xmax, double ymax,
			double cell) {
		this.xmax = xmax;
		this.xmin = xmin;
		this.ymin = ymin;
		this.ymax = ymax;
		this.cell = cell;
		this.width = this.xmax - this.xmin;
		this.height = this.ymax - this.ymin;
		calcColRow();
		distanceSquar = RANGE_CELL_NUM * RANGE_CELL_NUM;
	}

	/**
	 * 根据范围和网格大小计算行列数
	 */
	void calcColRow() {
		this.row = (int) Math.round(this.height / this.cell);
		this.col = (int) Math.round(this.width / this.cell);
		matrix = new float[this.col][this.row];
	}

	/**
	 * 根据x坐标计算Col值
	 * 
	 * @param x
	 * @return
	 */
	int cal2Col(double x) {
		double xvalue = x - this.xmin;
		float col = (float) (xvalue / this.cell);
		return this.col - Math.round(col);
	}

	/**
	 * 根据y坐标计算row值
	 * 
	 * @param y
	 * @return
	 */
	int cal2Row(double y) {
		double yvalue = y - this.ymin;
		float row = (float) (yvalue / this.cell);
		return Math.round(row);

	}

	public double getXmin() {
		return xmin;
	}

	public void setXmin(double xmin) {
		this.xmin = xmin;
	}

	public double getYmin() {
		return ymin;
	}

	public void setYmin(double ymin) {
		this.ymin = ymin;
	}

	public double getXmax() {
		return xmax;
	}

	public void setXmax(double xmax) {
		this.xmax = xmax;
	}

	public double getYmax() {
		return ymax;
	}

	public void setYmax(double ymax) {
		this.ymax = ymax;
	}

	public double getCell() {
		return cell;
	}

	public void setCell(double cell) {
		this.cell = cell;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	// /**
	// * 返回GridCoverage2D对象
	// *
	// * @return
	// */
	// public GridCoverage2D toGridCoverage2D() {
	//
	// Envelope env = new Envelope2D(DefaultGeographicCRS.WGS84, this.ymin,
	// this.xmin, this.height, this.width);
	// GridCoverageFactory gcf = new GridCoverageFactory();
	// GridCoverage2D grid = gcf.create(GRID_NAME, matrix, env);
	// return grid;
	//
	// }

	public void putCar(double lon, double lat, int dir) {

		int x = (int) Math.round((lon - this.xmin) / this.cell);
		int y = (int) Math.round((lat - this.ymin) / this.cell);

		addValue(x, this.row - y, dir, FIRST_ADD_VALUE);
	}

	void addValue(int x, int y, int dir, float value) {
		int xStart = x - RANGE_CELL_NUM > 0 ? x - RANGE_CELL_NUM : 0;
		int yStart = y - RANGE_CELL_NUM > 0 ? y - RANGE_CELL_NUM : 0;
		int xEnd = x + RANGE_CELL_NUM < this.col ? x + RANGE_CELL_NUM
				: this.col;
		int yEnd = y + RANGE_CELL_NUM < this.row ? y + RANGE_CELL_NUM
				: this.row;

		int gpsangle = 360 - dir - 90;
		// gpsangle = (360 + gpsangle) % 360;
		// gpsangle = gpsangle % 180;
		double gpsangleInRadio = Math.toRadians(gpsangle); // gps角度

		for (int i = xStart; i < xEnd; i++) {
			for (int j = yStart; j < yEnd; j++) {
				int disx = Math.abs(x - i);
				int disy = Math.abs(y - j);
				int dis1 = disx * disx + disy * disy;
				if (dis1 < this.distanceSquar) {
					int dis = disx > disy ? disx : disy;

					double angle = Math.atan2(j - y, i - x); // 当前像素与起点位置的角度
					angle = angle - gpsangleInRadio;
					// angle = Math.abs(angle);
					float v1 = (float) (value * Math.pow(REDUCE_FACTOR, dis));
					v1 = (float) (Math.sin(angle / 3) * v1);

					float v = matrix[i][j];
					matrix[i][j] = v + Math.abs(v1);
				}
			}
		}

	}

	// void addValueWithAngle(int x, int y, int dir, float value) {
	// int xStart = x - RANGE_CELL_NUM > 0 ? x - RANGE_CELL_NUM : 0;
	// int yStart = y - RANGE_CELL_NUM > 0 ? y - RANGE_CELL_NUM : 0;
	// int xEnd = x + RANGE_CELL_NUM < this.row ? x + RANGE_CELL_NUM
	// : this.row;
	// int yEnd = y + RANGE_CELL_NUM < this.col ? y + RANGE_CELL_NUM
	// : this.col;
	//
	// for (int i = xStart; i < xEnd; i++) {
	// for (int j = yStart; j < yEnd; j++) {
	// int disx = Math.abs(x - i);
	// int disy = Math.abs(y - j);
	// int dis1 = disx * disx + disy * disy;
	// if (dis1 < this.distanceSquar) {
	// int dis = disx > disy ? disx : disy;
	// float v1 = (float) (value * Math.pow(REDUCE_FACTOR, dis));
	// float v = matrix[i][j];
	// matrix[i][j] = v + v1;
	// }
	// }
	// }
	//
	// }
	public float[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(float[][] matrix) {
		this.matrix = matrix;
	}

	/**
	 * 获取当前坐标点位置所在值
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public float getValueByXY(double x, double y) {
		if (x < this.xmin || x > this.xmax || y < this.ymin || y > this.ymax)
			return -1;

		int xIdx = (int) Math.round((x - this.xmin) / this.cell);
		int yIdx = (int) Math.round((y - this.ymin) / this.cell);
		yIdx = this.row - yIdx;
		return this.matrix[xIdx][yIdx];
	}

	/**
	 * 根据坐标范围返回网格数据
	 * 
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 * @return
	 */
	public float[][] getMatrix(double xmin, double ymin, double xmax,
			double ymax) {

		xmin = xmin < this.xmin ? this.xmin : xmin;
		xmax = xmax > this.xmax ? this.xmax : xmax;
		ymin = ymin < this.ymin ? this.ymin : ymin;
		ymax = ymax > this.ymax ? this.ymax : ymax;

		int xStart = (int) Math.round((xmin - this.xmin) / this.cell);
		int xEnd = (int) Math.round((xmax - this.xmin) / this.cell);

		//
		// int yStart = (int) Math.round((ymin - this.ymin) / this.cell);
		// int yEnd = (int) Math.round((ymax - this.ymin) / this.cell);

		int yEnd = (int) Math.round((ymin - this.ymin) / this.cell);
		int yStart = (int) Math.round((ymax - this.ymin) / this.cell);
		yEnd = this.row - yEnd;
		yStart = this.row - yStart;

		// int xStart = cal2Col(xmax);
		// int xEnd = cal2Col(xmin);
		// int yStart = cal2Row(ymin);
		// int yEnd = cal2Row(ymax);

		int xcount = xEnd - xStart;
		int ycount = yEnd - yStart;

		float[][] result = new float[ycount + 2][xcount + 2];

		for (int i = 0; i < xcount; i++) {
			for (int j = 0; j < ycount; j++) {
				result[j + 1][i + 1] = this.matrix[i + xStart][j + yStart];
			}
		}

		// float[][] result = new float[ycount][xcount];
		//
		// for (int i = 0; i < xcount; i++) {
		// for (int j = 0; j < ycount; j++) {
		// result[j][i] = this.matrix[i + xStart][j + yStart];
		// }
		// }
		return result;
	}

}
